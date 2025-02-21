package fr.gouv.social.sireclamations.server_side;

import static org.junit.jupiter.api.Assertions.*;

import fr.gouv.social.sireclamations.hexagone.domain.ports.ReferentielDesAutoritesCompetentesParServicesADomicile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class ReferentielDesAutoritesCompetentesParServiceADomicileCsvAdapterTest {

  private ReferentielDesAutoritesCompetentesParServicesADomicile
      referentielDesAutoritesCompetentesParServicesADomicile;

  @BeforeEach
  void setup() {
    Resource csvResource = new ClassPathResource("data/servicesADomicile-test.csv");
    referentielDesAutoritesCompetentesParServicesADomicile =
        new ReferentielDesAutoritesCompetentesParServiceADomicileCsvAdapter(csvResource);
  }

  @Test
  void
      lorsqueLonSouhaiteRecupererUneAutoriteCompetentePourUnServiceExistant_alorsRetourneLautoriteCompetente() {
    // Given
    var serviceInexistant = "Hospitalisation à domicile";
    // When
    var autoriteCompetenteObtenue =
        referentielDesAutoritesCompetentesParServicesADomicile.recupererAutoriteCompetente(
            serviceInexistant);
    // Then
    assertEquals("ARS", autoriteCompetenteObtenue);
  }

  @Test
  void lorsqueLonSouhaiteRecupererUneAutoriteCompetentePourUnServiceInexistant_alorsRetourneNull() {
    // Given
    var serviceInexistant = "serviceInexistant";
    // When
    var autoriteCompetenteObtenue =
        referentielDesAutoritesCompetentesParServicesADomicile.recupererAutoriteCompetente(
            serviceInexistant);
    // Then
    assertNull(autoriteCompetenteObtenue);
  }
}
