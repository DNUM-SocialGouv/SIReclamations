package fr.gouv.social.sireclamations.hexagone.domain.ports;

import java.util.List;

public interface ReferentielDeCategoriesDEtablissements {
  List<String> recupererAutoritesCompetentesParCodeSousCategorieEtablissement(
      int codeSousCategorieEtablissement);
}
