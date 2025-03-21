package fr.gouv.social.sireclamations.user_side;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import fr.gouv.social.sireclamations.hexagone.AffecterReclamation;
import fr.gouv.social.sireclamations.hexagone.domain.AutoriteCompetente;
import fr.gouv.social.sireclamations.hexagone.domain.DossierDeReclamation;
import fr.gouv.social.sireclamations.hexagone.domain.Etablissement;
import fr.gouv.social.sireclamations.hexagone.domain.Reclamation;
import fr.gouv.social.sireclamations.hexagone.exceptions.AutoriteCompetenteNotFoundException;
import fr.gouv.social.sireclamations.hexagone.exceptions.DematSocialException;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ReclamationControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private AffecterReclamation affecterReclamation;

  @TestConfiguration
  static class TestConfig {
    @Bean
    public AffecterReclamation affecterReclamation() {
      return Mockito.mock(AffecterReclamation.class);
    }
  }

  @BeforeEach
  void resetMocks() {
    Mockito.reset(affecterReclamation);
  }

  @Test
  void lorsqueDeposerReclamationRenvoiAutoriteCompetenteNotFoundException_alorsRenvoiUne404()
      throws Exception {
    // Given
    int numeroDemarche = 1;
    int numeroDossier = 12345;
    String etat = "en_construction";
    String dateDepot = "2025-03-07 19:39:42 +0100";

    given(affecterReclamation.executer(numeroDossier))
        .willThrow(new AutoriteCompetenteNotFoundException("autorite competente not found"));
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
        .andExpect(jsonPath("$.message").value("autorite competente not found"));
  }

  @Test
  void lorsqueDeposerReclamationRenvoiDematSocialException_alorsRenvoiUne404() throws Exception {
    // Given
    int numeroDemarche = 1;
    int numeroDossier = 12345;
    String etat = "en_construction";
    String dateDepot = "2025-03-07 19:39:42 +0100";

    given(affecterReclamation.executer(numeroDossier))
        .willThrow(new DematSocialException("dossier not found"));
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
        .andExpect(jsonPath("$.message").value("dossier not found"));
  }

  @Test
  void
      lorsqueDeposerReclamationRenvoiBienLaReclamation_alorsRenvoiUne200AvecLesDonneesDeLaReclamationEnBody()
          throws Exception {
    // Given
    int numeroDemarche = 1;
    int numeroDossier = 12345;
    String etat = "en_construction";
    String dateDepot = "2025-03-07 19:39:42 +0100";

    var etablissement =
        new Etablissement("78000000", 500, 78210, "nom etablissement", "typeDeLieu");
    String libelleDuMisEnCause =
        "Un professionnel de santé (médecin, infirmier, aide-soignant, kiné, ostéopathe...)";
    var motifs = List.of("Problème lié aux locaux ou la restauration");
    var dossierDeReclamation =
        new DossierDeReclamation(numeroDossier, etablissement, libelleDuMisEnCause, true, motifs);
    given(affecterReclamation.executer(numeroDossier))
        .willReturn(
            new Reclamation(dossierDeReclamation, Set.of(AutoriteCompetente.ARS), etablissement));
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
        .andExpect(jsonPath("$.output.numeroDossier").value(12345))
        .andExpect(jsonPath("$.output.autoritesCompetentes[0]").value("ARS"))
        .andExpect(jsonPath("$.output.lieuDeSurvenue.numeroFiness").value("78000000"));
  }

  @Test
  void
      lorsqueLaPlateformeTelephoniqueEnvoiUnDossierDeReclamationComplet_alorsRenvoiUne200AvecLesDonneesDeLaReclamationEnBody()
          throws Exception {

    String jsonPayload =
        """
            {
               "id": "12345",
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
                 "typeDeMisEnCauseProfessionnel": "Professionnel",
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

    mockMvc
        .perform(
            post("/api/v1/reclamation/plateforme-telephonique")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.numeroDossier").value(12345))
        .andExpect(jsonPath("$.autoritesCompetentes", hasSize(1)))
        .andExpect(jsonPath("$.lieuDeSurvenue.codeTypeDeLieu").value("DOM"))
        .andExpect(jsonPath("$.autoritesCompetentes[0]").value("ARS"));
  }
}
