package fr.gouv.social.sireclamations.server_side;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.gouv.social.sireclamations.hexagone.domain.*;
import fr.gouv.social.sireclamations.hexagone.domain.ports.DematSocial;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

@Component
public class DematSocialAdapter implements DematSocial {
  private final DematSocialApi dematSocialApi;

  private final OpenDataSoftApi openDataSoftApi;

  private final ReferentielDuTypeDeLieux referentielDuTypeDeLieux;

  private static final String STRING_ERRORS = "errors";

  private static final String STRING_VALUE = "stringValue";
  private final ReferentielDesChampsDuFormulaire referentielDesChampsDuFormulaire;

  public DematSocialAdapter(
      Retrofit dematSocialRetrofit,
      OpenDataSoftApi openDataSoftApi,
      ReferentielDuTypeDeLieux referentielDuTypeDeLieux,
      ReferentielDesChampsDuFormulaire referentielDesChampsDuFormulaire) {
    this.dematSocialApi = dematSocialRetrofit.create(DematSocialApi.class);
    this.openDataSoftApi = openDataSoftApi;
    this.referentielDuTypeDeLieux = referentielDuTypeDeLieux;
    this.referentielDesChampsDuFormulaire = referentielDesChampsDuFormulaire;
  }

  @Override
  public DossierDeReclamation recupererDossier(int numeroDossier) throws IOException {
    GraphQLRequest request = new GraphQLRequest();
    request.setQueryId("ds-query-v2");
    request.setVariables(
        Map.of(
            "dossierNumber", numeroDossier,
            "includeGeometry", true,
            "includeInstructeurs", false));
    request.setOperationName("getDossier");

    Call<ResponseBody> call = dematSocialApi.executeGraphQLQueryRaw(request);
    Response<ResponseBody> response = call.execute();

    if (!response.isSuccessful() || response.body() == null) {
      throw new IOException(
          "Erreur API DematSocial : "
              + (response.errorBody() != null ? response.errorBody().string() : "Réponse vide"));
    }

    String jsonResponse = response.body().string();
    throwErreurSiLeJsonEstInexploitable(numeroDossier, jsonResponse);
    return convertToDossierDeReclamation(jsonResponse);
  }

