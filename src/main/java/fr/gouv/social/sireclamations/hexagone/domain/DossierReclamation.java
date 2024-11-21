package fr.gouv.social.sireclamations.hexagone.domain;

public class DossierReclamation {
    private String numeroDossier;
    private String codeSousCategorieEtablissement;
    private Etablissement etablissement;

    public DossierReclamation(String numeroDossier, String codeSousCategorieEtablissement, Etablissement etablissement) {
        this.numeroDossier = numeroDossier;
        this.codeSousCategorieEtablissement = codeSousCategorieEtablissement;
        this.etablissement = etablissement;
    }

    public String getNumeroDossier() {
        return numeroDossier;
    }

    public String getCodeSousCategorieEtablissement() {
        return codeSousCategorieEtablissement;
    }

    public String getCodePostal() {
        return etablissement.getCodePostal();
    }

    public Etablissement getEtablissement() {
        return etablissement;
    }
}
