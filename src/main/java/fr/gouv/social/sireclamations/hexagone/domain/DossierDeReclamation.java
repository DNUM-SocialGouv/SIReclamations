package fr.gouv.social.sireclamations.hexagone.domain;

public class DossierDeReclamation {
    private int numeroDossier;
    private Etablissement etablissement;
    private String libelleDuMisEnCause;

    public DossierDeReclamation(int numeroDossier, Etablissement etablissement, String libelleDuMisEnCause) {
        this.numeroDossier = numeroDossier;
        this.etablissement = etablissement;
        this.libelleDuMisEnCause = libelleDuMisEnCause;
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

    public String getLibelleDuMisEnCause() {return libelleDuMisEnCause;}
}
