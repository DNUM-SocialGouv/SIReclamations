package fr.gouv.social.sireclamations.hexagone;

import fr.gouv.social.sireclamations.hexagone.domain.AutoriteCompetente;
import fr.gouv.social.sireclamations.hexagone.domain.Domicile;
import fr.gouv.social.sireclamations.hexagone.domain.DossierDeReclamation;
import fr.gouv.social.sireclamations.hexagone.domain.Etablissement;
import fr.gouv.social.sireclamations.hexagone.domain.ports.ReferentielDesAutoritesCompetentesParCategoriesDEtablissements;
import fr.gouv.social.sireclamations.hexagone.domain.ports.ReferentielDesAutoritesCompetentesParLieuDeSurvenue;
import fr.gouv.social.sireclamations.hexagone.domain.ports.ReferentielDesAutoritesCompetentesParMisEnCauseADomicile;
import fr.gouv.social.sireclamations.hexagone.domain.ports.ReferentielDesAutoritesCompetentesParMisEnCauseEnEtablissement;
import fr.gouv.social.sireclamations.hexagone.domain.ports.ReferentielDesAutoritesCompetentesParMotifs;
import fr.gouv.social.sireclamations.hexagone.domain.ports.ReferentielDesAutoritesCompetentesParServicesADomicile;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class AutoriteCompetenteService {

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

  public AutoriteCompetenteService(
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
    this.referentielDesAutoritesCompetentesParCategoriesDEtablissements =
        referentielDesAutoritesCompetentesParCategoriesDEtablissements;
    this.referentielDesAutoritesCompetentesParMisEnCauseADomicile =
        referentielDesAutoritesCompetentesParMisEnCauseADomicile;
    this.referentielDesAutoritesCompetentesParServicesADomicile =
        referentielDesAutoritesCompetentesParServicesADomicile;
    this.referentielDesAutoritesCompetentesParMisEnCauseEnEtablissement =
        referentielDesAutoritesCompetentesParMisEnCauseEnEtablissement;
    this.referentielDesAutoritesCompetentesParLieuDeSurvenue =
        referentielDesAutoritesCompetentesParLieuDeSurvenue;
    this.referentielDesAutoritesCompetentesParMotifs = referentielDesAutoritesCompetentesParMotifs;
  }

  public Set<AutoriteCompetente> recupererAutoritesCompetentes(DossierDeReclamation dossier) {
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

    // Vérifier si autoriteCompetenteParLieuDeSurvenue et autoritesCompetentesParMotifs sont vides
    String autoriteCompetenteParLieuDeSurvenue =
        referentielDesAutoritesCompetentesParLieuDeSurvenue.recupererAutoriteCompetente(
            dossier.getLieuDeSurvenu().libelleTypeDeLieu());

    List<String> autoritesCompetentesParMotifs =
        dossier.getMotifs().stream()
            .map(referentielDesAutoritesCompetentesParMotifs::recupererAutoriteCompetente)
            .filter(Objects::nonNull)
            .toList();

    if (autoriteCompetenteParLieuDeSurvenue == null && autoritesCompetentesParMotifs.isEmpty()) {
      // Ajout spécifique pour Etablissement, uniquement si aucune autorité compétente par lieu et
      // motif
      List<String> autoritesCompetenteParCategorieEtablissement =
          referentielDesAutoritesCompetentesParCategoriesDEtablissements
              .recupererAutoritesCompetentesParCodeSousCategorieEtablissement(
                  etablissement.getCodeSousCategorie());

      if (!autoritesCompetenteParCategorieEtablissement.isEmpty()) {
        autoritesCompetentes.addAll(
            convertirCodesAutorites(autoritesCompetenteParCategorieEtablissement));
      }
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
