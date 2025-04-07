package fr.gouv.social.sireclamations.hexagone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import fr.gouv.social.sireclamations.hexagone.domain.AutoriteCompetente;
import fr.gouv.social.sireclamations.hexagone.domain.Domicile;
import fr.gouv.social.sireclamations.hexagone.domain.DossierDeReclamation;
import fr.gouv.social.sireclamations.hexagone.domain.Etablissement;
import fr.gouv.social.sireclamations.hexagone.domain.ports.ReferentielDesAutoritesCompetentesParCategoriesDEtablissements;
import fr.gouv.social.sireclamations.hexagone.domain.ports.ReferentielDesAutoritesCompetentesParLieuDeSurvenue;
import fr.gouv.social.sireclamations.hexagone.domain.ports.ReferentielDesAutoritesCompetentesParMisEnCauseADomicile;
import fr.gouv.social.sireclamations.hexagone.domain.ports.ReferentielDesAutoritesCompetentesParMisEnCauseEnEtablissement;
import fr.gouv.social.sireclamations.hexagone.domain.ports.ReferentielDesAutoritesCompetentesParMotifs;
import fr.gouv.social.sireclamations.hexagone.domain.ports.ReferentielDesAutoritesCompetentesParServicesADomicile;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AutoriteCompetenteServiceTest {

  @InjectMocks AutoriteCompetenteService autoriteCompetenteService;

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
      duneReclamationPourUneMaltraitanceParUnProfessionnelDeSanteDansUnEtablissementDeSante_doitRetournerARS() {
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
    when(referentielDesAutoritesCompetentesParMisEnCauseEnEtablissement.recupererAutoriteCompetente(
            libelleDuMisEnCauseProvenantDuFormulaire))
        .thenReturn("ARS");
    when(referentielDesAutoritesCompetentesParLieuDeSurvenue.recupererAutoriteCompetente(
            typeDeLieu))
        .thenReturn("ARS");
    // When
    var autoritesObtenue =
        autoriteCompetenteService.recupererAutoritesCompetentes(dossierReclamation);
    // Then
    var autoritesAttendues = Set.of(AutoriteCompetente.ARS);
    assertThat(autoritesObtenue).usingRecursiveComparison().isEqualTo(autoritesAttendues);
  }

  @Test
  void
      uneReclamationPourUneMaltraitanceParUnMembreDeLaFamilleDansUnEtablissementSocialPourUnMotifComportemental_doitRetournerCDetDDETS() {
    // Given
    var numeroDossier = 12345;
    var codeSousCategorieEtablissement = 340;
    var codePostal = 38120;
    var finess = "380022723";
    String nom = "SERVICE DES MAJEURS PROTEGES";
    String typeDeLieu =
        "Dans un établissement ou service social (Centre de jour, service d'aide, service Mandataire Judiciaire à la Protection des Majeurs...)";
    var motifs =
        List.of("Problème comportemental, relationnel ou de communication avec une personne");
    var etablissement =
        new Etablissement(finess, codeSousCategorieEtablissement, codePostal, nom, typeDeLieu);
    String libelleDuMisEnCauseProvenantDuFormulaire = "Membre de la famille";
    var dossierReclamation =
        new DossierDeReclamation(
            numeroDossier, etablissement, libelleDuMisEnCauseProvenantDuFormulaire, true, motifs);
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
    var autoritesObtenue =
        autoriteCompetenteService.recupererAutoritesCompetentes(dossierReclamation);
    // Then
    var autoritesAttendues = Set.of(AutoriteCompetente.CD, AutoriteCompetente.DDETS);
    assertThat(autoritesObtenue).usingRecursiveComparison().isEqualTo(autoritesAttendues);
  }

  @Test
  void
      deposerUneReclamationPourUneMaltraitanceParUnAutreProfessionnelDeLEtablissementDansUnEtablissementSocialPourUnMotifDeQualiteDesSoin_doitRetournerARS() {
    // Given
    var numeroDossier = 12345;
    var codeSousCategorieEtablissement = 340;
    var codePostal = 38120;
    var finess = "380022723";
    String nom = "SERVICE DES MAJEURS PROTEGES";
    String typeDeLieu =
        "Dans un établissement ou service social (Centre de jour, service d'aide, service Mandataire Judiciaire à la Protection des Majeurs...)";
    var motifs =
        List.of(
            "Problème de qualité des soins médicaux ou paramédicaux (ex: soins et/ou interventions inadaptés, absents ou abusifs...)");
    var etablissement =
        new Etablissement(finess, codeSousCategorieEtablissement, codePostal, nom, typeDeLieu);
    String libelleDuMisEnCauseProvenantDuFormulaire = "Un autre professionnel de l'établissement";
    var dossierReclamation =
        new DossierDeReclamation(
            numeroDossier, etablissement, libelleDuMisEnCauseProvenantDuFormulaire, true, motifs);
    when(referentielDesAutoritesCompetentesParMisEnCauseEnEtablissement.recupererAutoriteCompetente(
            libelleDuMisEnCauseProvenantDuFormulaire))
        .thenReturn(null);
    when(referentielDesAutoritesCompetentesParLieuDeSurvenue.recupererAutoriteCompetente(
            typeDeLieu))
        .thenReturn(null);
    when(referentielDesAutoritesCompetentesParMotifs.recupererAutoriteCompetente(motifs.get(0)))
        .thenReturn("ARS");
    // When
    var autoritesObtenue =
        autoriteCompetenteService.recupererAutoritesCompetentes(dossierReclamation);
    // Then
    var autoritesAttendues = Set.of(AutoriteCompetente.ARS);
    assertThat(autoritesObtenue).usingRecursiveComparison().isEqualTo(autoritesAttendues);
  }

  @Test
  void
      uneReclamationPourUneMaltraitanceParUnMembreDeLaFamilleDansUnEtablissementDeSanteMotifComportemental_doitRetournerCDetARS() {
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
    String libelleDuMisEnCauseProvenantDuFormulaire = "Membre de la famille";
    var dossierReclamation =
        new DossierDeReclamation(
            numeroDossier, etablissement, libelleDuMisEnCauseProvenantDuFormulaire, true, motifs);
    when(referentielDesAutoritesCompetentesParMisEnCauseEnEtablissement.recupererAutoriteCompetente(
            libelleDuMisEnCauseProvenantDuFormulaire))
        .thenReturn("CD");
    when(referentielDesAutoritesCompetentesParLieuDeSurvenue.recupererAutoriteCompetente(
            typeDeLieu))
        .thenReturn("ARS");
    when(referentielDesAutoritesCompetentesParMotifs.recupererAutoriteCompetente(motifs.get(0)))
        .thenReturn(null);
    // When
    var autoritesObtenue =
        autoriteCompetenteService.recupererAutoritesCompetentes(dossierReclamation);
    // Then
    var autoritesAttendues = Set.of(AutoriteCompetente.CD, AutoriteCompetente.ARS);
    assertThat(autoritesObtenue).usingRecursiveComparison().isEqualTo(autoritesAttendues);
  }

  @Test
  void uneReclamationAyantEuLieuADomicileAvecUnMembreDeLaFamillePourMisEnCause_doitRetournerCD() {
    // Given
    var numeroDossier = 12345;
    var codePostal = 38120;
    String libelleDuMisEnCauseProvenantDuFormulaire = "Membre de la famille";
    var motifs =
        List.of("Problème comportemental, relationnel ou de communication avec une personne");
    var typeDeLieu =
        "Au domicile (domicile de la victime, domicile d'un membre de la famille, domicile d'un aidant...)";
    var service = "Service Infirmier à Domicile (SIAD)";
    var domicile = new Domicile(codePostal, "l'adresse du domicile", typeDeLieu, service);
    var dossierReclamation =
        new DossierDeReclamation(
            numeroDossier, domicile, libelleDuMisEnCauseProvenantDuFormulaire, true, motifs);
    when(referentielDesAutoritesCompetentesParMisEnCauseADomicile.recupererAutoriteCompetente(
            libelleDuMisEnCauseProvenantDuFormulaire))
        .thenReturn("CD");
    when(referentielDesAutoritesCompetentesParServicesADomicile.recupererAutoriteCompetente(
            service))
        .thenReturn("ARS");

    // When
    var autoritesObtenue =
        autoriteCompetenteService.recupererAutoritesCompetentes(dossierReclamation);
    // Then
    var autoritesAttendues = Set.of(AutoriteCompetente.CD);
    assertThat(autoritesObtenue).usingRecursiveComparison().isEqualTo(autoritesAttendues);
  }

  @Test
  void
      uneReclamationAyantEuLieuADomicileAvecLeServiceMJPMetUnMisEnCauseInconnu_doitRetournerDDETS() {
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
    when(referentielDesAutoritesCompetentesParMisEnCauseADomicile.recupererAutoriteCompetente(
            libelleDuMisEnCauseProvenantDuFormulaire))
        .thenReturn(null);
    when(referentielDesAutoritesCompetentesParServicesADomicile.recupererAutoriteCompetente(
            service))
        .thenReturn("DDETS");

    // When
    var autoritesObtenue =
        autoriteCompetenteService.recupererAutoritesCompetentes(dossierReclamation);
    // Then
    var autoritesAttendues = Set.of(AutoriteCompetente.DDETS);
    assertThat(autoritesObtenue).usingRecursiveComparison().isEqualTo(autoritesAttendues);
  }
}
