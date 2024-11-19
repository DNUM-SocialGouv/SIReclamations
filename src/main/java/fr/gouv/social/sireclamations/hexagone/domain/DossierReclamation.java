package fr.gouv.social.sireclamations.hexagone.domain;

public class DossierReclamation {
    private String numero;
    private String codeSousCategorieEtablissement;
    private Etablissement etablissement;

    public DossierReclamation(String numero, String codeSousCategorieEtablissement, Etablissement etablissement) {
        this.numero = numero;
        this.codeSousCategorieEtablissement = codeSousCategorieEtablissement;
        this.etablissement = etablissement;
    }

    public String getNumero() {
        return numero;
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
