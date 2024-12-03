package fr.gouv.social.sireclamations.hexagone.domain.port;

import fr.gouv.social.sireclamations.hexagone.domain.DossierDeReclamation;

import java.io.IOException;

public interface DematSocial {
    DossierDeReclamation recupererDossier(int numeroDossier) throws IOException;
}
