package fr.gouv.social.sireclamations.infrastructure;

import fr.gouv.social.sireclamations.hexagone.port.ContactsPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ContactsAdapterTest {
    private ContactsPort contactsPort;

    @BeforeEach
    void setup() throws Exception {
        Resource csvResource = new ClassPathResource("data/departements-ac-contacts-test.csv");
        contactsPort = new ContactsAdapter(csvResource);
    }
    @Test
    void lorsqueFinessProvientDeMarseilleEtAutoriteCompetenteARS_alorsRenvoiEmailContactDeARSetAutres() {
        //Given
        var finess = "130000000";
        var autoritesCompetentes = List.of("ARS");
        //When
        var result = contactsPort.recupererContacts(finess, autoritesCompetentes);
        //Then
        var expectedContacts = List.of("BAL_Region@ARS.fr", "BAL_dept_13@ARS.fr", "BAL@autre93.fr");
        assertThat(result).usingRecursiveComparison().isEqualTo(expectedContacts);
    }

    @Test
    void lorsqueFinessProvientDeBastiaEtAutoriteCompetenteARSetCD_alorsRenvoiEmailContactDeARSetCDetAutres() {
        //Given
        var finess = "2B0000000";
        var autoritesCompetentes = List.of("ARS", "CD");
        //When
        var result = contactsPort.recupererContacts(finess, autoritesCompetentes);
        //Then
        var expectedContacts = List.of("BAL_Region@ARS.fr", "BAL_dept_2B@ARS.fr", "BAL@CD2B.fr", "BAL@autre94.fr");
        assertThat(result).usingRecursiveComparison().isEqualTo(expectedContacts);
    }

    @Test
    void lorsqueFinessProvientDeAjaccioEtAutoriteCompetenteCD_alorsRenvoiEmailContactDeCDetAutres() {
        //Given
        var finess = "2A0000000";
        var autoritesCompetentes = List.of("CD");
        //When
        var result = contactsPort.recupererContacts(finess, autoritesCompetentes);
        //Then
        var expectedContacts = List.of("BAL@CD2A.fr", "BAL@autre94.fr");
        assertThat(result).usingRecursiveComparison().isEqualTo(expectedContacts);
    }

    @Test
    void lorsqueFinessProvientDeMayotteEtAutoriteCompetenteDDETS_alorsRenvoiEmailContactDeDDETSetAutres() {
        //Given
        var finess = "980500000";
        var autoritesCompetentes = List.of("DDETS");
        //When
        var result = contactsPort.recupererContacts(finess, autoritesCompetentes);
        //Then
        var expectedContacts = List.of("BAL@DDETS976.fr", "BAL@autre976.fr");
        assertThat(result).usingRecursiveComparison().isEqualTo(expectedContacts);
    }

    @Test
    void lorsqueFinessProvientDeLaReunionEtAutoritesCompetentesARSetCDetDDETS_alorsRenvoiEmailContactDeARSetCDetDDETSetAutres() {
        //Given
        var finess = "970400000";
        var autoritesCompetentes = List.of("ARS", "CD", "DDETS");
        //When
        var result = contactsPort.recupererContacts(finess, autoritesCompetentes);
        //Then
        var expectedContacts = List.of("BAL_Region@ARS.fr", "BAL_dept_974@ARS.fr", "BAL@CD974.fr", "BAL@DDETS974.fr", "BAL@autre4.fr");
        assertThat(result).usingRecursiveComparison().isEqualTo(expectedContacts);
    }

}