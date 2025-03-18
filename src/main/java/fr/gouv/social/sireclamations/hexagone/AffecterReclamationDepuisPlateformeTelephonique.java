package fr.gouv.social.sireclamations.hexagone;

import fr.gouv.social.sireclamations.hexagone.domain.DossierDeReclamation;
import fr.gouv.social.sireclamations.hexagone.domain.Reclamation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AffecterReclamationDepuisPlateformeTelephonique {

  private final AutoriteCompetenteService autoriteCompetenteService;

  private static final Logger logger = LoggerFactory.getLogger(AffecterReclamation.class);

  public AffecterReclamationDepuisPlateformeTelephonique(
      AutoriteCompetenteService autoriteCompetenteService) {
    this.autoriteCompetenteService = autoriteCompetenteService;
  }

  public Reclamation executer(DossierDeReclamation dossierDeReclamation) {

    var autoritesCompetentes =
        autoriteCompetenteService.recupererAutoritesCompetentes(dossierDeReclamation);
    logger.info(
        "Résultat de l'affectation du dossier {} : autorités compétentes : {}",
        dossierDeReclamation.getNumeroDossier(),
        autoritesCompetentes);

    return new Reclamation(
        dossierDeReclamation, autoritesCompetentes, dossierDeReclamation.getLieuDeSurvenu());
  }
}
