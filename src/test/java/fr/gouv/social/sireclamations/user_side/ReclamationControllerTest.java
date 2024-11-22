package fr.gouv.social.sireclamations.user_side;

import fr.gouv.social.sireclamations.hexagone.domain.DossierDeReclamation;
import fr.gouv.social.sireclamations.hexagone.domain.Etablissement;
import fr.gouv.social.sireclamations.hexagone.domain.Reclamation;
import fr.gouv.social.sireclamations.hexagone.DeposerReclamation;
import fr.gouv.social.sireclamations.server_side.exceptions.AutoriteCompetenteNotFoundException;
import fr.gouv.social.sireclamations.server_side.exceptions.ContactNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReclamationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    DeposerReclamation deposerReclamation;

    @Test
    void lorsqueDeposerReclamationRenvoiAutoriteCompetenteNotFoundException_alorsRenvoiUne404() throws Exception {
        //Given
        var numeroDossier = 12345;
        var dossierRequest = """
                {
                    "numeroDossier": "%s"
                }
                """.formatted(numeroDossier);
        given(deposerReclamation.executer(numeroDossier))
                .willThrow(new AutoriteCompetenteNotFoundException("autorite competente not found"));
        //When Then
        mockMvc.perform(post("/api/v1/reclamations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dossierRequest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("autorite competente not found"));

    }

    @Test
    void lorsqueDeposerReclamationRenvoiContactNotFoundException_alorsRenvoiUne404() throws Exception {
        //Given
        var numeroDossier = 12345;
        var dossierRequest = """
                {
                    "numeroDossier": "%s"
                }
                """.formatted(numeroDossier);
        given(deposerReclamation.executer(numeroDossier))
                .willThrow(new ContactNotFoundException("contact not found"));
        //When Then
        mockMvc.perform(post("/api/v1/reclamations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dossierRequest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("contact not found"));

    }


    @Test
    void lorsqueDeposerReclamationRenvoiBienLaReclamation_alorsRenvoiUne200AvecLesDonneesDeLaReclamationEnBody() throws Exception {
        //Given
        var numeroDossier = 12345;
        var dossierRequest = """
                {
                    "numeroDossier": "%s"
                }
                """.formatted(numeroDossier);
        var etablissement = new Etablissement("78000000", 500, 78210, "nom etablissement");
        var dossierDeReclamation = new DossierDeReclamation(numeroDossier, etablissement);
        given(deposerReclamation.executer(numeroDossier))
                .willReturn(new Reclamation(
                        dossierDeReclamation,
                        List.of("ARS"),
                        List.of("email@email.fr"))
                );
        //When Then
        mockMvc.perform(post("/api/v1/reclamations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dossierRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroDossier").value(12345))
                .andExpect(jsonPath("$.codeSousCategorieEtablissement").value(500))
                .andExpect(jsonPath("$.autoritesCompetentes[0]").value("ARS"))
                .andExpect(jsonPath("$.contacts[0]").value("email@email.fr"));

    }
}