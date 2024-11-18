package fr.gouv.social.sireclamations.infrastructure;

public interface CategorieEtablissementRepository {
    String recupererAutoriteCompetenteParCodeSousCategorieEtablissement(String codeSousCategorieEtablissement);
}
