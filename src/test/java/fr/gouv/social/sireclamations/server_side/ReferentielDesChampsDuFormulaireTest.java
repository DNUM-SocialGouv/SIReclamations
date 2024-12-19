package fr.gouv.social.sireclamations.server_side;

import fr.gouv.social.sireclamations.hexagone.domain.ChampsArbreDeDecision;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ReferentielDesChampsDuFormulaireTest {

    private ReferentielDesChampsDuFormulaire referentielDesChampsDuFormulaire;

    @BeforeEach
    void setup() {
        Resource csvResource = new ClassPathResource("data/mappingFormulaireV2-test.csv");
        referentielDesChampsDuFormulaire = new ReferentielDesChampsDuFormulaire(csvResource);
    }

    @Test
    void recupereTousLesChampsNecessaireAlArbreDeDecision() {
        // When
        var mapDesChampsDeLarbreDeDecisionObtenu = referentielDesChampsDuFormulaire.getChampsPourArbreDeDecision();
        // Then
        Map<ChampsArbreDeDecision, String> expectedChampsArbreDeDecision = Map.ofEntries(
                Map.entry(ChampsArbreDeDecision.TYPE_DE_LIEU, "Q2hhbXAtMTk1MDU="),
                Map.entry(ChampsArbreDeDecision.LIEU_ETAB, "Q2hhbXAtMTk1MDg="),
                Map.entry(ChampsArbreDeDecision.LIEU_DOM, "Q2hhbXAtMTk1MDY="),
                Map.entry(ChampsArbreDeDecision.TYPE_DE_MEC_ETAB, "Q2hhbXAtMTk1MTY="),
                Map.entry(ChampsArbreDeDecision.TYPE_DE_MEC_DOM, "Q2hhbXAtMTk1MTU=")
        );

        assertThat(mapDesChampsDeLarbreDeDecisionObtenu).hasSize(5);
        assertThat(mapDesChampsDeLarbreDeDecisionObtenu).usingRecursiveComparison().isEqualTo(expectedChampsArbreDeDecision);

    }
}