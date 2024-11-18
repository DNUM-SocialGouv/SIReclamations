package fr.gouv.social.sireclamations.hexagone.useCase;

import fr.gouv.social.sireclamations.hexagone.port.DematSocialPort;
import fr.gouv.social.sireclamations.hexagone.port.ReclamationPort;

public class DeposerReclamation {

    private final DematSocialPort dematSocial;
    private final ReclamationPort reclamationRepository;

    public DeposerReclamation(DematSocialPort dematSocial, ReclamationPort reclamation) {
        this.dematSocial = dematSocial;
        this.reclamationRepository = reclamation;
    }
    public void executer(String numeroDossier){
        var dossierDeReclamation = dematSocial.recupererDossier(numeroDossier);
        reclamationRepository.deposerReclamation(dossierDeReclamation);

    }
}
