package fr.gouv.social.sireclamations.hexagone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import fr.gouv.social.sireclamations.hexagone.domain.*;
import fr.gouv.social.sireclamations.hexagone.domain.ports.*;
import fr.gouv.social.sireclamations.hexagone.exceptions.AutoriteCompetenteNotFoundException;
import fr.gouv.social.sireclamations.hexagone.exceptions.DematSocialException;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeposerReclamationTest {

  @InjectMocks DeposerReclamation deposerReclamation;
  @Mock DematSocial dematSocial;

  @Mock
  private ReferentielDesAutoritesCompetentesParCategoriesDEtablissements
      referentielDesAutoritesCompetentesParCategoriesDEtablissements;

  @Mock
  private ReferentielDesAutoritesCompetentesParMisEnCauseADomicile
      referentielDesAutoritesCompetentesParMisEnCauseADomicile;

  @Mock
  private ReferentielDesAutoritesCompetentesParMisEnCauseEnEtablissement
      referentielDesAutoritesCompetentesParMisEnCauseEnEtablissement;

  @Mock
  private ReferentielDesAutoritesCompetentesParLieuDeSurvenue
      referentielDesAutoritesCompetentesParLieuDeSurvenue;

  @Mock
  private ReferentielDesAutoritesCompetentesParMotifs referentielDesAutoritesCompetentesParMotifs;

  @Mock
  private ReferentielDesAutoritesCompetentesParServicesADomicile
      referentielDesAutoritesCompetentesParServicesADomicile;

  @Test
  void
      deposerUneReclamationPourUneMaltraitanceParUnProfessionnelDeSanteDansUnEtablissementDeSante_doitRetournerARS()
          throws IOException {
    // Given
    var numeroDossier = 12345;
    var codeSousCategorieEtablissement = 109;
    var codePostal = 75014;
    var finess = "750000507";
    String nom = "HOPITAL SAINTE MARIE PARIS";
    String typeDeLieu =
        "Dans un établissement de santé (hôpital, clinique, laboratoire, pharmacie ...)";
    var etablissement =
        new Etablissement(finess, codeSousCategorieEtablissement, codePostal, nom, typeDeLieu);
    String libelleDuMisEnCauseProvenantDuFormulaire =
        "Un professionnel de santé (médecin, infirmier, aide-soignant, kiné, ostéopathe...)";
    var motifs =
        List.of("Problème comportemental, relationnel ou de communication avec une personne");
    var dossierReclamation =
        new DossierDeReclamation(
            numeroDossier, etablissement, libelleDuMisEnCauseProvenantDuFormulaire, true, motifs);
    when(dematSocial.recupererDossier(numeroDossier)).thenReturn(dossierReclamation);
    when(referentielDesAutoritesCompetentesParMisEnCauseEnEtablissement.recupererAutoriteCompetente(
            libelleDuMisEnCauseProvenantDuFormulaire))
        .thenReturn("ARS");
    when(referentielDesAutoritesCompetentesParLieuDeSurvenue.recupererAutoriteCompetente(
            typeDeLieu))
        .thenReturn("ARS");
    when(referentielDesAutoritesCompetentesParCategoriesDEtablissements
            .recupererAutoritesCompetentesParCodeSousCategorieEtablissement(
                codeSousCategorieEtablissement))
        .thenReturn(List.of("ARS"));
    // When
    var reclamationObtenue = deposerReclamation.executer(numeroDossier);
    // Then
    var reclamationAttendue =
        new Reclamation(dossierReclamation, Set.of(AutoriteCompetente.ARS), etablissement);
    assertThat(reclamationObtenue).usingRecursiveComparison().isEqualTo(reclamationAttendue);
  }

  @Test
  void
      deposerUneReclamationPourUneMaltraitanceParUnMembreDeLaFamilleDansUnEtablissementSocialPourUnMotifComportemental_doitRetournerCDetDDETS()
          throws IOException {
    // Given
    var numeroDossier = 12345;
    var codeSousCategorieEtablissement = 340;
    var codePostal = 38120;
    var finess = "380022723";
    String nom = "SERVICE DES MAJEURS PROTEGES";
    String typeDeLieu =
        "Dans un établissement ou service social (Centre de jour, service d'aide, service Mandataire Judiciaire à la Protection des Majeurs...";
    var motifs =
        List.of("Problème comportemental, relationnel ou de communication avec une personne");
    var etablissement =
        new Etablissement(finess, codeSousCategorieEtablissement, codePostal, nom, typeDeLieu);
    String libelleDuMisEnCauseProvenantDuFormulaire = "Un membre de la famille";
    var dossierReclamation =
        new DossierDeReclamation(
            numeroDossier, etablissement, libelleDuMisEnCauseProvenantDuFormulaire, true, motifs);
    when(dematSocial.recupererDossier(numeroDossier)).thenReturn(dossierReclamation);
    when(referentielDesAutoritesCompetentesParMisEnCauseEnEtablissement.recupererAutoriteCompetente(
            libelleDuMisEnCauseProvenantDuFormulaire))
        .thenReturn("CD");
    when(referentielDesAutoritesCompetentesParLieuDeSurvenue.recupererAutoriteCompetente(
            typeDeLieu))
        .thenReturn(null);
    when(referentielDesAutoritesCompetentesParMotifs.recupererAutoriteCompetente(motifs.get(0)))
        .thenReturn(null);
    when(referentielDesAutoritesCompetentesParCategoriesDEtablissements
            .recupererAutoritesCompetentesParCodeSousCategorieEtablissement(
                codeSousCategorieEtablissement))
        .thenReturn(List.of("DDETS"));
    // When
    var reclamationObtenue = deposerReclamation.executer(numeroDossier);
    // Then
    var reclamationAttendue =
        new Reclamation(
            dossierReclamation,
            Set.of(AutoriteCompetente.CD, AutoriteCompetente.DDETS),
            etablissement);
    assertThat(reclamationObtenue).usingRecursiveComparison().isEqualTo(reclamationAttendue);
  }

  @Test
  void
      deposerUneReclamationPourUneMaltraitanceParUnAutreProfessionnelDeLEtablissementDansUnEtablissementSocialPourUnMotifDeQualiteDesSoin_doitRetournerARS()
          throws IOException {
    // Given
    var numeroDossier = 12345;
    var codeSousCategorieEtablissement = 340;
    var codePostal = 38120;
    var finess = "380022723";
    String nom = "SERVICE DES MAJEURS PROTEGES";
    String typeDeLieu =
        "Dans un établissement ou service social (Centre de jour, service d'aide, service Mandataire Judiciaire à la Protection des Majeurs...";
    var motifs =
        List.of(
            "Problème de qualité des soins médicaux ou paramédicaux (ex: soins et/ou interventions inadaptés, absents ou abusifs...)");
    var etablissement =
        new Etablissement(finess, codeSousCategorieEtablissement, codePostal, nom, typeDeLieu);
    String libelleDuMisEnCauseProvenantDuFormulaire = "Un autre professionnel de l'établissement";
    var dossierReclamation =
        new DossierDeReclamation(
            numeroDossier, etablissement, libelleDuMisEnCauseProvenantDuFormulaire, true, motifs);
    when(dematSocial.recupererDossier(numeroDossier)).thenReturn(dossierReclamation);
    when(referentielDesAutoritesCompetentesParMisEnCauseEnEtablissement.recupererAutoriteCompetente(
            libelleDuMisEnCauseProvenantDuFormulaire))
        .thenReturn(null);
    when(referentielDesAutoritesCompetentesParLieuDeSurvenue.recupererAutoriteCompetente(
            typeDeLieu))
        .thenReturn(null);
    when(referentielDesAutoritesCompetentesParMotifs.recupererAutoriteCompetente(motifs.get(0)))
        .thenReturn("ARS");
    // When
    var reclamationObtenue = deposerReclamation.executer(numeroDossier);
    // Then
    var reclamationAttendue =
        new Reclamation(dossierReclamation, Set.of(AutoriteCompetente.ARS), etablissement);
    assertThat(reclamationObtenue).usingRecursiveComparison().isEqualTo(reclamationAttendue);
  }

  @Test
  void
      deposerUneReclamationPourUneMaltraitanceParUnMembreDeLaFamilleDansUnEtablissementDeSanteMotifComportemental_doitRetournerCDetARS()
          throws IOException {
    // Given
    var numeroDossier = 12345;
    var codeSousCategorieEtablissement = 109;
    var codePostal = 75014;
    var finess = "750000507";
    String nom = "HOPITAL SAINTE MARIE PARIS";
    String typeDeLieu =
        "Dans un établissement de santé (hôpital, clinique, laboratoire, pharmacie ...)";
    var motifs =
        List.of("Problème comportemental, relationnel ou de communication avec une personne");
    var etablissement =
        new Etablissement(finess, codeSousCategorieEtablissement, codePostal, nom, typeDeLieu);
    String libelleDuMisEnCauseProvenantDuFormulaire = "Un membre de la famille";
    var dossierReclamation =
        new DossierDeReclamation(
            numeroDossier, etablissement, libelleDuMisEnCauseProvenantDuFormulaire, true, motifs);
    when(dematSocial.recupererDossier(numeroDossier)).thenReturn(dossierReclamation);
    when(referentielDesAutoritesCompetentesParMisEnCauseEnEtablissement.recupererAutoriteCompetente(
            libelleDuMisEnCauseProvenantDuFormulaire))
        .thenReturn("CD");
    when(referentielDesAutoritesCompetentesParLieuDeSurvenue.recupererAutoriteCompetente(
            typeDeLieu))
        .thenReturn("ARS");
    when(referentielDesAutoritesCompetentesParMotifs.recupererAutoriteCompetente(motifs.get(0)))
        .thenReturn(null);
    when(referentielDesAutoritesCompetentesParCategoriesDEtablissements
            .recupererAutoritesCompetentesParCodeSousCategorieEtablissement(
                codeSousCategorieEtablissement))
        .thenReturn(List.of("ARS"));
    // When
    var reclamationObtenue = deposerReclamation.executer(numeroDossier);
    // Then
    var reclamationAttendue =
        new Reclamation(
            dossierReclamation,
            Set.of(AutoriteCompetente.CD, AutoriteCompetente.ARS),
            etablissement);
    assertThat(reclamationObtenue).usingRecursiveComparison().isEqualTo(reclamationAttendue);
  }

  @Test
  void
      deposerUneReclamationAyantEuLieuADomicileAvecUnMembreDeLaFamillePourMisEnCause_doitRetournerCD()
          throws IOException {
    // Given
    var numeroDossier = 12345;
    var codePostal = 38120;
    String libelleDuMisEnCauseProvenantDuFormulaire = "Un membre de la famille";
    var motifs =
        List.of("Problème comportemental, relationnel ou de communication avec une personne");
    var typeDeLieu =
        "Au domicile (domicile de la victime, domicile d'un membre de la famille, domicile d'un aidant...)";
    var service = "Service Infirmier à Domicile (SIAD)";
    var domicile = new Domicile(codePostal, "l'adresse du domicile", typeDeLieu, service);
    var dossierReclamation =
        new DossierDeReclamation(
            numeroDossier, domicile, libelleDuMisEnCauseProvenantDuFormulaire, true, motifs);
    when(dematSocial.recupererDossier(numeroDossier)).thenReturn(dossierReclamation);
    when(referentielDesAutoritesCompetentesParMisEnCauseADomicile.recupererAutoriteCompetente(
            libelleDuMisEnCauseProvenantDuFormulaire))
        .thenReturn("CD");
    when(referentielDesAutoritesCompetentesParServicesADomicile.recupererAutoriteCompetente(
            service))
        .thenReturn("ARS");

    // When
    var reclamationObtenue = deposerReclamation.executer(numeroDossier);
    // Then
    var reclamationAttendue =
        new Reclamation(dossierReclamation, Set.of(AutoriteCompetente.CD), domicile);
    assertThat(reclamationObtenue).usingRecursiveComparison().isEqualTo(reclamationAttendue);
  }

  @Test
  void
      deposerUneReclamationAyantEuLieuADomicileAvecLeServiceMJPMetUnMisEnCauseInconnu_doitRetournerDDETS()
          throws IOException {
    // Given
    var numeroDossier = 12345;
    var codePostal = 38120;
    String libelleDuMisEnCauseProvenantDuFormulaire = "Inconnu";
    var motifs =
        List.of("Problème comportemental, relationnel ou de communication avec une personne");
    var typeDeLieu =
        "Au domicile (domicile de la victime, domicile d'un membre de la famille, domicile d'un aidant...)";
    var service = "Service Mandataire Judiciaire à la Protection des Majeurs (MJPM)";
    var domicile = new Domicile(codePostal, "l'adresse du domicile", typeDeLieu, service);
    var dossierReclamation =
        new DossierDeReclamation(
            numeroDossier, domicile, libelleDuMisEnCauseProvenantDuFormulaire, true, motifs);
    when(dematSocial.recupererDossier(numeroDossier)).thenReturn(dossierReclamation);
    when(referentielDesAutoritesCompetentesParMisEnCauseADomicile.recupererAutoriteCompetente(
            libelleDuMisEnCauseProvenantDuFormulaire))
        .thenReturn(null);
    when(referentielDesAutoritesCompetentesParServicesADomicile.recupererAutoriteCompetente(
            service))
        .thenReturn("DDETS");

    // When
    var reclamationObtenue = deposerReclamation.executer(numeroDossier);
    // Then
    var reclamationAttendue =
        new Reclamation(dossierReclamation, Set.of(AutoriteCompetente.DDETS), domicile);
    assertThat(reclamationObtenue).usingRecursiveComparison().isEqualTo(reclamationAttendue);
  }

  @Test
  void
      lorsqueLonSouhaiteDeposerUneReclamationMaisQuAucuneAutoriteCompetenteNestIdentifiee_alorsthrowsAutoriteCompetenteNotFoundException()
          throws IOException {
    // Given
    var numeroDossier = 12345;
    var codeSousCategorieEtablissementIntrouvable = 1234567910;
    var codePostal = 94300;
    var finess = "940003858";
    var nom = "EHPAD LE VERGER DE VINCENNES";
    String libelleDuMisEnCauseProvenantDuFormulaire =
        "Un professionnel de santé (médecin, infirmier, aide-soignant, kiné, ostéopathe...)";
    String typeDeLieu =
        "Dans un établissement de santé (hôpital, clinique, laboratoire, pharmacie ...)";
    var etablissement =
        new Etablissement(
            finess, codeSousCategorieEtablissementIntrouvable, codePostal, nom, typeDeLieu);
    var motifs =
        List.of("Problème comportemental, relationnel ou de communication avec une personne");
    var dossierReclamation =
        new DossierDeReclamation(
            numeroDossier, etablissement, libelleDuMisEnCauseProvenantDuFormulaire, true, motifs);
    when(dematSocial.recupererDossier(numeroDossier)).thenReturn(dossierReclamation);
    // When Then
    assertThatThrownBy(() -> deposerReclamation.executer(numeroDossier))
        .isInstanceOf(AutoriteCompetenteNotFoundException.class)
        .hasMessage("Aucune autorité compétente n'a été trouvée pour le dossier : 12345");
  }

  @Test
  void
      lorsqueLonSouhaiteDeposerUneReclamationConcernantUnDossierNexistantPasChezDematSocial_alorsRetourneDematSocialException()
          throws IOException {
    // Given
    var numeroDossier = 12345;
    when(dematSocial.recupererDossier(numeroDossier))
        .thenThrow(new IOException("erreur sur le dossier numero :" + numeroDossier));
    // When Then
    assertThatThrownBy(() -> deposerReclamation.executer(numeroDossier))
        .isInstanceOf(DematSocialException.class)
        .hasMessage("erreur sur le dossier numero :12345");
  }
}
