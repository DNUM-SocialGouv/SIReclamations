package fr.gouv.social.sireclamations.application;

import fr.gouv.social.sireclamations.hexagone.domain.Reclamation;

public class ReclamationApiMapper {
    public static ReclamationApiResponse toReclamationApiResponse(Reclamation reclamation) {
        return new ReclamationApiResponse(
                reclamation.getNumeroDossier(),
                reclamation.getCodeSousCategorieEtablissement(),
                reclamation.getAutoritesCompetentes(),
                reclamation.getContacts()
                );
    }
}