  private void throwErreurSiLeJsonEstInexploitable(int numeroDossier, String jsonResponse)
      throws IOException {
    if (!isValidJson(jsonResponse)) {
      throw new IOException("La réponse de l'API n'est pas un JSON valide : " + jsonResponse);
    }

    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode rootNode = objectMapper.readTree(jsonResponse);
    if (rootNode.has(STRING_ERRORS) && !rootNode.get(STRING_ERRORS).isEmpty()) {
      String errorMessage = rootNode.get(STRING_ERRORS).get(0).get("message").asText();
      throw new IOException(
          "Erreur API DematSocial pour le dossier numéro " + numeroDossier + " : " + errorMessage);
    }
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

  private DossierDeReclamation convertToDossierDeReclamation(String jsonResponse)
      throws IOException {
    var objectMapper = new ObjectMapper();
    var rootNode = objectMapper.readTree(jsonResponse);
    var dossierId = rootNode.path("data").path("dossier").path("number").asInt();
    var champsDuDossierJson = rootNode.path("data").path("dossier").path("champs");
    var mapDesChampsDuDossier = extraireChampsDuDossier(champsDuDossierJson);
    var champsPourArbreDeDecision = referentielDesChampsDuFormulaire.getChampsPourArbreDeDecision();

    var maltraitance =
        recupererMaltraitance(
            mapDesChampsDuDossier,
            champsPourArbreDeDecision.get(ChampsArbreDeDecision.MALTRAITANCE));
    var codeTypeDeLieu =
        recupererCodeTypeDeLieu(
            mapDesChampsDuDossier,
            champsPourArbreDeDecision.get(ChampsArbreDeDecision.TYPE_DE_LIEU));
    var lieuDeSurvenue =
        recupererLieuDeSurvenue(codeTypeDeLieu, mapDesChampsDuDossier, champsPourArbreDeDecision);
    var libelleMisEnCause =
        recupererLibelleMisEnCause(mapDesChampsDuDossier, champsPourArbreDeDecision);
    var motifs =
        recupererMotifs(
            mapDesChampsDuDossier, champsPourArbreDeDecision.get(ChampsArbreDeDecision.MOTIF));

    return new DossierDeReclamation(
        dossierId, lieuDeSurvenue, libelleMisEnCause, maltraitance, motifs);
  }

  private List<String> recupererMotifs(
      Map<String, JsonNode> mapDesChampsDuDossier, String idChampMotifs) {
    if (mapDesChampsDuDossier.containsKey(idChampMotifs)) {
      JsonNode valuesNode = mapDesChampsDuDossier.get(idChampMotifs).path("values");

      if (valuesNode.isArray()) {
        List<String> valeursMotifs = new ArrayList<>();
        for (JsonNode node : valuesNode) {
          valeursMotifs.add(node.asText());
        }
        return valeursMotifs;
      }
    }
    return Collections.emptyList();
  }

  private Map<String, JsonNode> extraireChampsDuDossier(JsonNode champsNode) {
    Map<String, JsonNode> map = new HashMap<>();
    champsNode.forEach(champ -> map.put(champ.path("id").asText(), champ));
    return map;
  }

  private boolean recupererMaltraitance(Map<String, JsonNode> champsMap, String idChampTypeDeLieu) {
    if (champsMap.containsKey(idChampTypeDeLieu)) {
      String maltraitanceValue = champsMap.get(idChampTypeDeLieu).path(STRING_VALUE).asText();
      if (maltraitanceValue.equals("Oui")) {
        return true;
      } else if (maltraitanceValue.equals("Non")) {
        return false;
      }
    }
    return false;
  }

  private CodeTypeDeLieu recupererCodeTypeDeLieu(
      Map<String, JsonNode> champsMap, String idChampTypeDeLieu) {
    if (champsMap.containsKey(idChampTypeDeLieu)) {
      String stringValue = champsMap.get(idChampTypeDeLieu).path(STRING_VALUE).asText();
      return referentielDuTypeDeLieux.recupererCodeTypeDeLieuxAPartirDuLibelle(stringValue);
    }
    return null;
  }

  private String recupererLibelleMisEnCause(
      Map<String, JsonNode> champsMap, Map<ChampsArbreDeDecision, String> champsPourArbre) {
    String idChampLibellePersonneResponsableEnEtablissement =
        champsPourArbre.get(ChampsArbreDeDecision.PERS_RESP_ETAB);
    String idChampLibellePersonneResponsableADomicile =
        champsPourArbre.get(ChampsArbreDeDecision.PERS_RESP_DOM);

    String idChampLibellePersonneResponsable =
        champsMap.containsKey(idChampLibellePersonneResponsableEnEtablissement)
            ? idChampLibellePersonneResponsableEnEtablissement
            : idChampLibellePersonneResponsableADomicile;

    if (champsMap.containsKey(idChampLibellePersonneResponsable)) {
      var persResp = champsMap.get(idChampLibellePersonneResponsable).path(STRING_VALUE).asText();
      if (persResp.contains("Professionnel")) {
        String idChampServiceADomicile = champsPourArbre.get(ChampsArbreDeDecision.SERVICE);
        String idChampMisEnCauseEtablissement =
            champsPourArbre.get(ChampsArbreDeDecision.TYPE_DE_MEC_ETAB);
        if (champsMap.get(idChampServiceADomicile) != null)
          return champsMap.get(idChampServiceADomicile).path(STRING_VALUE).asText();
        if (champsMap.get(idChampMisEnCauseEtablissement) != null)
          return champsMap.get(idChampMisEnCauseEtablissement).path(STRING_VALUE).asText();
      }
      return persResp;
    }

    return "";
  }

  private LieuDeSurvenue recupererLieuDeSurvenue(
      CodeTypeDeLieu codeTypeDeLieu,
      Map<String, JsonNode> mapDesChampsDuDossier,
      Map<ChampsArbreDeDecision, String> champsPourArbre)
      throws IOException {
    String idChampTypeDeLieu = champsPourArbre.get(ChampsArbreDeDecision.TYPE_DE_LIEU);
    String libelleTypeDeLieu =
        mapDesChampsDuDossier.get(idChampTypeDeLieu).path(STRING_VALUE).asText();
    String idChampCodePostal = champsPourArbre.get(ChampsArbreDeDecision.CODE_POSTAL);
    String libelleCodePostal =
        mapDesChampsDuDossier.get(idChampCodePostal).path(STRING_VALUE).asText();

    if (CodeTypeDeLieu.ETAB.equals(codeTypeDeLieu)) {
      String idChampLieuEtablissement = champsPourArbre.get(ChampsArbreDeDecision.LIEU_ETAB);
      String nomEtablissementVilleCodePostalEtFiness =
          mapDesChampsDuDossier.get(idChampLieuEtablissement).path(STRING_VALUE).asText();
      return recupererEtablissement(nomEtablissementVilleCodePostalEtFiness, libelleTypeDeLieu);
    }

    if (CodeTypeDeLieu.DOM.equals(codeTypeDeLieu)) {
      String idChampLieuDomicile =
          champsPourArbre.get(
              ChampsArbreDeDecision
                  .LIEU_DOM); // Adresse complète avec auto completion adresse cp et ville
      String idChampServiceADomicile = champsPourArbre.get(ChampsArbreDeDecision.SERVICE);
      JsonNode domicileChamp = mapDesChampsDuDossier.get(idChampLieuDomicile);
      String libelleService =
          Optional.ofNullable(mapDesChampsDuDossier.get(idChampServiceADomicile))
              .map(node -> node.path(STRING_VALUE).asText())
              .orElse(null);
      return recupererDomicile(domicileChamp, libelleTypeDeLieu, libelleService, libelleCodePostal);
    }

    return null;
  }

  private LieuDeSurvenue recupererDomicile(
      JsonNode champCompletDuDomicile,
      String libelleTypeDeLieu,
      String libelleService,
      String libelleCodePostal) {

    String adresse = null;
    String codePostal = null;

    // Récupération de l'objet "address" si présent
    JsonNode addressNode = champCompletDuDomicile.path("address");
    if (!addressNode.isMissingNode() && !addressNode.isNull()) {
      adresse = addressNode.path("streetAddress").asText(null);
      codePostal = addressNode.path("postalCode").asText(null);

      // Si l'adresse est composée de plusieurs parties, on essaie de les reconstruire
      if (adresse == null || adresse.isEmpty()) {
        String streetNumber = addressNode.path("streetNumber").asText(null);
        String streetName = addressNode.path("streetName").asText(null);
        String cityName = addressNode.path("cityName").asText(null);
        String postalCode = addressNode.path("postalCode").asText(null);

        if (streetNumber != null && streetName != null && cityName != null && postalCode != null) {
          adresse = String.format("%s %s %s %s", streetNumber, streetName, postalCode, cityName);
          codePostal = postalCode;
        }
      }
    }

    // Utiliser "stringValue" si l'adresse est absente ou vide
    if (adresse == null && champCompletDuDomicile.has("stringValue")) {
      String stringValue = champCompletDuDomicile.path("stringValue").asText(null);
      if (stringValue != null && !stringValue.isEmpty()) {
        adresse = stringValue;
      }
    }

    // Extraire le code postal depuis "libelleCodePostal" s'il est fourni
    if (codePostal == null && libelleCodePostal != null) {
      Pattern pattern = Pattern.compile(".*\\((\\d{5})\\).*");
      Matcher matcher = pattern.matcher(libelleCodePostal);
      if (matcher.matches()) {
        codePostal = matcher.group(1);
      }
    }

    // Extraction du code postal depuis l'adresse s'il n'a pas encore été trouvé
    if (codePostal == null && adresse != null) {
      codePostal = extraireCodePostalDepuisTexte(adresse);
    }

    // Conversion du code postal en entier si possible
    Integer codePostalInt = null;
    try {
      if (codePostal != null) {
        codePostalInt = Integer.parseInt(codePostal);
      }
    } catch (NumberFormatException e) {
      // Si le code postal est mal formé, on le laisse null
    }

    // Création de l'objet Domicile
    return new Domicile(codePostalInt, adresse, libelleTypeDeLieu, libelleService);
  }

  private String extraireCodePostalDepuisTexte(String adresse) {
    // Regex pour capturer les codes postaux français (5 chiffres)
    Pattern pattern = Pattern.compile("\\b\\d{5}\\b");
    Matcher matcher = pattern.matcher(adresse);

    if (matcher.find()) {
      return matcher.group();
    }
    return null;
  }

  private Etablissement recupererEtablissement(
      String nomEtablissementVilleCodePostalEtFiness, String libelleTypeDeLieu) throws IOException {
    String nom = "";
    String codePostal = "";
    String numeroFiness = "";
    String codeSousCategorie = "";

    // Expression régulière pour extraire le nom (avant la première virgule)
    Pattern nomPattern =
        Pattern.compile("^([A-Za-zÀ-ÿ\\s'.,-]+),"); // Capture tout avant la virgule
    Matcher nomMatcher = nomPattern.matcher(nomEtablissementVilleCodePostalEtFiness);

    if (nomMatcher.find()) {
      nom = nomMatcher.group(1);
    }

    // Expression régulière pour extraire le code postal (5 chiffres avant les parenthèses)
    Pattern codePostalPattern =
        Pattern.compile("(\\d{5})(?=\\s*\\()"); // 5 chiffres suivis de parenthèses
    Matcher codePostalMatcher = codePostalPattern.matcher(nomEtablissementVilleCodePostalEtFiness);

    if (codePostalMatcher.find()) {
      codePostal = codePostalMatcher.group(1);
    }

    // Expression régulière pour extraire le finess et le codeSousCategorie (finess: 9 caractères,
    // puis " - ",puis codeSousCategorie : 3 digits)
    //        \\d : Le premier caractère est obligatoirement un chiffre.
    //        [AB|\\d] : Le deuxième caractère peut être :La lettre A ou B, ou un chiffre.
    //        \\d{7} : 7 chiffres doivent suivre.
    //        (?:\\s*-\\s*(\\d{3}))? : Partie optionnelle après le tiret, contenant 3 chiffres pour
    // le code sous catégorie.
    Pattern pattern = Pattern.compile("\\((\\d[AB|\\d]\\d{7})(?:\\s*-\\s*(\\d{3}))?\\)");
    Matcher matcher = pattern.matcher(nomEtablissementVilleCodePostalEtFiness);

    if (matcher.find()) {
      numeroFiness = matcher.group(1);
      if (matcher.group(2) != null) { // Si code sous catégorie présente, on la récupère
        codeSousCategorie = matcher.group(2);
      } else {
        codeSousCategorie = recupererCodeSousCategorieDepuisLApiOpenDataSoft(numeroFiness);
      }
    }
    return new Etablissement(
        numeroFiness,
        Integer.parseInt(codeSousCategorie),
        Integer.parseInt(codePostal),
        nom,
        libelleTypeDeLieu);
  }

  private String recupererCodeSousCategorieDepuisLApiOpenDataSoft(String numeroFiness)
      throws IOException {
    String codeCategorie = "categ_code";
    Call<ResponseBody> call =
        openDataSoftApi.fetchCodeSousCategorie(
            codeCategorie, // Sélectionne uniquement la colonne "categ_code"
            String.format("et_finess:\"%s\"", numeroFiness), // Condition WHERE
            2 // Limite
            );

    Response<ResponseBody> response = call.execute();

    if (!response.isSuccessful() || response.body() == null) {
      throw new IOException(
          "Erreur lors de l'appel à l'API OpenDataSoft : "
              + (response.errorBody() != null ? response.errorBody().string() : "Réponse vide"));
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
      if (resultsNode.has(codeCategorie)) {
        return resultsNode.get(codeCategorie).asText();
      }
    }
    throw new IOException(
        "Aucun code sous catégorie trouvé dans la réponse de l'API openDataSoft pour le numéro FINESS : "
            + numeroFiness);
  }
}
