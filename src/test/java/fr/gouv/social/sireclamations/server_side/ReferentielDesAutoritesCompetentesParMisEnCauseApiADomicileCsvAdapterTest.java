package fr.gouv.social.sireclamations.server_side;

import static org.junit.jupiter.api.Assertions.*;

import fr.gouv.social.sireclamations.hexagone.domain.ports.ReferentielDesAutoritesCompetentesParMisEnCauseADomicile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class ReferentielDesAutoritesCompetentesParMisEnCauseApiADomicileCsvAdapterTest {

  private ReferentielDesAutoritesCompetentesParMisEnCauseADomicile
      referentielDesAutoritesCompetentesParMisEnCauseADomicile;

  @BeforeEach
  void setup() {
    Resource csvResource = new ClassPathResource("data/misEnCauseDomicile-test.csv");
    referentielDesAutoritesCompetentesParMisEnCauseADomicile =
        new ReferentielDesAutoritesCompetentesParMisEnCauseADomicileCsvAdapter(csvResource);
  }

  @Test
  void
      lorsqueLonSouhaiteRecupererUneAutoriteCompetentePourUnMisEnCauseADomicileExistant_alorsRetourneLautoriteCompetente() {
    // Given
    var motif = "Membre de la famille";
    // When
    var autoriteCompetenteObtenue =
        referentielDesAutoritesCompetentesParMisEnCauseADomicile.recupererAutoriteCompetente(motif);
    // Then
    assertEquals("CD", autoriteCompetenteObtenue);
  }

  @Test
  void
      lorsqueLonSouhaiteRecupererUneAutoriteCompetentePourUnMisEnCauseADomicileAbsentDuReferentiel_alorsRetourneNull() {
    // Given
    var motif = "libelleAbsentDuReferentiel";
    // When
    var autoriteCompetenteObtenue =
        referentielDesAutoritesCompetentesParMisEnCauseADomicile.recupererAutoriteCompetente(motif);
    // Then
    assertNull(autoriteCompetenteObtenue);
  }
}
