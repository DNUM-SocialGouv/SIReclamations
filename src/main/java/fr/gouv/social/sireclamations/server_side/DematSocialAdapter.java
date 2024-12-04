package fr.gouv.social.sireclamations.server_side;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.gouv.social.sireclamations.hexagone.domain.DossierDeReclamation;
import fr.gouv.social.sireclamations.hexagone.domain.Etablissement;
import fr.gouv.social.sireclamations.hexagone.domain.port.DematSocial;
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

    public DematSocialAdapter(Retrofit dematSocialRetrofit) {
        this.dematSocialApi = dematSocialRetrofit.create(DematSocialApi.class);
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
        Etablissement etablissement = null;
        String libelleDuMisEnCause = "";

        // Parcourir la liste des champs
        for (JsonNode champ : champsNode) {
            String id = champ.path("id").asText();
            String stringValue = champ.path("stringValue").asText();

            if ("Q2hhbXAtMTk1MDg=".equals(id)) {
                etablissement = recupererEtablissement(stringValue);
            }
            if ("Q2hhbXAtMTk1MTY=".equals(id)) {
                libelleDuMisEnCause = stringValue;
            }
        }
        return new DossierDeReclamation(dossierId, etablissement, libelleDuMisEnCause);
    }

    private Etablissement recupererEtablissement(String stringValue) {
        String nom = "";
        String codePostal = "";
        String numeroFiness = "";
        String codeSousCategorie = "500"; // Valeur par défaut pour codeSousCategorie si non trouvé

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
            }
        }
        return new Etablissement(numeroFiness, Integer.parseInt(codeSousCategorie), Integer.parseInt(codePostal), nom);
    }
}
