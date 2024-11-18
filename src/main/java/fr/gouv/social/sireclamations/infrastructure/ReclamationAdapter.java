package fr.gouv.social.sireclamations.infrastructure;

import fr.gouv.social.sireclamations.hexagone.domain.DossierReclamation;
import fr.gouv.social.sireclamations.hexagone.port.ReclamationPort;
import fr.gouv.social.sireclamations.infrastructure.exceptions.AutoriteCompetenteNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReclamationAdapter implements ReclamationPort {

    private final CategorieEtablissementRepository categorieEtablissementRepository;
    private final ContactsRepository contactsRepository;
    private final EmailService emailService;

    @Autowired
    public ReclamationAdapter(CategorieEtablissementRepository categorieEtablissementRepository,
                              ContactsRepository contactsRepository,
                              EmailService emailService) {
        this.categorieEtablissementRepository = categorieEtablissementRepository;
        this.contactsRepository = contactsRepository;
        this.emailService = emailService;
    }

    @Override
    public void deposerReclamation(DossierReclamation dossierReclamation) {
        var autoriteCompetente = categorieEtablissementRepository.recupererAutoriteCompetenteParCodeSousCategorieEtablissement(
                dossierReclamation.getCodeSousCategorieEtablissement());
        var contactsEmail = contactsRepository.recupererContactsParCodePostal(dossierReclamation.getEtablissement().getCodePostal(), autoriteCompetente);

        if (autoriteCompetente == null) {
            throw new AutoriteCompetenteNotFoundException("Aucune autorité compétente trouvée pour le code sous-catégorie d'établissement : " + dossierReclamation.getCodeSousCategorieEtablissement());
        }
        emailService.envoyer(contactsEmail, "vous êtes les autorités responsables !");
    }
}
