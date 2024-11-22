package fr.gouv.social.sireclamations.hexagone.domain.port;

import fr.gouv.social.sireclamations.hexagone.domain.DossierDeReclamation;

public interface DematSocial {
    DossierDeReclamation recupererDossier(int numeroDossier);
}
