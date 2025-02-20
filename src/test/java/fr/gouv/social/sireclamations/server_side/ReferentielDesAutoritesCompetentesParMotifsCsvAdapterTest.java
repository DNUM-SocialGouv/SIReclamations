package fr.gouv.social.sireclamations.server_side;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import fr.gouv.social.sireclamations.hexagone.domain.ports.ReferentielDesAutoritesCompetentesParMotifs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class ReferentielDesAutoritesCompetentesParMotifsCsvAdapterTest {

  private ReferentielDesAutoritesCompetentesParMotifs referentielDesAutoritesCompetentesParMotifs;

  @BeforeEach
  void setup() {
    Resource csvResource = new ClassPathResource("data/motifs-test.csv");
    referentielDesAutoritesCompetentesParMotifs =
        new ReferentielDesAutoritesCompetentesParMotifsCsvAdapter(csvResource);
  }

  @Test
  void
      lorsqueLonSouhaiteRecupererUneAutoriteCompetentePourUnMotifExistant_alorsRetourneLautoriteCompetente() {
    // Given
    var motif =
        "Difficultés d'accès aux soins (établissement ou professionnel) (ex: manque de moyen humain...)";
    // When
    var autoriteCompetenteObtenue =
        referentielDesAutoritesCompetentesParMotifs.recupererAutoriteCompetente(motif);
    // Then
    assertEquals("ARS", autoriteCompetenteObtenue);
  }

  @Test
  void
      lorsqueLonSouhaiteRecupererUneAutoriteCompetentePourUnMotifAbsentDuReferentiel_alorsRetourneNull() {
    // Given
    var motif = "autre";
    // When
    var autoriteCompetenteObtenue =
        referentielDesAutoritesCompetentesParMotifs.recupererAutoriteCompetente(motif);
    // Then
    assertNull(autoriteCompetenteObtenue);
  }
}
