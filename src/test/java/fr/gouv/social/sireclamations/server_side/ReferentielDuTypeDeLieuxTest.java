package fr.gouv.social.sireclamations.server_side;

import static org.junit.jupiter.api.Assertions.*;

import fr.gouv.social.sireclamations.hexagone.domain.CodeTypeDeLieu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class ReferentielDuTypeDeLieuxTest {

  private ReferentielDuTypeDeLieux referentielDuTypeDeLieux;

  @BeforeEach
  void setup() {
    Resource csvResource = new ClassPathResource("data/mappingFormulaireV2-typeLieux-test.csv");
    referentielDuTypeDeLieux = new ReferentielDuTypeDeLieux(csvResource);
  }

  @Test
  void lorsqueLonVeutRecupererLeCodeTypeDeLieuPourUnEtablissementDeSante_alorsRetourneETAB_M() {
    // Given
    var libelleDuLieu =
        "Dans un établissement de santé (hôpital, clinique, laboratoire, pharmacie ...)";

    // When
    var codeTypeLieux =
        referentielDuTypeDeLieux.recupererCodeTypeDeLieuxAPartirDuLibelle(libelleDuLieu);
    // Then
    assertEquals(CodeTypeDeLieu.ETAB, codeTypeLieux);
  }
}
