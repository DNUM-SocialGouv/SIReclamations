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

    public Reclamation(DossierDeReclamation dossierDeReclamation, List<String> autoritesCompetentes, List<String> contacts) {

        if (autoritesCompetentes.isEmpty()) {
            throw new AutoriteCompetenteNotFoundException("Aucune autorité compétente trouvée pour le code sous-catégorie d'établissement : " +
                    dossierDeReclamation.getCodeSousCategorieEtablissement());
        }
        if (contacts.isEmpty()) {
            throw new ContactNotFoundException("Aucun contact n'a été trouvé. Autorité(s) compétente(s) : " +
                    String.join(", ", autoritesCompetentes) + ",  Code sous-catégorie d'établissement : " +
                    dossierDeReclamation.getCodeSousCategorieEtablissement());
        }

        this.numeroDossier = dossierDeReclamation.getNumeroDossier();
        this.codeSousCategorieEtablissement = dossierDeReclamation.getCodeSousCategorieEtablissement();
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
