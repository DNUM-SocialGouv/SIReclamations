package fr.gouv.social.sireclamations.hexagone.domain;

public class DossierReclamation {
    private String numeroDossier;
    private Etablissement etablissement;

    public DossierReclamation(String numeroDossier, Etablissement etablissement) {
        this.numeroDossier = numeroDossier;
        this.etablissement = etablissement;
    }

    public String getNumeroDossier() {
        return numeroDossier;
    }

    public String getCodeSousCategorieEtablissement() {
        return etablissement.getCodeSousCategorieEtablissement();
    }


    public String getNumeroFinessEtablissement() {
        return etablissement.getNumeroFiness();
    }
}
