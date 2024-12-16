package fr.gouv.social.sireclamations.hexagone.domain;

import fr.gouv.social.sireclamations.hexagone.exceptions.AutoriteCompetenteNotFoundException;
import fr.gouv.social.sireclamations.hexagone.exceptions.ContactNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

public class Reclamation {
    private final int numeroDossier;
    private final LieuDeSurvenue lieuDeSurvenue;
    private final Set<AutoriteCompetente> autoritesCompetentes;
    private final List<String> contacts;

    private static final Logger logger = LoggerFactory.getLogger(Reclamation.class);

    public Reclamation(DossierDeReclamation dossierDeReclamation, Set<AutoriteCompetente> autoritesCompetentes, List<String> contacts, LieuDeSurvenue lieuDeSurvenue) {

        if (autoritesCompetentes.isEmpty()) {
            var messageErreur = "Aucune autorité compétente n'a été trouvée pour le dossier : " + dossierDeReclamation.getNumeroDossier();
            logger.info(messageErreur);
            throw new AutoriteCompetenteNotFoundException(messageErreur);
        }
        if (contacts.isEmpty()) {
            var messageErreur = "Aucun contact n'a été trouvé pour le dossier : " + dossierDeReclamation.getNumeroDossier();
            logger.info(messageErreur);
            throw new ContactNotFoundException(messageErreur);
        }

        this.numeroDossier = dossierDeReclamation.getNumeroDossier();
        this.lieuDeSurvenue = lieuDeSurvenue;
        this.autoritesCompetentes = autoritesCompetentes;
        this.contacts = contacts;
    }

    public int getNumeroDossier() {
        return numeroDossier;
    }

    public LieuDeSurvenue getLieuDeSurvenue() {
        return lieuDeSurvenue;
    }

    public Set<AutoriteCompetente> getAutoritesCompetentes() {
        return autoritesCompetentes;
    }

    public List<String> getContacts() {
        return contacts;
    }
}
