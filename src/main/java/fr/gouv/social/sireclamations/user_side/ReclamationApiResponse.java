package fr.gouv.social.sireclamations.user_side;

import fr.gouv.social.sireclamations.hexagone.domain.AutoriteCompetente;
import java.util.Set;

public class ReclamationApiResponse {
  private int numeroDossier;
  private LieuDeSurvenueApiResponse lieuDeSurvenue;
  private Set<AutoriteCompetente> autoritesCompetentes;

  public ReclamationApiResponse(
      int numeroDossier,
      Set<AutoriteCompetente> autoritesCompetentes,
      LieuDeSurvenueApiResponse lieuDeSurvenue) {
    this.numeroDossier = numeroDossier;
    this.autoritesCompetentes = autoritesCompetentes;
    this.lieuDeSurvenue = lieuDeSurvenue;
  }

  public int getNumeroDossier() {
    return numeroDossier;
  }

  public LieuDeSurvenueApiResponse getLieuDeSurvenue() {
    return lieuDeSurvenue;
  }

  public Set<AutoriteCompetente> getAutoritesCompetentes() {
    return autoritesCompetentes;
  }
}
