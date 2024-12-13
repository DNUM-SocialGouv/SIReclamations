package fr.gouv.social.sireclamations.hexagone;

import fr.gouv.social.sireclamations.hexagone.domain.AutoriteCompetente;
import fr.gouv.social.sireclamations.hexagone.domain.DossierDeReclamation;
import fr.gouv.social.sireclamations.hexagone.domain.Etablissement;
import fr.gouv.social.sireclamations.hexagone.domain.Reclamation;
import fr.gouv.social.sireclamations.hexagone.domain.ports.*;
import fr.gouv.social.sireclamations.server_side.EmailService;
import fr.gouv.social.sireclamations.hexagone.exceptions.AutoriteCompetenteNotFoundException;
import fr.gouv.social.sireclamations.hexagone.exceptions.ContactNotFoundException;
import fr.gouv.social.sireclamations.hexagone.exceptions.DematSocialException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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

        Set<AutoriteCompetente> autoritesCompetentes = new HashSet<>();
        var codeTypeDuMisEnCause = referentielDesTypeDeMisEnCause.recupererTypeDuMisEnCause(dossierReclamation.getLibelleDuMisEnCause());
        var autoriteCompetentePourLeMisEnCause = referentielDesAutoritesCompetentesParTypeDeMisEnCause.recupererAutoriteCompetentePourUnTypeDeMisEnCause(codeTypeDuMisEnCause);

        if (autoriteCompetentePourLeMisEnCause != null)
            autoritesCompetentes.add(AutoriteCompetente.valueOf(autoriteCompetentePourLeMisEnCause));

        var lieuSurvenue = dossierReclamation.getLieuDeSurvenu();
        if (lieuSurvenue instanceof Etablissement etablissement) {
            List<String> codesAutorites = referentielDeCategoriesDEtablissements
                    .recupererAutoritesCompetentesParCodeSousCategorieEtablissement(etablissement.getCodeSousCategorie());

            if (codesAutorites != null) {
                autoritesCompetentes.addAll(
                        codesAutorites.stream()
                                .filter(Objects::nonNull) // Évite les valeurs null
                                .filter(code -> Arrays.stream(AutoriteCompetente.values())
                                        .anyMatch(enumValue -> enumValue.name().equals(code))) // Vérifie que le code est valide
                                .map(AutoriteCompetente::valueOf) // Convertit en AutoriteCompetente
                                .collect(Collectors.toSet()) // Collecte les nouvelles valeurs valides dans un Set
                );
            }

        }
        if (lieuSurvenue instanceof Domicile){
            autoritesCompetentes.add(AutoriteCompetente.CD);
        }


        var contactsEmail = referentielDesContacts.recupererContacts(lieuSurvenue.getCodePostal(),
                autoritesCompetentes);

        emailService.envoyer(contactsEmail, "vous êtes les autorités responsables.");
        logger.info("email envoyé");
        return new Reclamation(
                dossierReclamation,
                autoritesCompetentes,
                contactsEmail,
                lieuSurvenue);
    }
}
