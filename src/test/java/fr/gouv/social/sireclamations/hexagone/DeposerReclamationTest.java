package fr.gouv.social.sireclamations.hexagone;

import fr.gouv.social.sireclamations.hexagone.domain.CodeTypeDuMisEnCause;
import fr.gouv.social.sireclamations.hexagone.domain.Reclamation;
import fr.gouv.social.sireclamations.hexagone.exceptions.DematSocialException;
import fr.gouv.social.sireclamations.hexagone.domain.ports.*;
import fr.gouv.social.sireclamations.hexagone.domain.DossierDeReclamation;
import fr.gouv.social.sireclamations.hexagone.domain.Etablissement;
import fr.gouv.social.sireclamations.server_side.EmailService;
import fr.gouv.social.sireclamations.hexagone.exceptions.AutoriteCompetenteNotFoundException;
import fr.gouv.social.sireclamations.hexagone.exceptions.ContactNotFoundException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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

    @Nested
    class DeposerReclamationEnRecuperantAutoriteCompetenteParTypeEtablissement {
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
            when(referentielDesContacts.recupererContacts(codePostal, Set.of("ARS"))).thenReturn(List.of("idf@ars.com"));

            //When
            var reclamationActuelle = deposerReclamation.executer(numeroDossier);
            //Then
            var reclamationAttendue = new Reclamation(dossierReclamation, Set.of("ARS"), List.of("idf@ars.com"), etablissement);
            assertThat(reclamationActuelle).usingRecursiveComparison().isEqualTo(reclamationAttendue);
            ArgumentCaptor<List<String>> emailsCaptor = ArgumentCaptor.forClass(List.class);
            var destinatairesEmails = List.of("idf@ars.com");
            verify(emailService, times(1)).envoyer(emailsCaptor.capture(), any());
            assertThat(emailsCaptor.getValue()).isEqualTo(destinatairesEmails);

        }
    }

    @Nested
    class DeposerReclamationEnRecuperantAutoriteCompetenteEnFonctionDuMisEnCause {
        @Test
        void lorsqueLonDeposeUneReclamationConcernantUnEtablissementDeTypeResidencesAutonomieEtUnPsMisEnCause_alorsRenvoilesMailsDeContactDeCDetARS() throws IOException {
            // Given
            var numeroDossier = 12345;
            var codeSousCategorieEtablissement = 202;
            var codePostal = 78100;
            var finess = "780802351";
            String nom = "RESIDENCE AUTONOMIE BERLIOZ";
            String libelleDuMisEnCauseProvenantDuFormulaire = "Un professionnel de santé (médecin, infirmier, aide-soignant, kiné, ostéopathe...)";
            var etablissement = new Etablissement(finess, codeSousCategorieEtablissement, codePostal, nom);
            var dossierReclamation = new DossierDeReclamation(numeroDossier, etablissement, libelleDuMisEnCauseProvenantDuFormulaire);
            when(dematSocial.recupererDossier(numeroDossier)).thenReturn(dossierReclamation);
            when(referentielDeCategoriesDEtablissements.recupererAutoritesCompetentesParCodeSousCategorieEtablissement(codeSousCategorieEtablissement)).thenReturn(List.of("CD"));
            when(referentielDesTypeDeMisEnCause.recupererTypeDuMisEnCause(libelleDuMisEnCauseProvenantDuFormulaire)).thenReturn(CodeTypeDuMisEnCause.PS);
            when(referentielDesAutoritesCompetentesParTypeDeMisEnCause.recupererAutoriteCompetentePourUnTypeDeMisEnCause(CodeTypeDuMisEnCause.PS)).thenReturn("ARS");
            when(referentielDesContacts.recupererContacts(codePostal, Set.of("CD", "ARS"))).thenReturn(List.of("idf@cd.com", "idf@ars.com"));
            //When
            var reclamationActuelle = deposerReclamation.executer(numeroDossier);
            //Then
            var reclamationAttendue = new Reclamation(dossierReclamation, Set.of("CD", "ARS"), List.of("idf@cd.com", "idf@ars.com"), etablissement);
            assertThat(reclamationActuelle).usingRecursiveComparison().isEqualTo(reclamationAttendue);
            ArgumentCaptor<List<String>> emailsCaptor = ArgumentCaptor.forClass(List.class);
            var destinatairesEmails = List.of("idf@cd.com", "idf@ars.com");
            verify(emailService, times(1)).envoyer(emailsCaptor.capture(), any());
            assertThat(emailsCaptor.getValue()).isEqualTo(destinatairesEmails);
        }
    }

    @Nested
    class DeposerReclamationSurvenueADomicile{

        @Test
        void lorsqueLonDeposeUneRecamationSurvenueADomicileAParisAvecUnMECautreResident_alorsRenvoiUneReclamationEtEnvoiUnMailAuContactDuCdDeParis() throws IOException {
            //Given
            var numeroDossier = 12345;
            var codePostal = 78100;
            String libelleDuMisEnCauseProvenantDuFormulaire = "Un autre résident ou un autre patient";
            var domicile = new Domicile(codePostal, "18 rue mon domicile 78100 Saint-Germain-en-Laye");
            var dossierReclamation = new DossierDeReclamation(numeroDossier, domicile, libelleDuMisEnCauseProvenantDuFormulaire);
            when(dematSocial.recupererDossier(numeroDossier)).thenReturn(dossierReclamation);
            when(referentielDesTypeDeMisEnCause.recupererTypeDuMisEnCause(libelleDuMisEnCauseProvenantDuFormulaire)).thenReturn(CodeTypeDuMisEnCause.P);
            when(referentielDesAutoritesCompetentesParTypeDeMisEnCause.recupererAutoriteCompetentePourUnTypeDeMisEnCause(CodeTypeDuMisEnCause.P)).thenReturn("CD");
            when(referentielDesContacts.recupererContacts(codePostal, Set.of("CD"))).thenReturn(List.of("idf@cd.com"));

            //When
            var reclamationActuelle = deposerReclamation.executer(numeroDossier);
            //Then
            var reclamationAttendue = new Reclamation(dossierReclamation, Set.of("CD"), List.of("idf@cd.com"), domicile);
            assertThat(reclamationActuelle).usingRecursiveComparison().isEqualTo(reclamationAttendue);
            ArgumentCaptor<List<String>> emailsCaptor = ArgumentCaptor.forClass(List.class);
            var destinatairesEmails = List.of("idf@cd.com");
            verify(emailService, times(1)).envoyer(emailsCaptor.capture(), any());
            assertThat(emailsCaptor.getValue()).isEqualTo(destinatairesEmails);
        }

        @Test
        void lorsqueLonDeposeUneRecamationSurvenueADomicileAParisAvecUnMECPersonnelDeSante_alorsRenvoiUneReclamationEtEnvoiUnMailAuContactDuArsEtCdDeParis() throws IOException {
            //Given
            var numeroDossier = 12345;
            var codePostal = 78100;
            var domicile = new Domicile(codePostal, "18 rue mon domicile 78100 Saint-Germain-en-Laye");
            String libelleDuMisEnCauseProvenantDuFormulaire = "Un professionnel de santé (médecin, infirmier, aide-soignant, kiné, ostéopathe...)";
            var dossierReclamation = new DossierDeReclamation(numeroDossier, domicile, libelleDuMisEnCauseProvenantDuFormulaire);
            when(dematSocial.recupererDossier(numeroDossier)).thenReturn(dossierReclamation);
            when(referentielDesTypeDeMisEnCause.recupererTypeDuMisEnCause(libelleDuMisEnCauseProvenantDuFormulaire)).thenReturn(CodeTypeDuMisEnCause.PS);
            when(referentielDesAutoritesCompetentesParTypeDeMisEnCause.recupererAutoriteCompetentePourUnTypeDeMisEnCause(CodeTypeDuMisEnCause.PS)).thenReturn("ARS");
            when(referentielDesContacts.recupererContacts(codePostal, Set.of("CD", "ARS"))).thenReturn(List.of("idf@cd.com", "idf@ars.com"));

            //When
            var reclamationActuelle = deposerReclamation.executer(numeroDossier);

            //Then
            var reclamationAttendue = new Reclamation(dossierReclamation, Set.of("CD","ARS"), List.of("idf@cd.com","idf@ars.com"), domicile);
            assertThat(reclamationActuelle).usingRecursiveComparison().isEqualTo(reclamationAttendue);
            ArgumentCaptor<List<String>> emailsCaptor = ArgumentCaptor.forClass(List.class);
            var destinatairesEmails = List.of("idf@cd.com", "idf@ars.com");
            verify(emailService, times(1)).envoyer(emailsCaptor.capture(), any());
            assertThat(emailsCaptor.getValue()).isEqualTo(destinatairesEmails);
        }
    }
    @Nested
    class ExceptionDeposerReclamation {
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
            when(referentielDesContacts.recupererContacts(codePostal, Collections.emptySet())).thenReturn(Collections.emptyList());
            //When Then
            assertThatThrownBy(
                    () -> deposerReclamation.executer(numeroDossier))
                    .isInstanceOf(AutoriteCompetenteNotFoundException.class)
                    .hasMessage("Aucune autorité compétente n'a été trouvée pour le dossier : 12345");

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
            when(referentielDesContacts.recupererContacts(codePostal, Set.of("ARS"))).thenReturn(Collections.emptyList());
            //When Then
            assertThatThrownBy(
                    () -> deposerReclamation.executer(numeroDossier))
                    .isInstanceOf(ContactNotFoundException.class)
                    .hasMessage("Aucun contact n'a été trouvé pour le dossier : 12345");

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
}