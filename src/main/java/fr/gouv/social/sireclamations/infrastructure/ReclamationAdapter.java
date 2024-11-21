package fr.gouv.social.sireclamations.infrastructure;

import fr.gouv.social.sireclamations.hexagone.domain.DossierReclamation;
import fr.gouv.social.sireclamations.hexagone.domain.Reclamation;
import fr.gouv.social.sireclamations.hexagone.port.ReclamationPort;
import fr.gouv.social.sireclamations.infrastructure.exceptions.AutoriteCompetenteNotFoundException;
import fr.gouv.social.sireclamations.infrastructure.exceptions.ContactNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ReclamationAdapter implements ReclamationPort {

    private final CategorieEtablissementRepository categorieEtablissementRepository;
    private final ContactsRepository contactsRepository;
    private final EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(ReclamationAdapter.class);

    public ReclamationAdapter(CategorieEtablissementRepository categorieEtablissementRepository,
                              ContactsRepository contactsRepository,
                              EmailService emailService) {
        this.categorieEtablissementRepository = categorieEtablissementRepository;
        this.contactsRepository = contactsRepository;
        this.emailService = emailService;
    }

    @Override
    public Reclamation deposerReclamation(DossierReclamation dossierReclamation) {
        var autoriteCompetente = categorieEtablissementRepository.recupererAutoriteCompetenteParCodeSousCategorieEtablissement(
                dossierReclamation.getCodeSousCategorieEtablissement());
        var contactsEmail = contactsRepository.recupererContacts(dossierReclamation.getCodePostal(),
                autoriteCompetente);

        if (autoriteCompetente == null || autoriteCompetente.isEmpty()) {
            var messageErreur = "Aucune autorité compétente trouvée pour le code sous-catégorie d'établissement : " +
                    dossierReclamation.getCodeSousCategorieEtablissement();
            logger.error(messageErreur);
            throw new AutoriteCompetenteNotFoundException(messageErreur);
        }
        if (contactsEmail == null || contactsEmail.isEmpty()) {
            var messageErreur = "Aucun contact n'a été trouvé. Autorité(s) compétente(s) : " +
                    String.join(", ", autoriteCompetente) + ",  Code sous-catégorie d'établissement : " +
                    dossierReclamation.getCodeSousCategorieEtablissement();
            logger.error(messageErreur);
            throw new ContactNotFoundException(messageErreur);
        }
        emailService.envoyer(contactsEmail, "vous êtes les autorités responsables !");
        return new Reclamation(dossierReclamation.getNumeroDossier(),
                dossierReclamation.getCodeSousCategorieEtablissement(),
                autoriteCompetente,
                contactsEmail);
    }
}
