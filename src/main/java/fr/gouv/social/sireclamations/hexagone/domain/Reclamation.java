package fr.gouv.social.sireclamations.hexagone.domain;

import fr.gouv.social.sireclamations.hexagone.exceptions.AutoriteCompetenteNotFoundException;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Reclamation {
  private final int numeroDossier;
  private final LieuDeSurvenue lieuDeSurvenue;
  private final Set<AutoriteCompetente> autoritesCompetentes;

  private static final Logger logger = LoggerFactory.getLogger(Reclamation.class);

  public Reclamation(
      DossierDeReclamation dossierDeReclamation,
      Set<AutoriteCompetente> autoritesCompetentes,
      LieuDeSurvenue lieuDeSurvenue) {

    if (autoritesCompetentes.isEmpty()) {
      var messageErreur =
          "Aucune autorité compétente n'a été trouvée pour le dossier : "
              + dossierDeReclamation.getNumeroDossier();
      logger.info(messageErreur);
      throw new AutoriteCompetenteNotFoundException(messageErreur);
    }

    this.numeroDossier = dossierDeReclamation.getNumeroDossier();
    this.lieuDeSurvenue = lieuDeSurvenue;
    this.autoritesCompetentes = autoritesCompetentes;
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
}
