package fr.gouv.social.sireclamations.hexagone;

import fr.gouv.social.sireclamations.hexagone.domain.DossierDeReclamation;
import fr.gouv.social.sireclamations.hexagone.domain.Reclamation;
import fr.gouv.social.sireclamations.hexagone.domain.port.*;
import fr.gouv.social.sireclamations.server_side.EmailService;
import fr.gouv.social.sireclamations.server_side.exceptions.AutoriteCompetenteNotFoundException;
import fr.gouv.social.sireclamations.server_side.exceptions.ContactNotFoundException;
import fr.gouv.social.sireclamations.hexagone.domain.exceptions.DematSocialException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class DeposerReclamation {

    private final DematSocial dematSocial;
    private final ReferentielDeCategoriesDEtablissements referentielDeCategoriesDEtablissements;
    private final ReferentielDesContacts referentielDesContacts;
    private final ReferentielDesTypeDeMisEnCause referentielDesTypeDeMisEnCause;
    private final ReferentielDesAutoritesCompetentesParTypeDeMisEnCause referentielDesAutoritesCompetentesParTypeDeMisEnCause;
    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(DeposerReclamation.class);


    public DeposerReclamation(DematSocial dematSocial,
                              ReferentielDeCategoriesDEtablissements referentielDeCategoriesDEtablissements,
                              ReferentielDesContacts referentielDesContacts,
                              ReferentielDesTypeDeMisEnCause referentielDesTypeDeMisEnCause,
                              ReferentielDesAutoritesCompetentesParTypeDeMisEnCause referentielDesAutoritesCompetentesParTypeDeMisEnCause,
                              EmailService emailService) {
        this.dematSocial = dematSocial;
        this.referentielDeCategoriesDEtablissements = referentielDeCategoriesDEtablissements;
        this.referentielDesContacts = referentielDesContacts;
        this.referentielDesTypeDeMisEnCause = referentielDesTypeDeMisEnCause;
        this.referentielDesAutoritesCompetentesParTypeDeMisEnCause = referentielDesAutoritesCompetentesParTypeDeMisEnCause;
        this.emailService = emailService;
    }

    public Reclamation executer(int numeroDossier) throws AutoriteCompetenteNotFoundException, ContactNotFoundException, DematSocialException{
        DossierDeReclamation dossierReclamation;
        try {
            dossierReclamation = dematSocial.recupererDossier(numeroDossier);
        } catch (IOException e) {
            logger.error("Erreur lors de la récupération du dossier chez demat social : " + e.getMessage(), e);
            throw new DematSocialException(e.getMessage());
        }
        var autoritesCompetentes = new ArrayList<>(referentielDeCategoriesDEtablissements.recupererAutoritesCompetentesParCodeSousCategorieEtablissement(
                dossierReclamation.getEtablissement().getCodeSousCategorie()));
        var codeTypeDuMisEnCause = referentielDesTypeDeMisEnCause.recupererTypeDuMisEnCause(dossierReclamation.getLibelleDuMisEnCause());
        var autoriteCompetentePourLeMisEnCause = referentielDesAutoritesCompetentesParTypeDeMisEnCause.recupererAutoriteCompetentePourUnTypeDeMisEnCause(codeTypeDuMisEnCause);
        if (autoriteCompetentePourLeMisEnCause != null && !autoritesCompetentes.contains(autoriteCompetentePourLeMisEnCause)){
            autoritesCompetentes.add(autoriteCompetentePourLeMisEnCause);
        }
        var contactsEmail = referentielDesContacts.recupererContacts(dossierReclamation.getEtablissement().getNumeroFiness(),
                autoritesCompetentes);

        emailService.envoyer(contactsEmail, "vous êtes les autorités responsables.");
        logger.info("email envoyé");
        return new Reclamation(
                dossierReclamation,
                autoritesCompetentes,
                contactsEmail);
    }
}
