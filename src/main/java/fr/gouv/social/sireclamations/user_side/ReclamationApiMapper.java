package fr.gouv.social.sireclamations.user_side;

import fr.gouv.social.sireclamations.hexagone.domain.AutreEtablissement;
import fr.gouv.social.sireclamations.hexagone.domain.Domicile;
import fr.gouv.social.sireclamations.hexagone.domain.DossierDeReclamation;
import fr.gouv.social.sireclamations.hexagone.domain.Etablissement;
import fr.gouv.social.sireclamations.hexagone.domain.LieuDeSurvenue;
import fr.gouv.social.sireclamations.hexagone.domain.Reclamation;
import fr.gouv.social.sireclamations.hexagone.domain.Trajet;
import fr.gouv.social.sireclamations.user_side.DossierDeReclamationDeLaPlateformeTelephoniqueApi.LieuSurvenueApi;
import fr.gouv.social.sireclamations.user_side.DossierDeReclamationDeLaPlateformeTelephoniqueApi.MisEnCauseApi;

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
            dossierDeReclamationDeLaPlateformeTelephoniqueApi.getLieuSurvenue());
    var libelleMisEnCause =
        recupererLibelleMisEnCause(
            dossierDeReclamationDeLaPlateformeTelephoniqueApi.getLieuSurvenue(),
            dossierDeReclamationDeLaPlateformeTelephoniqueApi.getMisEnCause());
    return new DossierDeReclamation(
        Integer.parseInt(dossierDeReclamationDeLaPlateformeTelephoniqueApi.getId()),
        lieuDeSurvenu,
        libelleMisEnCause,
        dossierDeReclamationDeLaPlateformeTelephoniqueApi.getMaltraitance(),
        dossierDeReclamationDeLaPlateformeTelephoniqueApi.getDescription().getTypesDeFaits());
  }

  private static String recupererLibelleMisEnCause(
      LieuSurvenueApi lieuSurvenue, MisEnCauseApi misEnCause) {
    if (lieuSurvenue.getDomicile() != null) {
      var domicile = lieuSurvenue.getDomicile();
      return domicile.getServiceADomicile() != null
          ? domicile.getServiceADomicile()
          : misEnCause.getTypeDeMisEnCause();
    } else if (lieuSurvenue.getEtablissementSanitaireEtSocial() != null) {
      var etablissement = lieuSurvenue.getEtablissementSanitaireEtSocial();
      return etablissement.getTypeDeMisEnCause() != null
          ? etablissement.getTypeDeMisEnCause()
          : misEnCause.getTypeDeMisEnCause();
    } else if (lieuSurvenue.getCabinetMedical() != null) {
      var cabinetMedical = lieuSurvenue.getCabinetMedical();
      return cabinetMedical.getTypeDeMisEnCause() != null
          ? cabinetMedical.getTypeDeMisEnCause()
          : misEnCause.getTypeDeMisEnCause();
    } else if (lieuSurvenue.getTrajet() != null) {
      var trajet = lieuSurvenue.getTrajet();
      return trajet.getTypeDeMisEnCause() != null
          ? trajet.getTypeDeMisEnCause()
          : misEnCause.getTypeDeMisEnCause();
    }
    return null;
  }

  private static LieuDeSurvenue recupererLieuDeSurvenue(LieuSurvenueApi lieuSurvenue) {
    if (lieuSurvenue.getDomicile() != null) {
      var domicile = lieuSurvenue.getDomicile();
      return new Domicile(
          Integer.parseInt(lieuSurvenue.getCodePostal()),
          domicile.getAdresse(),
          lieuSurvenue.getNatureLieu(),
          domicile.getServiceADomicile());
    } else if (lieuSurvenue.getEtablissementSanitaireEtSocial() != null) {
      var etablissement = lieuSurvenue.getEtablissementSanitaireEtSocial();
      return new Etablissement(
          etablissement.getEtFiness(),
          Integer.parseInt(etablissement.getCodeCategorieEtablissement()),
          Integer.parseInt(lieuSurvenue.getCodePostal()),
          etablissement.getNomEtablissement(),
          lieuSurvenue.getNatureLieu());

    } else if (lieuSurvenue.getCabinetMedical() != null) {
      var cabinetMedical = lieuSurvenue.getCabinetMedical();
      return new AutreEtablissement(
          Integer.parseInt(lieuSurvenue.getCodePostal()),
          cabinetMedical.getAdresse(),
          lieuSurvenue.getNatureLieu());
    } else if (lieuSurvenue.getTrajet() != null) {
      return new Trajet(
          Integer.parseInt(lieuSurvenue.getCodePostal()), lieuSurvenue.getNatureLieu());
    }
    return null;
  }
}
