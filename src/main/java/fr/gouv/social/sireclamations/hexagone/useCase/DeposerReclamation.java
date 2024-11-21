package fr.gouv.social.sireclamations.hexagone.useCase;

import fr.gouv.social.sireclamations.hexagone.domain.Reclamation;
import fr.gouv.social.sireclamations.hexagone.port.DematSocialPort;
import fr.gouv.social.sireclamations.hexagone.port.CategorieEtablissementPort;
import fr.gouv.social.sireclamations.hexagone.port.ContactsPort;
import fr.gouv.social.sireclamations.infrastructure.EmailService;
import fr.gouv.social.sireclamations.infrastructure.exceptions.AutoriteCompetenteNotFoundException;
import fr.gouv.social.sireclamations.infrastructure.exceptions.ContactNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DeposerReclamation {

    private final DematSocialPort dematSocial;
    private final CategorieEtablissementPort categorieEtablissementPort;
    private final ContactsPort contactsPort;
    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(DeposerReclamation.class);


    public DeposerReclamation(DematSocialPort dematSocial,
                              CategorieEtablissementPort categorieEtablissementPort,
                              ContactsPort contactsPort, EmailService emailService) {
        this.dematSocial = dematSocial;
        this.categorieEtablissementPort = categorieEtablissementPort;
        this.contactsPort = contactsPort;
        this.emailService = emailService;
    }

    public Reclamation executer(String numeroDossier){
        var dossierReclamation = dematSocial.recupererDossier(numeroDossier);
        var autoriteCompetente = categorieEtablissementPort.recupererAutoriteCompetenteParCodeSousCategorieEtablissement(
                dossierReclamation.getEtablissement().getCodeSousCategorieEtablissement());
        var contactsEmail = contactsPort.recupererContacts(dossierReclamation.getEtablissement().getNumeroFiness(),
                autoriteCompetente);

        if (autoriteCompetente == null || autoriteCompetente.isEmpty()) {
            var messageErreur = "Aucune autorité compétente trouvée pour le code sous-catégorie d'établissement : " +
                    dossierReclamation.getEtablissement().getCodeSousCategorieEtablissement();
            logger.error(messageErreur);
            throw new AutoriteCompetenteNotFoundException(messageErreur);
        }
        if (contactsEmail == null || contactsEmail.isEmpty()) {
            var messageErreur = "Aucun contact n'a été trouvé. Autorité(s) compétente(s) : " +
                    String.join(", ", autoriteCompetente) + ",  Code sous-catégorie d'établissement : " +
                    dossierReclamation.getEtablissement().getCodeSousCategorieEtablissement();
            logger.error(messageErreur);
            throw new ContactNotFoundException(messageErreur);
        }
        emailService.envoyer(contactsEmail, "vous êtes les autorités responsables !");
        return new Reclamation(dossierReclamation.getNumeroDossier(),
                dossierReclamation.getEtablissement().getCodeSousCategorieEtablissement(),
                autoriteCompetente,
                contactsEmail);
    }
}
