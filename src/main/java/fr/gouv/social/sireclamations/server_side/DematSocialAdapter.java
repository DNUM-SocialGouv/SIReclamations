package fr.gouv.social.sireclamations.server_side;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.gouv.social.sireclamations.hexagone.Domicile;
import fr.gouv.social.sireclamations.hexagone.domain.DossierDeReclamation;
import fr.gouv.social.sireclamations.hexagone.domain.Etablissement;
import fr.gouv.social.sireclamations.hexagone.domain.LieuDeSurvenue;
import fr.gouv.social.sireclamations.hexagone.domain.ports.DematSocial;
import fr.gouv.social.sireclamations.hexagone.exceptions.CodePostalAbsentException;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;


import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DematSocialAdapter implements DematSocial {
    private final DematSocialApi dematSocialApi;

    private final OpenDataSoftApi openDataSoftApi;


    public DematSocialAdapter(Retrofit dematSocialRetrofit, OpenDataSoftApi openDataSoftApi) {
        this.dematSocialApi = dematSocialRetrofit.create(DematSocialApi.class);
        this.openDataSoftApi = openDataSoftApi;
    }

    @Override
    public DossierDeReclamation recupererDossier(int numeroDossier) throws IOException {
        GraphQLRequest request = new GraphQLRequest();
        request.setQueryId("ds-query-v2");
        request.setVariables(Map.of(
                "dossierNumber", numeroDossier,
                "includeGeometry", true,
                "includeInstructeurs", false
        ));
        request.setOperationName("getDossier");

        Call<ResponseBody> call = dematSocialApi.executeGraphQLQueryRaw(request);
        Response<ResponseBody> response = call.execute();

        return recupererDossierDeReclamationOuThrowErreurSiLeJsonEstInexploitable(numeroDossier, response);
    }

    @NotNull
    private DossierDeReclamation recupererDossierDeReclamationOuThrowErreurSiLeJsonEstInexploitable(int numeroDossier, Response<ResponseBody> response) throws IOException {
        String jsonResponse;
        if (!response.isSuccessful() && response.body() == null) {
            throw new IOException("Erreur API DematSocial : " + (response.errorBody() != null ? response.errorBody().string() : "Réponse vide"));
        }
        jsonResponse = response.body().string();

        if (!isValidJson(jsonResponse)) {
            throw new IOException("La réponse de l'API n'est pas un JSON valide : " + jsonResponse);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        if (rootNode.has("errors") && !rootNode.get("errors").isEmpty()) {
            String errorMessage = rootNode.get("errors").get(0).get("message").asText();
            throw new IOException("Erreur API DematSocial pour le dossier numéro " + numeroDossier + " : " + errorMessage);
        }

        return convertToDossierDeReclamation(jsonResponse);
    }

    private boolean isValidJson(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.readTree(jsonResponse);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private DossierDeReclamation convertToDossierDeReclamation(String jsonResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        var dossierId = rootNode.path("data").path("dossier").path("number").asInt();
        JsonNode champsNode = rootNode.path("data").path("dossier").path("champs");
        LieuDeSurvenue lieuDeSurvenue = null;
        String libelleDuMisEnCause = "";

        // Parcourir la liste des champs
        for (JsonNode champ : champsNode) {
            String id = champ.path("id").asText();
            String stringValue = champ.path("stringValue").asText();


            if ("Q2hhbXAtMTk1MDg=".equals(id)) { //Si etablissement présent
                lieuDeSurvenue = recupererEtablissement(stringValue);
            }
            if ("Q2hhbXAtMTk1MDY=".equals(id)) { //si domicile présent
                lieuDeSurvenue = recupererDomicile(champ);
            }
            if ("Q2hhbXAtMTk1MTY=".equals(id) || "Q2hhbXAtMTk1MTU=".equals(id)) {
                libelleDuMisEnCause = stringValue;
            }
        }
        return new DossierDeReclamation(dossierId, lieuDeSurvenue, libelleDuMisEnCause);
    }

    private LieuDeSurvenue recupererDomicile(JsonNode champ) {
        String adresse = null;
        String codePostal = null;

        // Vérification si "address" est présent et non null
        JsonNode addressNode = champ.path("address");
        if (!addressNode.isMissingNode() && !addressNode.isNull()) {
            adresse = addressNode.path("streetAddress").asText(null);
            codePostal = addressNode.path("postalCode").asText(null);
        }
        // Si "address" est absent ou incomplet, on utilise "stringValue"
        if (adresse == null) {
            adresse = champ.path("stringValue").asText(null); // Fallback vers stringValue
        }
        // Extraction du code postal depuis stringValue si absent
        if (codePostal == null && adresse != null) {
            codePostal = extraireCodePostalDepuisTexte(adresse);
        }
        if (codePostal != null) {
            return new Domicile(Integer.parseInt(codePostal), adresse);
        }
        return new Domicile(null, adresse);
    }

    private String extraireCodePostalDepuisTexte(String adresse) {
        // Regex pour capturer les codes postaux français (5 chiffres)
        Pattern pattern = Pattern.compile("\\b\\d{5}\\b");
        Matcher matcher = pattern.matcher(adresse);

        if (matcher.find()) {
            return matcher.group();
        }

        throw new CodePostalAbsentException("Le code postal n'est pas présent dans l'adresse du lieu de survenue.");
    }

    private Etablissement recupererEtablissement(String stringValue) throws IOException {
        String nom = "";
        String codePostal = "";
        String numeroFiness = "";
        String codeSousCategorie = "";

        // Expression régulière pour extraire le nom (avant la première virgule)
        Pattern nomPattern = Pattern.compile("^([A-Za-zÀ-ÿ\\s'.,-]+),");  // Capture tout avant la virgule
        Matcher nomMatcher = nomPattern.matcher(stringValue);

        if (nomMatcher.find()) {
            nom = nomMatcher.group(1);
        }

        // Expression régulière pour extraire le code postal (5 chiffres avant les parenthèses)
        Pattern codePostalPattern = Pattern.compile("(\\d{5})(?=\\s*\\()");  // 5 chiffres suivis de parenthèses
        Matcher codePostalMatcher = codePostalPattern.matcher(stringValue);

        if (codePostalMatcher.find()) {
            codePostal = codePostalMatcher.group(1);
        }

        // Expression régulière pour extraire le finess et le codeSousCategorie (finess: 9 caractères, puis " - ",puis codeSousCategorie : 3 digits)
        //        \\d : Le premier caractère est obligatoirement un chiffre.
        //        [AB|\\d] : Le deuxième caractère peut être :La lettre A ou B, ou un chiffre.
        //        \\d{7} : 7 chiffres doivent suivre.
        //        (?:\\s*-\\s*(\\d{3}))? : Partie optionnelle après le tiret, contenant 3 chiffres pour le code sous catégorie.
        Pattern pattern = Pattern.compile("\\((\\d[AB|\\d]\\d{7})(?:\\s*-\\s*(\\d{3}))?\\)");
        Matcher matcher = pattern.matcher(stringValue);


        if (matcher.find()) {
            numeroFiness = matcher.group(1);
            if (matcher.group(2) != null) { //Si code sous catégorie présente, on la récupère
                codeSousCategorie = matcher.group(2);
            } else {
                codeSousCategorie = recupererCodeSousCategorieDepuisLApiOpenDataSoft(numeroFiness);
            }
        }
        return new Etablissement(numeroFiness, Integer.parseInt(codeSousCategorie), Integer.parseInt(codePostal), nom);
    }

    private String recupererCodeSousCategorieDepuisLApiOpenDataSoft(String numeroFiness) throws IOException {
        String categetab = "categ_code";
        Call<ResponseBody> call = openDataSoftApi.fetchCodeSousCategorie(
                categetab, // Sélectionne uniquement la colonne "categetab"
                "et_finess:\"" + numeroFiness + "\"", // Condition WHERE
                2 // Limite
        );

        Response<ResponseBody> response = call.execute();

        if (!response.isSuccessful() || response.body() == null) {
            throw new IOException("Erreur lors de l'appel à l'API OpenDataSoft : " +
                    (response.errorBody() != null ? response.errorBody().string() : "Réponse vide"));
        }

        String jsonResponse = response.body().string();

        if (!isValidJson(jsonResponse)) {
            throw new IOException("La réponse de l'API n'est pas un JSON valide : " + jsonResponse);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        // Vérification des résultats
        String results = "results";
        if (rootNode.has(results) && !rootNode.get(results).isEmpty()) {
            JsonNode resultsNode = rootNode.get(results).get(0);
            if (resultsNode.has(categetab)) {
                return resultsNode.get(categetab).asText();
            }
        }

        throw new IOException("Aucun code sous catégorie trouvé dans la réponse de l'API openDataSoft pour le numéro FINESS : " + numeroFiness);
    }
}
