package fr.gouv.social.sireclamations.server_side;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class ReferentielDesAutoritesCompetentesParMisEnCauseEnEtablissementCsvAdapterTest {

  private ReferentielDesAutoritesCompetentesParMisEnCauseEnEtablissementCsvAdapter
      referentielDesAutoritesCompetentesParMisEnCausePourMaltraitance;

  @BeforeEach
  void setup() {
    Resource csvResource = new ClassPathResource("data/misEnCauseEtablissement-test.csv");
    referentielDesAutoritesCompetentesParMisEnCausePourMaltraitance =
        new ReferentielDesAutoritesCompetentesParMisEnCauseEnEtablissementCsvAdapter(csvResource);
  }

  @Test
  void
      lorsqueLonSouhaiteRecupererUneAutoriteCompetentePourUnMisEnCauseADomicileExistant_alorsRetourneLautoriteCompetente() {
    // Given
    var libelle = "Membre de la famille";
    // When
    var autoriteCompetenteObtenue =
        referentielDesAutoritesCompetentesParMisEnCausePourMaltraitance.recupererAutoriteCompetente(
            libelle);
    // Then
    assertEquals("CD", autoriteCompetenteObtenue);
  }

  @Test
  void
      lorsqueLonSouhaiteRecupererUneAutoriteCompetentePourUnMisEnCauseADomicileAbsentDuReferentiel_alorsRetourneNull() {
    // Given
    var libelle = "libelleAbsentDuReferentiel";
    // When
    var autoriteCompetenteObtenue =
        referentielDesAutoritesCompetentesParMisEnCausePourMaltraitance.recupererAutoriteCompetente(
            libelle);
    // Then
    assertNull(autoriteCompetenteObtenue);
  }
}
