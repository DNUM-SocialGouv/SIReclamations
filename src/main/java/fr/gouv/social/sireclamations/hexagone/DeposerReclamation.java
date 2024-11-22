package fr.gouv.social.sireclamations.hexagone;

import fr.gouv.social.sireclamations.hexagone.domain.Reclamation;
import fr.gouv.social.sireclamations.hexagone.domain.port.DematSocial;
import fr.gouv.social.sireclamations.hexagone.domain.port.ReferentielDeCategoriesDEtablissements;
import fr.gouv.social.sireclamations.hexagone.domain.port.ReferentielDesContacts;
import fr.gouv.social.sireclamations.server_side.EmailService;
import fr.gouv.social.sireclamations.server_side.exceptions.AutoriteCompetenteNotFoundException;
import fr.gouv.social.sireclamations.server_side.exceptions.ContactNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DeposerReclamation {

    private final DematSocial dematSocial;
    private final ReferentielDeCategoriesDEtablissements referentielDeCategoriesDEtablissements;
    private final ReferentielDesContacts referentielDesContacts;
    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(DeposerReclamation.class);


    public DeposerReclamation(DematSocial dematSocial,
                              ReferentielDeCategoriesDEtablissements referentielDeCategoriesDEtablissements,
                              ReferentielDesContacts referentielDesContacts, EmailService emailService) {
        this.dematSocial = dematSocial;
        this.referentielDeCategoriesDEtablissements = referentielDeCategoriesDEtablissements;
        this.referentielDesContacts = referentielDesContacts;
        this.emailService = emailService;
    }

    public Reclamation executer(int numeroDossier) throws AutoriteCompetenteNotFoundException, ContactNotFoundException{
        var dossierReclamation = dematSocial.recupererDossier(numeroDossier);
        var autoritesCompetentes = referentielDeCategoriesDEtablissements.recupererAutoritesCompetentesParCodeSousCategorieEtablissement(
                dossierReclamation.getCodeSousCategorieEtablissement());
        var contactsEmail = referentielDesContacts.recupererContacts(dossierReclamation.getNumeroFinessEtablissement(),
                autoritesCompetentes);

        emailService.envoyer(contactsEmail, "vous êtes les autorités responsables.");
        logger.info("email envoyé");
        return new Reclamation(
                dossierReclamation,
                autoritesCompetentes,
                contactsEmail);
    }
}
