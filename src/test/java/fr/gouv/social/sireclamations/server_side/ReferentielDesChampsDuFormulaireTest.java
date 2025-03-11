package fr.gouv.social.sireclamations.server_side;

import static org.assertj.core.api.Assertions.assertThat;

import fr.gouv.social.sireclamations.hexagone.domain.ChampsArbreDeDecision;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

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
    var mapDesChampsDeLarbreDeDecisionObtenu =
        referentielDesChampsDuFormulaire.getChampsPourArbreDeDecision();
    // Then
    Map<ChampsArbreDeDecision, String> expectedChampsArbreDeDecision =
        Map.ofEntries(
            Map.entry(ChampsArbreDeDecision.TYPE_DE_LIEU, "Q2hhbXAtMTk1MDU="),
            Map.entry(ChampsArbreDeDecision.LIEU_ETAB, "Q2hhbXAtMTk1MDg="),
            Map.entry(ChampsArbreDeDecision.LIEU_DOM, "Q2hhbXAtMjcxNjE="),
            Map.entry(ChampsArbreDeDecision.TYPE_DE_MEC_ETAB, "Q2hhbXAtMTk1MTU="),
            Map.entry(ChampsArbreDeDecision.MALTRAITANCE, "Q2hhbXAtMjcxNTU="),
            Map.entry(ChampsArbreDeDecision.MOTIF, "Q2hhbXAtMTk1MjY="),
            Map.entry(ChampsArbreDeDecision.SERVICE, "Q2hhbXAtMjcxNjg="),
            Map.entry(ChampsArbreDeDecision.PERS_RESP_ETAB, "Q2hhbXAtMjgzNjg="),
            Map.entry(ChampsArbreDeDecision.PERS_RESP_DOM, "Q2hhbXAtMjg3ODE="),
            Map.entry(ChampsArbreDeDecision.CODE_POSTAL, "Q2hhbXAtMjgzNjc="),
            Map.entry(ChampsArbreDeDecision.VILLE, "Q2hhbXAtMjgzNjk="));

    assertThat(mapDesChampsDeLarbreDeDecisionObtenu).hasSize(expectedChampsArbreDeDecision.size());
    assertThat(mapDesChampsDeLarbreDeDecisionObtenu)
        .containsExactlyInAnyOrderEntriesOf(expectedChampsArbreDeDecision);
  }
}
