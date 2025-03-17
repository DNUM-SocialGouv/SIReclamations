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
public class AffecterReclamation {

  private final DematSocial dematSocial;
  private final ReferentielDesAutoritesCompetentesParCategoriesDEtablissements
      referentielDesAutoritesCompetentesParCategoriesDEtablissements;

  private final ReferentielDesAutoritesCompetentesParMisEnCauseADomicile
      referentielDesAutoritesCompetentesParMisEnCauseADomicile;

  private final ReferentielDesAutoritesCompetentesParServicesADomicile
      referentielDesAutoritesCompetentesParServicesADomicile;

  private final ReferentielDesAutoritesCompetentesParMisEnCauseEnEtablissement
      referentielDesAutoritesCompetentesParMisEnCauseEnEtablissement;

  private final ReferentielDesAutoritesCompetentesParLieuDeSurvenue
      referentielDesAutoritesCompetentesParLieuDeSurvenue;

  private final ReferentielDesAutoritesCompetentesParMotifs
      referentielDesAutoritesCompetentesParMotifs;
  private static final Logger logger = LoggerFactory.getLogger(AffecterReclamation.class);

  public AffecterReclamation(
      DematSocial dematSocial,
      ReferentielDesAutoritesCompetentesParCategoriesDEtablissements
          referentielDesAutoritesCompetentesParCategoriesDEtablissements,
      ReferentielDesAutoritesCompetentesParMisEnCauseADomicile
          referentielDesAutoritesCompetentesParMisEnCauseADomicile,
      ReferentielDesAutoritesCompetentesParServicesADomicile
          referentielDesAutoritesCompetentesParServicesADomicile,
      ReferentielDesAutoritesCompetentesParMisEnCauseEnEtablissement
          referentielDesAutoritesCompetentesParMisEnCauseEnEtablissement,
      ReferentielDesAutoritesCompetentesParLieuDeSurvenue
          referentielDesAutoritesCompetentesParLieuDeSurvenue,
      ReferentielDesAutoritesCompetentesParMotifs referentielDesAutoritesCompetentesParMotifs) {
    this.dematSocial = dematSocial;
    this.referentielDesAutoritesCompetentesParCategoriesDEtablissements =
        referentielDesAutoritesCompetentesParCategoriesDEtablissements;
    this.referentielDesAutoritesCompetentesParMisEnCauseADomicile =
        referentielDesAutoritesCompetentesParMisEnCauseADomicile;
    this.referentielDesAutoritesCompetentesParMisEnCauseEnEtablissement =
        referentielDesAutoritesCompetentesParMisEnCauseEnEtablissement;
    this.referentielDesAutoritesCompetentesParLieuDeSurvenue =
        referentielDesAutoritesCompetentesParLieuDeSurvenue;
    this.referentielDesAutoritesCompetentesParMotifs = referentielDesAutoritesCompetentesParMotifs;
    this.referentielDesAutoritesCompetentesParServicesADomicile =
        referentielDesAutoritesCompetentesParServicesADomicile;
  }

  public Reclamation executer(int numeroDossier)
      throws AutoriteCompetenteNotFoundException, DematSocialException {
    DossierDeReclamation dossier = recupererDossier(numeroDossier);
    Set<AutoriteCompetente> autorites = determinerAutoritesCompetentes(dossier);

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

  private Set<AutoriteCompetente> determinerAutoritesCompetentes(DossierDeReclamation dossier) {
    Set<AutoriteCompetente> autoritesCompetentes = new HashSet<>();

    if (dossier.getLieuDeSurvenu() instanceof Domicile domicile) {
      traiterDomicile(dossier, domicile, autoritesCompetentes);
    } else if (dossier.getLieuDeSurvenu() instanceof Etablissement etablissement) {
      traiterEtablissement(dossier, etablissement, autoritesCompetentes);
    } else {
      traiterLieuCommun(dossier, autoritesCompetentes);
    }

    return autoritesCompetentes;
  }

  private void traiterDomicile(
      DossierDeReclamation dossier,
      Domicile domicile,
      Set<AutoriteCompetente> autoritesCompetentes) {
    String autoriteCompetentePourLeMisEnCauseADomicile =
        referentielDesAutoritesCompetentesParMisEnCauseADomicile.recupererAutoriteCompetente(
            dossier.getLibelleDuMisEnCause());
    String autoriteCompetentePourServiceADomicile =
        referentielDesAutoritesCompetentesParServicesADomicile.recupererAutoriteCompetente(
            domicile.getService());

    Optional.ofNullable(autoriteCompetentePourLeMisEnCauseADomicile)
        .map(AutoriteCompetente::valueOf)
        .ifPresent(autoritesCompetentes::add);

    if (autoriteCompetentePourLeMisEnCauseADomicile == null) {
      Optional.ofNullable(autoriteCompetentePourServiceADomicile)
          .map(AutoriteCompetente::valueOf)
          .ifPresent(autoritesCompetentes::add);
    }

    if (autoritesCompetentes.isEmpty()) {
      autoritesCompetentes.add(AutoriteCompetente.CD);
    }
  }

  private void traiterEtablissement(
      DossierDeReclamation dossier,
      Etablissement etablissement,
      Set<AutoriteCompetente> autoritesCompetentes) {
    traiterLieuCommun(dossier, autoritesCompetentes);
    List<String> autoritesCompetenteParCategorieEtablissement =
        referentielDesAutoritesCompetentesParCategoriesDEtablissements
            .recupererAutoritesCompetentesParCodeSousCategorieEtablissement(
                etablissement.getCodeSousCategorie());

    if (!autoritesCompetenteParCategorieEtablissement.isEmpty()) {
      autoritesCompetentes.addAll(
          convertirCodesAutorites(autoritesCompetenteParCategorieEtablissement));
    }
  }

  private void traiterLieuCommun(
      DossierDeReclamation dossier, Set<AutoriteCompetente> autoritesCompetentes) {
    String autoriteCompetentePourLeMisEnCause =
        dossier.getMaltraitance()
            ? referentielDesAutoritesCompetentesParMisEnCauseEnEtablissement
                .recupererAutoriteCompetente(dossier.getLibelleDuMisEnCause())
            : null;
    String autoriteCompetenteParLieuDeSurvenue =
        referentielDesAutoritesCompetentesParLieuDeSurvenue.recupererAutoriteCompetente(
            dossier.getLieuDeSurvenu().libelleTypeDeLieu());

    List<String> autoritesCompetentesParMotifs =
        dossier.getMotifs().stream()
            .map(referentielDesAutoritesCompetentesParMotifs::recupererAutoriteCompetente)
            .filter(Objects::nonNull)
            .toList();

    Optional.ofNullable(autoriteCompetentePourLeMisEnCause)
        .map(AutoriteCompetente::valueOf)
        .ifPresent(autoritesCompetentes::add);

    Optional.ofNullable(autoriteCompetenteParLieuDeSurvenue)
        .map(AutoriteCompetente::valueOf)
        .ifPresent(autoritesCompetentes::add);

    if (autoriteCompetenteParLieuDeSurvenue == null && !autoritesCompetentesParMotifs.isEmpty()) {
      autoritesCompetentes.addAll(convertirCodesAutorites(autoritesCompetentesParMotifs));
    }
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
