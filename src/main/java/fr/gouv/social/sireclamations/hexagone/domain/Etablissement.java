package fr.gouv.social.sireclamations.hexagone.domain;

public class Etablissement {
    private String numeroFiness;
    private String codeSousCategorieEtablissement;
    private String codePostal;

    private String nom;

    public Etablissement(String numeroFiness, String codeEtablissement, String codePostal, String nom) {
        this.numeroFiness = numeroFiness;
        this.codeSousCategorieEtablissement = codeEtablissement;
        this.codePostal = codePostal;
        this.nom = nom;
    }

    public String getNumeroFiness() {
        return numeroFiness;
    }

    public String getCodeSousCategorieEtablissement() {
        return codeSousCategorieEtablissement;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public String getNom() {
        return nom;
    }
}
