package fr.gouv.social.sireclamations.user_side;

import fr.gouv.social.sireclamations.hexagone.domain.AutoriteCompetente;
import java.util.List;
import java.util.Set;

public class ReclamationApiResponse {
  private int numeroDossier;
  private LieuDeSurvenueApiResponse lieuDeSurvenue;
  private Set<AutoriteCompetente> autoritesCompetentes;
  private List<String> contacts;

  public ReclamationApiResponse(
      int numeroDossier,
      Set<AutoriteCompetente> autoritesCompetentes,
      List<String> contacts,
      LieuDeSurvenueApiResponse lieuDeSurvenue) {
    this.numeroDossier = numeroDossier;
    this.autoritesCompetentes = autoritesCompetentes;
    this.contacts = contacts;
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

  public List<String> getContacts() {
    return contacts;
  }
}
