package fr.gouv.social.sireclamations.hexagone.port;

import fr.gouv.social.sireclamations.hexagone.domain.DossierReclamation;
import fr.gouv.social.sireclamations.hexagone.domain.Reclamation;

public interface ReclamationPort {
    Reclamation deposerReclamation(DossierReclamation dossierReclamation);
}
