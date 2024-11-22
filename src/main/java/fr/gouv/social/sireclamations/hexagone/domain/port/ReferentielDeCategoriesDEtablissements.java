package fr.gouv.social.sireclamations.hexagone.domain.port;

import java.util.List;

public interface ReferentielDeCategoriesDEtablissements {
    List<String> recupererAutoritesCompetentesParCodeSousCategorieEtablissement(int codeSousCategorieEtablissement);
}
