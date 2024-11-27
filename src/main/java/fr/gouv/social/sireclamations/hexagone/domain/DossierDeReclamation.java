package fr.gouv.social.sireclamations.hexagone.domain;

public class DossierDeReclamation {
    private int numeroDossier;
    private Etablissement etablissement;

    public DossierDeReclamation(int numeroDossier, Etablissement etablissement) {
        this.numeroDossier = numeroDossier;
        this.etablissement = etablissement;
    }

    public int getNumeroDossier() {
        return numeroDossier;
    }

    public int getCodePostal() {
        return etablissement.getCodePostal();
    }

    public Etablissement getEtablissement() {
        return etablissement;
    }
}
