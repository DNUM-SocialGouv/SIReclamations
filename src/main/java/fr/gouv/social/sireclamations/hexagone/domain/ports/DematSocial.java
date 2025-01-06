package fr.gouv.social.sireclamations.hexagone.domain.ports;

import fr.gouv.social.sireclamations.hexagone.domain.DossierDeReclamation;
import java.io.IOException;

public interface DematSocial {
  DossierDeReclamation recupererDossier(int numeroDossier) throws IOException;
}
