package fr.gouv.social.sireclamations.hexagone.domain;

import java.util.List;

public class DossierDeReclamation {

  private final int numeroDossier;
  private boolean maltraitance;
  private final LieuDeSurvenue lieuDeSurvenue;
  private final String libelleDuMisEnCause;

  private final List<String> motifs;

  public DossierDeReclamation(
      int numeroDossier,
      LieuDeSurvenue lieuDeSurvenue,
      String libelleDuMisEnCause,
      boolean maltraitance,
      List<String> motifs) {
    this.numeroDossier = numeroDossier;
    this.lieuDeSurvenue = lieuDeSurvenue;
    this.libelleDuMisEnCause = libelleDuMisEnCause;
    this.maltraitance = maltraitance;
    this.motifs = motifs;
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

  public String getLibelleDuMisEnCause() {
    return libelleDuMisEnCause;
  }

  public boolean getMaltraitance() {
    return maltraitance;
  }

  public List<String> getMotifs() {
    return motifs;
  }
}
