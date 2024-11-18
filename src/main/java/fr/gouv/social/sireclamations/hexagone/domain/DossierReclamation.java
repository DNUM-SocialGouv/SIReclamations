package fr.gouv.social.sireclamations.hexagone.domain;

public class DossierReclamation {
    private String numero;
    private String codeSousCategorieEtablissement;
    private String codePostal;
    private Etablissement etablissement;

    public DossierReclamation(String numero, String codeSousCategorieEtablissement, String codePostal, Etablissement etablissement) {
        this.numero = numero;
        this.codeSousCategorieEtablissement = codeSousCategorieEtablissement;
        this.codePostal = codePostal;
        this.etablissement = etablissement;
    }

    public String getNumero() {
        return numero;
    }

    public String getCodeSousCategorieEtablissement() {
        return codeSousCategorieEtablissement;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public Etablissement getEtablissement() {
        return etablissement;
    }
}
