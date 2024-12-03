package fr.gouv.social.sireclamations.hexagone.domain;

import fr.gouv.social.sireclamations.hexagone.DeposerReclamation;
import fr.gouv.social.sireclamations.server_side.exceptions.AutoriteCompetenteNotFoundException;
import fr.gouv.social.sireclamations.server_side.exceptions.ContactNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Reclamation {
    private final int numeroDossier;
    private final int codeSousCategorieEtablissement;
    private final List<String> autoritesCompetentes;
    private final List<String> contacts;

    private static final Logger logger = LoggerFactory.getLogger(Reclamation.class);

    public Reclamation(DossierDeReclamation dossierDeReclamation, List<String> autoritesCompetentes, List<String> contacts) {

        var codeSousCategorieEtablissement = dossierDeReclamation.getEtablissement().getCodeSousCategorie();
        if (autoritesCompetentes.isEmpty()) {
            var messageErreur = "Aucune autorité compétente trouvée pour le code sous-catégorie d'établissement : " + codeSousCategorieEtablissement;
            logger.info(messageErreur);
            throw new AutoriteCompetenteNotFoundException(messageErreur);
        }
        if (contacts.isEmpty()) {
            var messageErreur = "Aucun contact n'a été trouvé. Autorité(s) compétente(s) : " +
                    String.join(", ", autoritesCompetentes) + ",  Code sous-catégorie d'établissement : " +
                    codeSousCategorieEtablissement;
            logger.info(messageErreur);
            throw new ContactNotFoundException(messageErreur);
        }

        this.numeroDossier = dossierDeReclamation.getNumeroDossier();
        this.codeSousCategorieEtablissement = codeSousCategorieEtablissement;
        this.autoritesCompetentes = autoritesCompetentes;
        this.contacts = contacts;
    }

    public int getNumeroDossier() {
        return numeroDossier;
    }

    public int getCodeSousCategorieEtablissement() {
        return codeSousCategorieEtablissement;
    }

    public List<String> getAutoritesCompetentes() {
        return autoritesCompetentes;
    }

    public List<String> getContacts() {
        return contacts;
    }
}
