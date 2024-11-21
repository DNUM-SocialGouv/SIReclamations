package fr.gouv.social.sireclamations.hexagone.port;

import java.util.List;

public interface CategorieEtablissementPort {
    List<String> recupererAutoriteCompetenteParCodeSousCategorieEtablissement(String codeSousCategorieEtablissement);
}
