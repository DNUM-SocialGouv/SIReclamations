package fr.gouv.social.sireclamations.server_side;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class ReferentielDesLieuxDeSurvenueCsvAdapterTest {

  private ReferentielDesLieuxDeSurvenueCsvAdapter referentielDesLieuxDeSurvenueCsvAdapter;

  @BeforeEach
  void setup() {
    Resource csvResource = new ClassPathResource("data/autoriteCompetenteLieuDeSurvenue-test.csv");
    referentielDesLieuxDeSurvenueCsvAdapter =
        new ReferentielDesLieuxDeSurvenueCsvAdapter(csvResource);
  }

  @Test
  void
      lorsqueLonSouhaiteRecupererUneAutoriteCompetentePourUnLieuDeSurvenueExistant_alorsRetourneLautoriteCompetente() {
    // Given
    var lieuDeSurvenue = "Etablissement de santé (hôpital, clinique, laboratoire, pharmacie...)";
    // When
    var autoriteCompetenteObtenue =
        referentielDesLieuxDeSurvenueCsvAdapter.recupererAutoriteCompetente(lieuDeSurvenue);
    // Then
    assertEquals("ARS", autoriteCompetenteObtenue);
  }

  @Test
  void
      lorsqueLonSouhaiteRecupererUneAutoriteCompetentePourUnLieuDeSurvenueAbsentDuReferentiel_alorsRetourneNull() {
    // Given
    var lieuDeSurvenue = "inconnu";
    // When
    var autoriteCompetenteObtenue =
        referentielDesLieuxDeSurvenueCsvAdapter.recupererAutoriteCompetente(lieuDeSurvenue);
    // Then
    assertNull(autoriteCompetenteObtenue);
  }
}
