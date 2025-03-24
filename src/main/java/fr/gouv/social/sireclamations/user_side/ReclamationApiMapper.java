package fr.gouv.social.sireclamations.user_side;

import fr.gouv.social.sireclamations.hexagone.domain.AutreEtablissement;
import fr.gouv.social.sireclamations.hexagone.domain.Domicile;
import fr.gouv.social.sireclamations.hexagone.domain.DossierDeReclamation;
import fr.gouv.social.sireclamations.hexagone.domain.Etablissement;
import fr.gouv.social.sireclamations.hexagone.domain.LieuDeSurvenue;
import fr.gouv.social.sireclamations.hexagone.domain.Reclamation;
import fr.gouv.social.sireclamations.hexagone.domain.Trajet;
import fr.gouv.social.sireclamations.user_side.plateforme_telephonique.DossierDeReclamationDeLaPlateformeTelephoniqueApi;
import fr.gouv.social.sireclamations.user_side.plateforme_telephonique.LieuDeSurvenueApi;
import fr.gouv.social.sireclamations.user_side.plateforme_telephonique.MisEnCauseApi;

public class ReclamationApiMapper {
  public static ReclamationApiResponse toReclamationApiResponse(Reclamation reclamation) {
    LieuDeSurvenue lieu = reclamation.getLieuDeSurvenue();
    LieuDeSurvenueApiResponse lieuDeSurvenueResponse = null;

    if (lieu instanceof Domicile domicile) {
      lieuDeSurvenueResponse =
          new LieuDeSurvenueApiResponse(
              domicile.getCodePostal(),
              domicile.getCodeTypeDeLieu().name(),
              domicile.getAdresse(),
              null,
              null,
              null);
    } else if (lieu instanceof Etablissement etablissement) {
      lieuDeSurvenueResponse =
          new LieuDeSurvenueApiResponse(
              etablissement.getCodePostal(),
              etablissement.getCodeTypeDeLieu().name(),
              null,
              etablissement.getNumeroFiness(),
              etablissement.getCodeSousCategorie(),
              etablissement.getNom());
    }

    return new ReclamationApiResponse(
        reclamation.getNumeroDossier(),
        reclamation.getAutoritesCompetentes(),
        lieuDeSurvenueResponse);
  }

  public static DossierDeReclamation toDossierDeReclamation(
      DossierDeReclamationDeLaPlateformeTelephoniqueApi
          dossierDeReclamationDeLaPlateformeTelephoniqueApi) {
    var lieuDeSurvenu =
        recupererLieuDeSurvenue(
            dossierDeReclamationDeLaPlateformeTelephoniqueApi.getLieuDeSurvenueApi());
    var libelleMisEnCause =
        recupererLibelleMisEnCause(
            dossierDeReclamationDeLaPlateformeTelephoniqueApi.getLieuDeSurvenueApi(),
            dossierDeReclamationDeLaPlateformeTelephoniqueApi.getMisEnCause());
    return new DossierDeReclamation(
        Integer.parseInt(dossierDeReclamationDeLaPlateformeTelephoniqueApi.getId()),
        lieuDeSurvenu,
        libelleMisEnCause,
        dossierDeReclamationDeLaPlateformeTelephoniqueApi.getMaltraitance(),
        dossierDeReclamationDeLaPlateformeTelephoniqueApi.getDescription().getTypesDeFaits());
  }

  private static String recupererLibelleMisEnCause(
      LieuDeSurvenueApi lieuSurvenue, MisEnCauseApi misEnCause) {
    if (lieuSurvenue.getDomicileApi() != null) {
      var domicile = lieuSurvenue.getDomicileApi();
      return domicile.getServiceADomicile() != null
          ? domicile.getServiceADomicile().getDescription()
          : misEnCause.getTypeDePersonneMisEnCause().getDescription();
    } else if (lieuSurvenue.getEtablissementSanitaireEtSocialApi() != null) {
      var etablissement = lieuSurvenue.getEtablissementSanitaireEtSocialApi();
      return etablissement.getTypeDeMisEnCause() != null
          ? etablissement.getTypeDeMisEnCause().getDescription()
          : misEnCause.getTypeDePersonneMisEnCause().getDescription();
    } else if (lieuSurvenue.getCabinetMedicalApi() != null) {
      var cabinetMedical = lieuSurvenue.getCabinetMedicalApi();
      return cabinetMedical.getTypeDeMisEnCause() != null
          ? cabinetMedical.getTypeDeMisEnCause().getDescription()
          : misEnCause.getTypeDePersonneMisEnCause().getDescription();
    } else if (lieuSurvenue.getTrajetApi() != null) {
      var trajet = lieuSurvenue.getTrajetApi();
      return trajet.getTypeDeMisEnCause() != null
          ? trajet.getTypeDeMisEnCause().getDescription()
          : misEnCause.getTypeDePersonneMisEnCause().getDescription();
    }
    return null;
  }

  private static LieuDeSurvenue recupererLieuDeSurvenue(LieuDeSurvenueApi lieuSurvenue) {
    if (lieuSurvenue.getDomicileApi() != null) {
      var domicile = lieuSurvenue.getDomicileApi();
      return new Domicile(
          Integer.parseInt(lieuSurvenue.getCodePostal()),
          domicile.getAdresse(),
          lieuSurvenue.getNatureLieu(),
          domicile.getServiceADomicile() != null
              ? domicile.getServiceADomicile().getDescription()
              : null);
    } else if (lieuSurvenue.getEtablissementSanitaireEtSocialApi() != null) {
      var etablissement = lieuSurvenue.getEtablissementSanitaireEtSocialApi();
      return new Etablissement(
          etablissement.getEtFiness(),
          Integer.parseInt(etablissement.getCodeCategorieEtablissement()),
          Integer.parseInt(lieuSurvenue.getCodePostal()),
          etablissement.getNomEtablissement(),
          lieuSurvenue.getNatureLieu());

    } else if (lieuSurvenue.getCabinetMedicalApi() != null) {
      var cabinetMedical = lieuSurvenue.getCabinetMedicalApi();
      return new AutreEtablissement(
          Integer.parseInt(lieuSurvenue.getCodePostal()),
          cabinetMedical.getAdresse(),
          lieuSurvenue.getNatureLieu());
    } else if (lieuSurvenue.getTrajetApi() != null) {
      return new Trajet(
          Integer.parseInt(lieuSurvenue.getCodePostal()), lieuSurvenue.getNatureLieu());
    }
    return null;
  }
}
