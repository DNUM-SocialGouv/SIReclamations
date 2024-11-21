package fr.gouv.social.sireclamations.hexagone.useCase;

import fr.gouv.social.sireclamations.hexagone.domain.Reclamation;
import fr.gouv.social.sireclamations.hexagone.port.DematSocialPort;
import fr.gouv.social.sireclamations.hexagone.port.ReclamationPort;
import org.springframework.stereotype.Component;

@Component
public class DeposerReclamation {

    private final DematSocialPort dematSocial;
    private final ReclamationPort reclamation;

    public DeposerReclamation(DematSocialPort dematSocial, ReclamationPort reclamation) {
        this.dematSocial = dematSocial;
        this.reclamation = reclamation;
    }
    public Reclamation executer(String numeroDossier){
        var dossierDeReclamation = dematSocial.recupererDossier(numeroDossier);
        return reclamation.deposerReclamation(dossierDeReclamation);
    }
}
