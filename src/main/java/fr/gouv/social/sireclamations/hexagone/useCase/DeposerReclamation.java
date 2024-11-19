package fr.gouv.social.sireclamations.hexagone.useCase;

import fr.gouv.social.sireclamations.hexagone.port.DematSocialPort;
import fr.gouv.social.sireclamations.hexagone.port.ReclamationPort;

public class DeposerReclamation {

    private final DematSocialPort dematSocial;
    private final ReclamationPort reclamation;

    public DeposerReclamation(DematSocialPort dematSocial, ReclamationPort reclamation) {
        this.dematSocial = dematSocial;
        this.reclamation = reclamation;
    }
    public void executer(String numeroDossier){
        var dossierDeReclamation = dematSocial.recupererDossier(numeroDossier);
        reclamation.deposerReclamation(dossierDeReclamation);

    }
}
