package fr.gouv.social.sireclamations.user_side;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReclamationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    @Tag("localOnly")
    void lorsqueLonDeposeUneReclamationPourUnDossierExistant_alorsRetourne200EtLaReclamationEnBody() throws Exception {
        // Given
        int numeroDossier = 186287; //Correspond a un dossier existant avec un Ehpad pour établissement
        DeposerReclamationRequest request = new DeposerReclamationRequest();
        request.setNumeroDossier(numeroDossier);

        // When Then
        mockMvc.perform(post("/api/v1/reclamations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroDossier", is(numeroDossier)))
                .andExpect(jsonPath("$.autoritesCompetentes", hasSize(2)))
                .andExpect(jsonPath("$.autoritesCompetentes", containsInAnyOrder("CD", "ARS")))
                .andExpect(jsonPath("$.contacts", hasSize(4)));
    }
    @Test
    void lorsqueLonDeposeUneReclamationPourUnDossierInexistant_alorsRetourne404() throws Exception {
        // Given
        int numeroDossier = 1111111111;
        DeposerReclamationRequest request = new DeposerReclamationRequest();
        request.setNumeroDossier(numeroDossier);

        // When Then
        mockMvc.perform(post("/api/v1/reclamations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Erreur API DematSocial pour le dossier numéro 1111111111 : Dossier not found"));
    }


}