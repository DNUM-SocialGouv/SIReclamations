package fr.gouv.social.sireclamations.server_side;

import fr.gouv.social.sireclamations.hexagone.domain.DossierDeReclamation;
import fr.gouv.social.sireclamations.hexagone.domain.port.DematSocial;
import org.springframework.stereotype.Component;

@Component
public class DematSocialAdapter implements DematSocial {
    @Override
    public DossierDeReclamation recupererDossier(String numeroDossier) {
        return null;
    }
}
