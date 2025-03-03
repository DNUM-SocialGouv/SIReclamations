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

  private final ReferentielDesAutoritesCompetentesParMisEnCauseADomicile
      referentielDesAutoritesCompetentesParMisEnCauseADomicile;

  private final ReferentielDesAutoritesCompetentesParMisEnCauseEnEtablissement
      referentielDesAutoritesCompetentesParMisEnCauseEnEtablissement;

  private final ReferentielDesAutoritesCompetentesParLieuDeSurvenue
      referentielDesAutoritesCompetentesParLieuDeSurvenue;

  private final ReferentielDesAutoritesCompetentesParMotifs
      referentielDesAutoritesCompetentesParMotifs;
  private static final Logger logger = LoggerFactory.getLogger(DeposerReclamation.class);

  public DeposerReclamation(
      DematSocial dematSocial,
      ReferentielDesAutoritesCompetentesParCategoriesDEtablissements
          referentielDesAutoritesCompetentesParCategoriesDEtablissements,
      ReferentielDesTypeDeMisEnCause referentielDesTypeDeMisEnCause,
      ReferentielDesAutoritesCompetentesParTypeDeMisEnCause
          referentielDesAutoritesCompetentesParTypeDeMisEnCause,
      ReferentielDesAutoritesCompetentesParMisEnCauseADomicile
          referentielDesAutoritesCompetentesParMisEnCauseADomicile,
      ReferentielDesAutoritesCompetentesParMisEnCauseEnEtablissement
          referentielDesAutoritesCompetentesParMisEnCauseEnEtablissement,
      ReferentielDesAutoritesCompetentesParLieuDeSurvenue
          referentielDesAutoritesCompetentesParLieuDeSurvenue,
      ReferentielDesAutoritesCompetentesParMotifs referentielDesAutoritesCompetentesParMotifs) {
    this.dematSocial = dematSocial;
    this.referentielDesAutoritesCompetentesParCategoriesDEtablissements =
        referentielDesAutoritesCompetentesParCategoriesDEtablissements;
    this.referentielDesTypeDeMisEnCause = referentielDesTypeDeMisEnCause;
    this.referentielDesAutoritesCompetentesParTypeDeMisEnCause =
        referentielDesAutoritesCompetentesParTypeDeMisEnCause;
    this.referentielDesAutoritesCompetentesParMisEnCauseADomicile =
        referentielDesAutoritesCompetentesParMisEnCauseADomicile;
    this.referentielDesAutoritesCompetentesParMisEnCauseEnEtablissement =
        referentielDesAutoritesCompetentesParMisEnCauseEnEtablissement;
    this.referentielDesAutoritesCompetentesParLieuDeSurvenue =
        referentielDesAutoritesCompetentesParLieuDeSurvenue;
    this.referentielDesAutoritesCompetentesParMotifs = referentielDesAutoritesCompetentesParMotifs;
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
    if (!(dossier.getLieuDeSurvenu() instanceof Etablissement etablissement)) {
      return dossier.getLieuDeSurvenu() instanceof Domicile ? Collections.emptySet() : Set.of();
    }

    // Récupération des données des référentiels existant
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

    List<String> autoritesCompetenteParCategorieEtablissement =
        referentielDesAutoritesCompetentesParCategoriesDEtablissements
            .recupererAutoritesCompetentesParCodeSousCategorieEtablissement(
                etablissement.getCodeSousCategorie());

    // Application des règles métier
    Set<AutoriteCompetente> autoritesCompetentes = new HashSet<>();
    Optional.ofNullable(autoriteCompetentePourLeMisEnCause)
        .map(AutoriteCompetente::valueOf)
        .ifPresent(autoritesCompetentes::add);
    Optional.ofNullable(autoriteCompetenteParLieuDeSurvenue)
        .map(AutoriteCompetente::valueOf)
        .ifPresent(autoritesCompetentes::add);

    if (autoriteCompetenteParLieuDeSurvenue == null) {
      if (!autoritesCompetentesParMotifs.isEmpty()) {
        autoritesCompetentes.addAll(convertirCodesAutorites(autoritesCompetentesParMotifs));
      } else {
        autoritesCompetentes.addAll(
            convertirCodesAutorites(autoritesCompetenteParCategorieEtablissement));
      }
    }

    return autoritesCompetentes;
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
