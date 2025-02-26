package fr.gouv.social.sireclamations.server_side;

import static org.junit.jupiter.api.Assertions.*;

import fr.gouv.social.sireclamations.hexagone.domain.ports.ReferentielDesAutoritesCompetentesParCategoriesDEtablissements;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class ReferentielDesAutoritesCompetentesParCategoriesDEtablissementsCsvAdapterTest {
  private ReferentielDesAutoritesCompetentesParCategoriesDEtablissements
      referentielDesAutoritesCompetentesParCategoriesDEtablissements;

  @BeforeEach
  void setUp() throws Exception {
    Resource csvResource = new ClassPathResource("data/sousCatFINESS-ac-test.csv");
    referentielDesAutoritesCompetentesParCategoriesDEtablissements =
        new ReferentielDesAutoritesCompetentesParCategorieDEtablissementCsvAdapter(csvResource);
  }

  @Test
  void lorsqueLonVeutRecupererAutoriteCompetentePourUnEhpad_alorsRenvoiARSetCD() {
    // Given
    var codeSousCategorie = 500;
    // When
    var autoritesCompetentes =
        referentielDesAutoritesCompetentesParCategoriesDEtablissements
            .recupererAutoritesCompetentesParCodeSousCategorieEtablissement(codeSousCategorie);
    // Then
    assertEquals(List.of("ARS", "CD"), autoritesCompetentes);
  }

  @Test
  void
      lorsqueLonVeutRecupererAutoriteCompetentePourUneCategorieEtablissementInexistante_alorsRenvoiNull() {
    // Given
    var codeSousCategorieInexistant = 1234567891;
    // When
    var autoritesCompetentes =
        referentielDesAutoritesCompetentesParCategoriesDEtablissements
            .recupererAutoritesCompetentesParCodeSousCategorieEtablissement(
                codeSousCategorieInexistant);
    // Then
    assertTrue(autoritesCompetentes.isEmpty());
  }
}
