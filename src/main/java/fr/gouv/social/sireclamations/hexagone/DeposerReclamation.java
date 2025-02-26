package fr.gouv.social.sireclamations.hexagone;

import fr.gouv.social.sireclamations.hexagone.domain.*;
import fr.gouv.social.sireclamations.hexagone.domain.ports.*;
import fr.gouv.social.sireclamations.hexagone.exceptions.AutoriteCompetenteNotFoundException;
import fr.gouv.social.sireclamations.hexagone.exceptions.DematSocialException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DeposerReclamation {

  private final DematSocial dematSocial;
  private final ReferentielDesAutoritesCompetentesParCategoriesDEtablissements
      referentielDesAutoritesCompetentesParCategoriesDEtablissements;
  private final ReferentielDesTypeDeMisEnCause referentielDesTypeDeMisEnCause;
  private final ReferentielDesAutoritesCompetentesParTypeDeMisEnCause
      referentielDesAutoritesCompetentesParTypeDeMisEnCause;
  private static final Logger logger = LoggerFactory.getLogger(DeposerReclamation.class);

  public DeposerReclamation(
      DematSocial dematSocial,
      ReferentielDesAutoritesCompetentesParCategoriesDEtablissements
          referentielDesAutoritesCompetentesParCategoriesDEtablissements,
      ReferentielDesTypeDeMisEnCause referentielDesTypeDeMisEnCause,
      ReferentielDesAutoritesCompetentesParTypeDeMisEnCause
          referentielDesAutoritesCompetentesParTypeDeMisEnCause) {
    this.dematSocial = dematSocial;
    this.referentielDesAutoritesCompetentesParCategoriesDEtablissements =
        referentielDesAutoritesCompetentesParCategoriesDEtablissements;
    this.referentielDesTypeDeMisEnCause = referentielDesTypeDeMisEnCause;
    this.referentielDesAutoritesCompetentesParTypeDeMisEnCause =
        referentielDesAutoritesCompetentesParTypeDeMisEnCause;
  }

  public Reclamation executer(int numeroDossier)
      throws AutoriteCompetenteNotFoundException, DematSocialException {
    DossierDeReclamation dossier = recupererDossier(numeroDossier);
    Set<AutoriteCompetente> autorites = determinerAutoritesCompetentes(dossier);

    return new Reclamation(dossier, autorites, dossier.getLieuDeSurvenu());
  }

  private DossierDeReclamation recupererDossier(int numeroDossier) throws DematSocialException {
    try {
      return dematSocial.recupererDossier(numeroDossier);
    } catch (IOException e) {
      logger.error(
          "Erreur lors de la récupération du dossier chez demat social : " + e.getMessage(), e);
      throw new DematSocialException(e.getMessage());
    }
  }

  private Set<AutoriteCompetente> determinerAutoritesCompetentes(DossierDeReclamation dossier) {
    Set<AutoriteCompetente> autorites = new HashSet<>();
    var typeMisEnCause =
        referentielDesTypeDeMisEnCause.recupererTypeDuMisEnCause(dossier.getLibelleDuMisEnCause());
    var autoriteParType =
        referentielDesAutoritesCompetentesParTypeDeMisEnCause
            .recupererAutoriteCompetentePourUnTypeDeMisEnCause(typeMisEnCause);

    if (autoriteParType != null) {
      autorites.add(AutoriteCompetente.valueOf(autoriteParType));
    }

    if (dossier.getLieuDeSurvenu() instanceof Etablissement etablissement) {
      autorites.addAll(
          convertirCodesAutorites(
              referentielDesAutoritesCompetentesParCategoriesDEtablissements
                  .recupererAutoritesCompetentesParCodeSousCategorieEtablissement(
                      etablissement.getCodeSousCategorie())));
    } else if (dossier.getLieuDeSurvenu() instanceof Domicile) {
      autorites.add(AutoriteCompetente.CD);
    }

    return autorites;
  }

  private Set<AutoriteCompetente> convertirCodesAutorites(List<String> codesAutorites) {
    return codesAutorites.stream()
        .filter(Objects::nonNull)
        .filter(
            code -> Arrays.stream(AutoriteCompetente.values()).anyMatch(a -> a.name().equals(code)))
        .map(AutoriteCompetente::valueOf)
        .collect(Collectors.toSet());
  }
}
