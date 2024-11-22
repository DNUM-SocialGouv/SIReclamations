package fr.gouv.social.sireclamations.hexagone;

import fr.gouv.social.sireclamations.hexagone.domain.Reclamation;
import fr.gouv.social.sireclamations.hexagone.domain.port.DematSocial;
import fr.gouv.social.sireclamations.hexagone.domain.DossierDeReclamation;
import fr.gouv.social.sireclamations.hexagone.domain.Etablissement;
import fr.gouv.social.sireclamations.hexagone.domain.port.ReferentielDeCategoriesDEtablissements;
import fr.gouv.social.sireclamations.hexagone.domain.port.ReferentielDesContacts;
import fr.gouv.social.sireclamations.server_side.EmailService;
import fr.gouv.social.sireclamations.server_side.exceptions.AutoriteCompetenteNotFoundException;
import fr.gouv.social.sireclamations.server_side.exceptions.ContactNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeposerReclamationTest {

    @InjectMocks
    DeposerReclamation deposerReclamation;
    @Mock
    DematSocial dematSocial;
    @Mock
    private ReferentielDeCategoriesDEtablissements referentielDeCategoriesDEtablissements;
    @Mock
    private ReferentielDesContacts contactsPort;
    @Mock
    private EmailService emailService;

    @Test
    void lorsqueLonDeposeUneReclamationConcernantUneEHPADsurParis_alorsEnvoiUnMailAuContactDeLARSdeParis(){
        //Given
        var numeroDossier = "12345";
        var codeSousCategorieEtablissement = "500";
        var codePostal = "94300";
        String finess = "940003858";
        String nom = "EHPAD LE VERGER DE VINCENNES";
        var etablissement = new Etablissement(finess, codeSousCategorieEtablissement, codePostal, nom);
        var dossierReclamation = new DossierDeReclamation(numeroDossier, etablissement);
        when(dematSocial.recupererDossier(numeroDossier)).thenReturn(dossierReclamation);
        when(referentielDeCategoriesDEtablissements.recupererAutoritesCompetentesParCodeSousCategorieEtablissement(codeSousCategorieEtablissement)).thenReturn(List.of("ARS"));
        when(contactsPort.recupererContacts(finess, List.of("ARS"))).thenReturn(List.of("idf@ars.com"));

        //When
        var reclamationActuelle = deposerReclamation.executer(numeroDossier);
        //Then
        var reclamationAttendue = new Reclamation(dossierReclamation, List.of("ARS"), List.of("idf@ars.com"));
        ArgumentCaptor<List<String>> emailsCaptor = ArgumentCaptor.forClass(List.class);
        var destinatairesEmails = List.of("idf@ars.com");
        verify(emailService, times(1)).envoyer(emailsCaptor.capture(), any());
        assertThat(emailsCaptor.getValue()).isEqualTo(destinatairesEmails);
        assertThat(reclamationActuelle).usingRecursiveComparison().isEqualTo(reclamationAttendue);

    }

    @Test
    void lorsqueLonDeposeUneReclamationConcernantUneCategorieEtablissementInconnu_alorsAutoriteCompetenteNotFoundException(){
        //Given
        var numeroDossier = "12345";
        var codeSousCategorieEtablissementIntrouvable = "123-code-categorie-introuvable-45679";
        var codePostal = "94300";
        String finess = "940003858";
        String nom = "EHPAD LE VERGER DE VINCENNES";
        var etablissement = new Etablissement(finess, codeSousCategorieEtablissementIntrouvable, codePostal, nom);
        var dossierReclamation = new DossierDeReclamation(numeroDossier, etablissement);
        when(dematSocial.recupererDossier(numeroDossier)).thenReturn(dossierReclamation);
        when(referentielDeCategoriesDEtablissements.recupererAutoritesCompetentesParCodeSousCategorieEtablissement(codeSousCategorieEtablissementIntrouvable)).thenReturn(Collections.emptyList());
        //When Then
        assertThatThrownBy(
                () -> deposerReclamation.executer(numeroDossier))
                .isInstanceOf(AutoriteCompetenteNotFoundException.class)
                .hasMessage("Aucune autorité compétente trouvée pour le code sous-catégorie d'établissement : 123-code-categorie-introuvable-45679");

    }

    @Test
    void lorsqueLonDeposeUneReclamationConcernantUneAutoriteSansContactsRenseignés_alorsRetourneContactNotFoundException(){
        //Given
        var numeroDossier = "12345";
        var codeSousCategorieEtablissement = "500";
        var codePostal = "94300";
        String finess = "940003858";
        String nom = "EHPAD LE VERGER DE VINCENNES";
        var etablissement = new Etablissement(finess, codeSousCategorieEtablissement, codePostal, nom);
        var dossierReclamation = new DossierDeReclamation(numeroDossier, etablissement);
        when(dematSocial.recupererDossier(numeroDossier)).thenReturn(dossierReclamation);
        when(referentielDeCategoriesDEtablissements.recupererAutoritesCompetentesParCodeSousCategorieEtablissement(codeSousCategorieEtablissement)).thenReturn(List.of("ARS"));
        when(contactsPort.recupererContacts(finess, List.of("ARS"))).thenReturn(Collections.emptyList());
        //When Then
        assertThatThrownBy(
                () -> deposerReclamation.executer(numeroDossier))
                .isInstanceOf(ContactNotFoundException.class)
                .hasMessage("Aucun contact n'a été trouvé. Autorité(s) compétente(s) : ARS,  Code sous-catégorie d'établissement : 500");

    }
}