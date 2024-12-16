package fr.gouv.social.sireclamations.hexagone.domain;

public class DossierDeReclamation {
    private int numeroDossier;
    private LieuDeSurvenue lieuDeSurvenue;
    private String libelleDuMisEnCause;

    public DossierDeReclamation(int numeroDossier, LieuDeSurvenue lieuDeSurvenue, String libelleDuMisEnCause) {
        this.numeroDossier = numeroDossier;
        this.lieuDeSurvenue = lieuDeSurvenue;
        this.libelleDuMisEnCause = libelleDuMisEnCause;
    }

    public int getNumeroDossier() {
        return numeroDossier;
    }

    public int getCodePostal() {
        return lieuDeSurvenue.getCodePostal();
    }

    public LieuDeSurvenue getLieuDeSurvenu() {
        return lieuDeSurvenue;
    }

    public String getLibelleDuMisEnCause() {return libelleDuMisEnCause;}
}
