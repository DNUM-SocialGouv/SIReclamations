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
    int numeroDossier = 209940; // Correspond a un dossier existant avec un Ehpad pour établissement
    String etat = "en_construction";
    String dateDepot = "2025-03-07 19:39:42 +0100";
    // When Then
    mockMvc
        .perform(
            post("/api/v1/reclamations")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("procedure_id", String.valueOf(numeroDemarche))
                .param("dossier_id", String.valueOf(numeroDossier))
                .param("state", etat)
                .param("updated_at", dateDepot))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.numeroDossier", is(numeroDossier)))
        .andExpect(jsonPath("$.autoritesCompetentes", hasSize(2)))
        .andExpect(jsonPath("$.autoritesCompetentes", containsInAnyOrder("CD", "ARS")))
        .andExpect(jsonPath("$.contacts", hasSize(4)));
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
            post("/api/v1/reclamations")
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
