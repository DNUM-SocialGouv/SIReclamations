package fr.gouv.social.sireclamations.hexagone;

import fr.gouv.social.sireclamations.hexagone.domain.CodeTypeDuMisEnCause;
import fr.gouv.social.sireclamations.hexagone.domain.Reclamation;
import fr.gouv.social.sireclamations.hexagone.domain.exceptions.DematSocialException;
import fr.gouv.social.sireclamations.hexagone.domain.port.*;
import fr.gouv.social.sireclamations.hexagone.domain.DossierDeReclamation;
import fr.gouv.social.sireclamations.hexagone.domain.Etablissement;
import fr.gouv.social.sireclamations.server_side.EmailService;
import fr.gouv.social.sireclamations.server_side.exceptions.AutoriteCompetenteNotFoundException;
import fr.gouv.social.sireclamations.server_side.exceptions.ContactNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
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
    private ReferentielDesContacts referentielDesContacts;
    @Mock
    private ReferentielDesTypeDeMisEnCause referentielDesTypeDeMisEnCause;
    @Mock
    private ReferentielDesAutoritesCompetentesParTypeDeMisEnCause referentielDesAutoritesCompetentesParTypeDeMisEnCause;
    @Mock
    private EmailService emailService;

    @Test
    void lorsqueLonDeposeUneReclamationConcernantUnEHPADsurParis_alorsRenvoiUneReclamationAssociéeEtEnvoiUnMailAuContactDeLARSdeParis() throws IOException {
        //Given
        var numeroDossier = 12345;
        var codeSousCategorieEtablissement = 500;
        var codePostal = 94300;
        var finess = "940003858";
        String nom = "EHPAD LE VERGER DE VINCENNES";
        String libelleDuMisEnCauseProvenantDuFormulaire = "Un professionnel de santé (médecin, infirmier, aide-soignant, kiné, ostéopathe...)";
        var etablissement = new Etablissement(finess, codeSousCategorieEtablissement, codePostal, nom);
        var dossierReclamation = new DossierDeReclamation(numeroDossier, etablissement, libelleDuMisEnCauseProvenantDuFormulaire);
        when(dematSocial.recupererDossier(numeroDossier)).thenReturn(dossierReclamation);
        when(referentielDeCategoriesDEtablissements.recupererAutoritesCompetentesParCodeSousCategorieEtablissement(codeSousCategorieEtablissement)).thenReturn(List.of("ARS"));
        when(referentielDesTypeDeMisEnCause.recupererTypeDuMisEnCause(libelleDuMisEnCauseProvenantDuFormulaire)).thenReturn(CodeTypeDuMisEnCause.PS);
        when(referentielDesAutoritesCompetentesParTypeDeMisEnCause.recupererAutoriteCompetentePourUnTypeDeMisEnCause(CodeTypeDuMisEnCause.PS)).thenReturn("ARS");
        when(referentielDesContacts.recupererContacts(finess, List.of("ARS"))).thenReturn(List.of("idf@ars.com"));

        //When
        var reclamationActuelle = deposerReclamation.executer(numeroDossier);
        //Then
        var reclamationAttendue = new Reclamation(dossierReclamation, List.of("ARS"), List.of("idf@ars.com"));
        assertThat(reclamationActuelle).usingRecursiveComparison().isEqualTo(reclamationAttendue);
        ArgumentCaptor<List<String>> emailsCaptor = ArgumentCaptor.forClass(List.class);
        var destinatairesEmails = List.of("idf@ars.com");
        verify(emailService, times(1)).envoyer(emailsCaptor.capture(), any());
        assertThat(emailsCaptor.getValue()).isEqualTo(destinatairesEmails);

    }

    @Test
    void lorsqueLonDeposeUneReclamationConcernantUnEhpadDeTypeResidencesAutonomieEtUnPsMisEnCause_alorsRenvoilesMailsDeContactDeCDetARS() throws IOException {
        // Given
        var numeroDossier = 12345;
        var codeSousCategorieEtablissement = 202;
        var codePostal = 94300;
        var finess = "940003858";
        String nom = "EHPAD LE VERGER DE VINCENNES";
        String libelleDuMisEnCauseProvenantDuFormulaire = "Un professionnel de santé (médecin, infirmier, aide-soignant, kiné, ostéopathe...)";
        var etablissement = new Etablissement(finess, codeSousCategorieEtablissement, codePostal, nom);
        var dossierReclamation = new DossierDeReclamation(numeroDossier, etablissement, libelleDuMisEnCauseProvenantDuFormulaire);
        when(dematSocial.recupererDossier(numeroDossier)).thenReturn(dossierReclamation);
        when(referentielDeCategoriesDEtablissements.recupererAutoritesCompetentesParCodeSousCategorieEtablissement(codeSousCategorieEtablissement)).thenReturn(List.of("CD"));
        when(referentielDesTypeDeMisEnCause.recupererTypeDuMisEnCause(libelleDuMisEnCauseProvenantDuFormulaire)).thenReturn(CodeTypeDuMisEnCause.PS);
        when(referentielDesAutoritesCompetentesParTypeDeMisEnCause.recupererAutoriteCompetentePourUnTypeDeMisEnCause(CodeTypeDuMisEnCause.PS)).thenReturn("ARS");
        when(referentielDesContacts.recupererContacts(finess, List.of("CD","ARS"))).thenReturn(List.of("idf@cd.com", "idf@ars.com"));
        //When
        var reclamationActuelle = deposerReclamation.executer(numeroDossier);
        //Then
        var reclamationAttendue = new Reclamation(dossierReclamation, List.of("CD", "ARS"), List.of("idf@cd.com", "idf@ars.com"));
        assertThat(reclamationActuelle).usingRecursiveComparison().isEqualTo(reclamationAttendue);
        ArgumentCaptor<List<String>> emailsCaptor = ArgumentCaptor.forClass(List.class);
        var destinatairesEmails = List.of("idf@cd.com", "idf@ars.com");
        verify(emailService, times(1)).envoyer(emailsCaptor.capture(), any());
        assertThat(emailsCaptor.getValue()).isEqualTo(destinatairesEmails);
    }

    @Test
    void lorsqueLonDeposeUneReclamationConcernantUneCategorieEtablissementInconnu_alorsAutoriteCompetenteNotFoundException() throws IOException {
        //Given
        var numeroDossier = 12345;
        var codeSousCategorieEtablissementIntrouvable = 1234567910;
        var codePostal = 94300;
        var finess = "940003858";
        var nom = "EHPAD LE VERGER DE VINCENNES";
        String libelleDuMisEnCauseProvenantDuFormulaire = "Un professionnel de santé (médecin, infirmier, aide-soignant, kiné, ostéopathe...)";
        var etablissement = new Etablissement(finess, codeSousCategorieEtablissementIntrouvable, codePostal, nom);
        var dossierReclamation = new DossierDeReclamation(numeroDossier, etablissement, libelleDuMisEnCauseProvenantDuFormulaire);
        when(dematSocial.recupererDossier(numeroDossier)).thenReturn(dossierReclamation);
        when(referentielDeCategoriesDEtablissements.recupererAutoritesCompetentesParCodeSousCategorieEtablissement(codeSousCategorieEtablissementIntrouvable)).thenReturn(Collections.emptyList());
        when(referentielDesTypeDeMisEnCause.recupererTypeDuMisEnCause(libelleDuMisEnCauseProvenantDuFormulaire)).thenReturn(CodeTypeDuMisEnCause.PS);
        when(referentielDesAutoritesCompetentesParTypeDeMisEnCause.recupererAutoriteCompetentePourUnTypeDeMisEnCause(CodeTypeDuMisEnCause.PS)).thenReturn(null);
        //When Then
        assertThatThrownBy(
                () -> deposerReclamation.executer(numeroDossier))
                .isInstanceOf(AutoriteCompetenteNotFoundException.class)
                .hasMessage("Aucune autorité compétente trouvée pour le code sous-catégorie d'établissement : 1234567910");

    }

    @Test
    void lorsqueLonDeposeUneReclamationConcernantUneeAutoriteSansContactsRenseignés_alorsRetourneContactNotFoundException() throws IOException {
        //Given
        var numeroDossier = 12345;
        var codeSousCategorieEtablissement = 500;
        var codePostal = 94300;
        var finess = "940003858";
        var nom = "EHPAD LE VERGER DE VINCENNES";
        String libelleDuMisEnCauseProvenantDuFormulaire = "Un professionnel de santé (médecin, infirmier, aide-soignant, kiné, ostéopathe...)";
        var etablissement = new Etablissement(finess, codeSousCategorieEtablissement, codePostal, nom);
        var dossierReclamation = new DossierDeReclamation(numeroDossier, etablissement, libelleDuMisEnCauseProvenantDuFormulaire);
        when(dematSocial.recupererDossier(numeroDossier)).thenReturn(dossierReclamation);
        when(referentielDeCategoriesDEtablissements.recupererAutoritesCompetentesParCodeSousCategorieEtablissement(codeSousCategorieEtablissement)).thenReturn(List.of("ARS"));
        when(referentielDesTypeDeMisEnCause.recupererTypeDuMisEnCause(libelleDuMisEnCauseProvenantDuFormulaire)).thenReturn(CodeTypeDuMisEnCause.PS);
        when(referentielDesAutoritesCompetentesParTypeDeMisEnCause.recupererAutoriteCompetentePourUnTypeDeMisEnCause(CodeTypeDuMisEnCause.PS)).thenReturn("ARS");
        when(referentielDesContacts.recupererContacts(finess, List.of("ARS"))).thenReturn(Collections.emptyList());
        //When Then
        assertThatThrownBy(
                () -> deposerReclamation.executer(numeroDossier))
                .isInstanceOf(ContactNotFoundException.class)
                .hasMessage("Aucun contact n'a été trouvé. Autorité(s) compétente(s) : ARS,  Code sous-catégorie d'établissement : 500");

    }

    @Test
    void lorsqueLonSouhaiteDeposerUneReclamationConcernantUnDossierNexistantPasChezDematSocial_alorsRetourneDematSocialException() throws IOException {
        //Given
        var numeroDossier = 12345;
        when(dematSocial.recupererDossier(numeroDossier)).thenThrow(new IOException("erreur sur le dossier numero :" + numeroDossier));
        //When Then
        assertThatThrownBy(
                () -> deposerReclamation.executer(numeroDossier))
                .isInstanceOf(DematSocialException.class)
                .hasMessage("erreur sur le dossier numero :12345");

    }
}