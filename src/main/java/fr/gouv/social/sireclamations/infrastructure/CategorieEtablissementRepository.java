package fr.gouv.social.sireclamations.infrastructure;

import java.util.List;

public interface CategorieEtablissementRepository {
    List<String> recupererAutoriteCompetenteParCodeSousCategorieEtablissement(String codeSousCategorieEtablissement);
}
