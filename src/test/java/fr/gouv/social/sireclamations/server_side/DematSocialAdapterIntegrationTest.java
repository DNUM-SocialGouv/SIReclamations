package fr.gouv.social.sireclamations.server_side;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DematSocialAdapterIntegrationTest {

  @Autowired private DematSocialAdapter dematSocialAdapter;

  @Test
  @Tag("localOnly")
  void lorsqueJappelleDematSocialAvecUnNumeroDeDossierExistant_alorsRetourneLeDossier()
      throws IOException {
    // Given
    var numeroDossier = 185631; // Correspond a un dossier existant avec un Ehpad pour établissement
    // When
    var dossier = dematSocialAdapter.recupererDossier(numeroDossier);
    // Then
    assertEquals(numeroDossier, dossier.getNumeroDossier());
  }
}
