package fr.gouv.social.sireclamations.hexagone.domain;

import fr.gouv.social.sireclamations.hexagone.DeposerReclamation;
import fr.gouv.social.sireclamations.server_side.exceptions.AutoriteCompetenteNotFoundException;
import fr.gouv.social.sireclamations.server_side.exceptions.ContactNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Reclamation {
    private final String numeroDossier;
    private final String codeSousCategorieEtablissement;
    private final List<String> autoritesCompetentes;
    private final List<String> contacts;

    private static final Logger logger = LoggerFactory.getLogger(DeposerReclamation.class);
    public Reclamation(DossierDeReclamation dossierDeReclamation, List<String> autoritesCompetentes, List<String> contacts) {

        if (autoritesCompetentes.isEmpty()) {
            var messageErreur = "Aucune autorité compétente trouvée pour le code sous-catégorie d'établissement : " +
                    dossierDeReclamation.getCodeSousCategorieEtablissement();
            logger.error(messageErreur);
            throw new AutoriteCompetenteNotFoundException(messageErreur);
        }
        if (contacts.isEmpty()) {
            var messageErreur = "Aucun contact n'a été trouvé. Autorité(s) compétente(s) : " +
                    String.join(", ", autoritesCompetentes) + ",  Code sous-catégorie d'établissement : " +
                    dossierDeReclamation.getCodeSousCategorieEtablissement();
            logger.error(messageErreur);
            throw new ContactNotFoundException(messageErreur);
        }

        this.numeroDossier = dossierDeReclamation.getNumeroDossier();
        this.codeSousCategorieEtablissement = dossierDeReclamation.getCodeSousCategorieEtablissement();
        this.autoritesCompetentes = autoritesCompetentes;
        this.contacts = contacts;
    }

    public String getNumeroDossier() {
        return numeroDossier;
    }

    public String getCodeSousCategorieEtablissement() {
        return codeSousCategorieEtablissement;
    }

    public List<String> getAutoritesCompetentes() {
        return autoritesCompetentes;
    }

    public List<String> getContacts() {
        return contacts;
    }
}
