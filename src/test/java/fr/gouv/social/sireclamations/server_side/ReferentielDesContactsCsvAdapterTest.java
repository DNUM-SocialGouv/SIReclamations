package fr.gouv.social.sireclamations.server_side;

import static org.assertj.core.api.Assertions.assertThat;

import fr.gouv.social.sireclamations.hexagone.domain.AutoriteCompetente;
import fr.gouv.social.sireclamations.hexagone.domain.ports.ReferentielDesContacts;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class ReferentielDesContactsCsvAdapterTest {
  private ReferentielDesContacts contactsPort;

  @BeforeEach
  void setup() {
    Resource csvResource = new ClassPathResource("data/departements-ac-contacts-test.csv");
    contactsPort = new ReferentielDesContactsCsvAdapter(csvResource);
  }

  @Test
  void lorsqueCodePostalEstMarseilleEtAutoriteCompetenteARS_alorsRenvoiEmailContactDeARSetAutres() {
    // Given
    var codePostal = 13000;
    var autoritesCompetentes = Set.of(AutoriteCompetente.ARS);
    // When
    var result = contactsPort.recupererContacts(codePostal, autoritesCompetentes);
    // Then
    var expectedContacts = List.of("BAL_Region@ARS.fr", "BAL_dept_13@ARS.fr", "BAL@autre93.fr");
    assertThat(result).containsExactlyInAnyOrderElementsOf(expectedContacts);
  }

  @Test
  void
      lorsqueCodePostalEstDeBastiaEtAutoriteCompetenteARSetCD_alorsRenvoiEmailContactDeARSetCDetAutres() {
    // Given
    var codePostal = 20200;
    var autoritesCompetentes = Set.of(AutoriteCompetente.ARS, AutoriteCompetente.CD);
    // When
    var result = contactsPort.recupererContacts(codePostal, autoritesCompetentes);
    // Then
    var expectedContacts =
        List.of("BAL_Region@ARS.fr", "BAL_dept_2B@ARS.fr", "BAL@CD2B.fr", "BAL@autre94.fr");
    assertThat(result).containsExactlyInAnyOrderElementsOf(expectedContacts);
  }

  @Test
  void lorsqueCodePostalEstDeAjaccioEtAutoriteCompetenteCD_alorsRenvoiEmailContactDeCDetAutres() {
    // Given
    var codePostal = 20000;
    var autoritesCompetentes = Set.of(AutoriteCompetente.CD);
    // When
    var result = contactsPort.recupererContacts(codePostal, autoritesCompetentes);
    // Then
    var expectedContacts = List.of("BAL@CD2A.fr", "BAL@autre94.fr");
    assertThat(result).containsExactlyInAnyOrderElementsOf(expectedContacts);
  }

  @Test
  void
      lorsqueCodePostalEstDeMayotteEtAutoriteCompetenteDDETS_alorsRenvoiEmailContactDeDDETSetAutres() {
    // Given
    var codePostal = 97600;
    var autoritesCompetentes = Set.of(AutoriteCompetente.DDETS);
    // When
    var result = contactsPort.recupererContacts(codePostal, autoritesCompetentes);
    // Then
    var expectedContacts = List.of("BAL@DDETS976.fr", "BAL@autre976.fr");
    assertThat(result).containsExactlyInAnyOrderElementsOf(expectedContacts);
  }

  @Test
  void
      lorsqueCodePostalEstDeLaReunionEtAutoritesCompetentesARSetCDetDDETS_alorsRenvoiEmailContactDeARSetCDetDDETSetAutres() {
    // Given
    var codePostal = 97400;
    var autoritesCompetentes =
        Set.of(AutoriteCompetente.ARS, AutoriteCompetente.CD, AutoriteCompetente.DDETS);
    // When
    var result = contactsPort.recupererContacts(codePostal, autoritesCompetentes);
    // Then
    var expectedContacts =
        List.of(
            "BAL_Region@ARS.fr",
            "BAL_dept_974@ARS.fr",
            "BAL@CD974.fr",
            "BAL@DDETS974.fr",
            "BAL@autre4.fr");
    assertThat(result).containsExactlyInAnyOrderElementsOf(expectedContacts);
  }
}
