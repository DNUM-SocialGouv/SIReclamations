package fr.gouv.social.sireclamations.user_side;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ReclamationControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  @Tag("localOnly")
  void lorsqueLonDeposeUneReclamationPourUnDossierExistant_alorsRetourne200EtLaReclamationEnBody()
      throws Exception {
    // Given
    int numeroDemarche = 1;
    int numeroDossier = 211523;
    String etat = "en_construction";
    String dateDepot = "2025-03-07 19:39:42 +0100";
    // When Then
    mockMvc
        .perform(
            post("/api/v1/reclamation/demat-social")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("procedure_id", String.valueOf(numeroDemarche))
                .param("dossier_id", String.valueOf(numeroDossier))
                .param("state", etat)
                .param("updated_at", dateDepot))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.output.numeroDossier", is(numeroDossier)))
        .andExpect(jsonPath("$.output.autoritesCompetentes", hasSize(2)))
        .andExpect(jsonPath("$.output.autoritesCompetentes", containsInAnyOrder("ARS")));
  }

  @Test
  void
      lorsqueLonDeposeUneReclamationViaLaPlateformeTelephoniquePourUnDossierADomicile_alorsRetourne200EtARS()
          throws Exception {
    // Given
    String json =
        """
        {
           "id": "1234",
           "lieuSurvenue": {
             "codePostal": "75010",
             "commune": "Paris",
             "natureLieu": "Au domicile (domicile de la victime, domicile d'un membre de la famille, domicile d'un aidant...)",
             "domicile": {
               "adresse": "31 Avenue Pierre Curie",
               "serviceADomicile": "Service de Soins Infirmier à Domicile (SSIAD)"
             }
           },
           "misEnCause": {
             "typeDeMisEnCause": "Professionnel",
             "civilite": "M.",
             "nom": "string",
             "prenom": "string",
             "profession": "string"
           },
           "description": {
             "maltraitance": true,
             "typeDeMaltraitance": [
               "Maltraitance physique (châtiments corporels, agressions physiques, intervention médicale sans consentement éclairé, enfermement...)"
             ],
             "typesDeFaits": [
               "Problème comportemental, relationnel ou de communication avec une personne"
             ],
             "dateSurvenue": "2019-08-24",
             "consequenceSurLaVictime": [
               "Sur la santé physique et/ou psychique (blessures, troubles de la santé ou mentaux...)"
             ],
             "situationToujoursActuelle": "Oui",
             "dateDeFin": "2019-08-24",
             "description": "string"
           }
         }

        """;
    // When Then
    mockMvc
        .perform(
            post("/api/v1/reclamation/plateforme-telephonique")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.numeroDossier", is(1234)))
        .andExpect(jsonPath("$.lieuDeSurvenue.codeTypeDeLieu", is("DOM")))
        .andExpect(jsonPath("$.autoritesCompetentes", hasSize(1)))
        .andExpect(jsonPath("$.autoritesCompetentes", containsInAnyOrder("ARS")));
  }

  @Test
  void
      lorsqueLonDeposeUneReclamationViaLaPlateformeTelephoniquePourUnDossierEnEtablissementDeSanteAvecMaltraitanceParUnProfessionnelDeSante_alorsRetourne200EtARS()
          throws Exception {
    // Given
    String json =
        """
        {
           "id": "1234",
           "lieuSurvenue": {
             "codePostal": "75010",
             "commune": "Paris",
             "natureLieu": "Dans un établissement de santé (hôpital, clinique, laboratoire, pharmacie ...)",
             "etablissementSanitaireEtSocial": {
               "et_finess": "1234567",
               "codeCategorieEtablissement": "340",
               "nomEtablissement": "HOPITAL ST-LAZARE",
               "typeDeMisEnCause": "Un professionnel de santé (médecin, infirmier, aide-soignant, kiné, ostéopathe...)"
             }
           },
           "misEnCause": {
             "typeDeMisEnCause": "Professionnel",
             "rpps": "string",
             "civilite": "M.",
             "nom": "string",
             "prenom": "string",
             "profession": "string"
           },
           "description": {
             "maltraitance": true,
             "typeDeMaltraitance": [
               "Maltraitance physique (châtiments corporels, agressions physiques, intervention médicale sans consentement éclairé, enfermement...)"
             ],
             "typesDeFaits": [
               "Problème comportemental, relationnel ou de communication avec une personne"
             ],
             "dateSurvenue": "2019-08-24",
             "consequenceSurLaVictime": [
               "Sur la santé physique et/ou psychique (blessures, troubles de la santé ou mentaux...)"
             ],
             "situationToujoursActuelle": "Oui",
             "dateDeFin": "2019-08-24",
             "description": "string"
           }
         }

        """;
    // When Then
    mockMvc
        .perform(
            post("/api/v1/reclamation/plateforme-telephonique")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.numeroDossier", is(1234)))
        .andExpect(jsonPath("$.lieuDeSurvenue.codeTypeDeLieu", is("ETAB")))
        .andExpect(jsonPath("$.lieuDeSurvenue.numeroFiness", is("1234567")))
        .andExpect(jsonPath("$.lieuDeSurvenue.codeSousCategorie", is(340)))
        .andExpect(jsonPath("$.autoritesCompetentes", hasSize(1)))
        .andExpect(jsonPath("$.autoritesCompetentes", containsInAnyOrder("ARS")));
  }

  @Test
  void
      lorsqueLonDeposeUneReclamationViaLaPlateformeTelephoniquePourUnDossierEnEtablissementDeSanteAvecMaltraitanceParUnMembreDeLaFamille_alorsRetourne200EtARSetDDETS()
          throws Exception {
    // Given
    String json =
        """
        {
           "id": "1234",
           "lieuSurvenue": {
             "codePostal": "75010",
             "commune": "Paris",
             "natureLieu": "Dans un établissement de santé (hôpital, clinique, laboratoire, pharmacie ...)",
             "etablissementSanitaireEtSocial": {
               "et_finess": "1234567",
               "codeCategorieEtablissement": "340",
               "nomEtablissement": "HOPITAL ST-LAZARE"
             }
           },
           "misEnCause": {
             "typeDeMisEnCause": "Membre de la famille",
             "rpps": "string",
             "civilite": "M.",
             "nom": "string",
             "prenom": "string",
             "profession": "string"
           },
           "description": {
             "maltraitance": true,
             "typeDeMaltraitance": [
               "Maltraitance physique (châtiments corporels, agressions physiques, intervention médicale sans consentement éclairé, enfermement...)"
             ],
             "typesDeFaits": [
               "Problème comportemental, relationnel ou de communication avec une personne"
             ],
             "dateSurvenue": "2019-08-24",
             "consequenceSurLaVictime": [
               "Sur la santé physique et/ou psychique (blessures, troubles de la santé ou mentaux...)"
             ],
             "situationToujoursActuelle": "Oui",
             "dateDeFin": "2019-08-24",
             "description": "string"
           }
         }

        """;
    // When Then
    mockMvc
        .perform(
            post("/api/v1/reclamation/plateforme-telephonique")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.numeroDossier", is(1234)))
        .andExpect(jsonPath("$.lieuDeSurvenue.codeTypeDeLieu", is("ETAB")))
        .andExpect(jsonPath("$.lieuDeSurvenue.numeroFiness", is("1234567")))
        .andExpect(jsonPath("$.lieuDeSurvenue.codeSousCategorie", is(340)))
        .andExpect(jsonPath("$.autoritesCompetentes", hasSize(2)))
        .andExpect(jsonPath("$.autoritesCompetentes", containsInAnyOrder("ARS", "CD")));
  }

  @Test
  void lorsqueLonDeposeUneReclamationPourUnDossierInexistant_alorsRetourne404() throws Exception {
    // Given
    int numeroDemarche = 1;
    int numeroDossier = 1111111111;
    String etat = "en_construction";
    String dateDepot = "2025-03-07 19:39:42 +0100";

    // When Then
    mockMvc
        .perform(
            post("/api/v1/reclamation/demat-social")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("procedure_id", String.valueOf(numeroDemarche))
                .param("dossier_id", String.valueOf(numeroDossier))
                .param("state", etat)
                .param("updated_at", dateDepot))
        .andExpect(status().isNotFound())
        .andExpect(
            jsonPath("$.message")
                .value(
                    "Erreur API DematSocial pour le dossier numéro 1111111111 : Dossier not found"))
        .andExpect(jsonPath("$.status").value("404"));
  }
}
