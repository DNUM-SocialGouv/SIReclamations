package fr.gouv.social.sireclamations.hexagone.domain;

public class Etablissement {
    private String numeroFiness;
    private int codeSousCategorieEtablissement;
    private int codePostal;
    private String nom;

    public Etablissement(String numeroFiness, int codeEtablissement, int codePostal, String nom) {
        this.numeroFiness = numeroFiness;
        this.codeSousCategorieEtablissement = codeEtablissement;
        this.codePostal = codePostal;
        this.nom = nom;
    }

    public String getNumeroFiness() {
        return numeroFiness;
    }

    public int getCodeSousCategorieEtablissement() {
        return codeSousCategorieEtablissement;
    }

}
