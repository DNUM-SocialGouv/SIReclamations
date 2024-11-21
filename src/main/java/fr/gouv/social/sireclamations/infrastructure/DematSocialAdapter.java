package fr.gouv.social.sireclamations.infrastructure;

import fr.gouv.social.sireclamations.hexagone.domain.DossierReclamation;
import fr.gouv.social.sireclamations.hexagone.port.DematSocialPort;
import org.springframework.stereotype.Component;

@Component
public class DematSocialAdapter implements DematSocialPort {
    @Override
    public DossierReclamation recupererDossier(String numeroDossier) {
        return null;
    }
}
