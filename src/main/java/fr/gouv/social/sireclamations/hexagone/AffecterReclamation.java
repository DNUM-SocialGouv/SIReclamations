package fr.gouv.social.sireclamations.hexagone;

import fr.gouv.social.sireclamations.hexagone.domain.*;
import fr.gouv.social.sireclamations.hexagone.domain.ports.*;
import fr.gouv.social.sireclamations.hexagone.exceptions.AutoriteCompetenteNotFoundException;
import fr.gouv.social.sireclamations.hexagone.exceptions.DematSocialException;
import java.io.IOException;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AffecterReclamation {

  private final DematSocial dematSocial;
  private final AutoriteCompetenteService autoriteCompetenteService;
  private static final Logger logger = LoggerFactory.getLogger(AffecterReclamation.class);

  public AffecterReclamation(
      DematSocial dematSocial, AutoriteCompetenteService autoriteCompetenteService) {
    this.dematSocial = dematSocial;
    this.autoriteCompetenteService = autoriteCompetenteService;
  }

  public Reclamation executer(int numeroDossier)
      throws AutoriteCompetenteNotFoundException, DematSocialException {
    DossierDeReclamation dossier = recupererDossier(numeroDossier);
    Set<AutoriteCompetente> autorites =
        autoriteCompetenteService.recupererAutoritesCompetentes(dossier);

    logger.info(
        "Résultat de l'affectation du dossier {} : autorités compétentes : {}",
        numeroDossier,
        autorites);

    return new Reclamation(dossier, autorites, dossier.getLieuDeSurvenu());
  }

  private DossierDeReclamation recupererDossier(int numeroDossier) throws DematSocialException {
    try {
      return dematSocial.recupererDossier(numeroDossier);
    } catch (IOException e) {
      logger.error(
          "Erreur lors de la récupération du dossier demat.social : {}", e.getMessage(), e);
      throw new DematSocialException(e.getMessage());
    }
  }
}
