package fr.gouv.social.sireclamations.hexagone.port;

import fr.gouv.social.sireclamations.hexagone.domain.DossierReclamation;

public interface ReclamationPort {
    void deposerReclamation(DossierReclamation dossierReclamation);
}
