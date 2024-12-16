package fr.gouv.social.sireclamations.user_side;

import fr.gouv.social.sireclamations.hexagone.Domicile;
import fr.gouv.social.sireclamations.hexagone.domain.Etablissement;
import fr.gouv.social.sireclamations.hexagone.domain.LieuDeSurvenue;
import fr.gouv.social.sireclamations.hexagone.domain.Reclamation;

public class ReclamationApiMapper {
    public static ReclamationApiResponse toReclamationApiResponse(Reclamation reclamation) {
        LieuDeSurvenue lieu = reclamation.getLieuDeSurvenue();
        LieuDeSurvenueApiResponse lieuDeSurvenueResponse = null;

        if (lieu instanceof Domicile domicile) {
            lieuDeSurvenueResponse = new LieuDeSurvenueApiResponse(
                    domicile.getCodePostal(),
                    domicile.getCodeTypeDeLieu().name(),
                    domicile.getAdresse(),
                    null,
                    null,
                    null
            );
        } else if (lieu instanceof Etablissement etablissement) {
            lieuDeSurvenueResponse = new LieuDeSurvenueApiResponse(
                    etablissement.getCodePostal(),
                    etablissement.getCodeTypeDeLieu().name(),
                    null,
                    etablissement.getNumeroFiness(),
                    etablissement.getCodeSousCategorie(),
                    etablissement.getNom()
            );
        }

        return new ReclamationApiResponse(
                reclamation.getNumeroDossier(),
                reclamation.getAutoritesCompetentes(),
                reclamation.getContacts(),
                lieuDeSurvenueResponse
                );
    }
}
