package fr.gouv.social.sireclamations.user_side.plateforme_telephonique;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Civilite {
  M("M."),
  MME("Mme"),
  MX("Mx");

  private final String description;

  Civilite(String description) {
    this.description = description;
  }

  @JsonValue
  public String getDescription() {
    return description;
  }

  // Méthode statique pour récupérer un ServiceADomicile depuis une chaîne
  @JsonCreator
  public static Civilite fromString(String text) {
    for (Civilite civilite : Civilite.values()) {
      if (civilite.description.equalsIgnoreCase(text)) {
        return civilite;
      }
    }
    throw new IllegalArgumentException("Aucune Civilite ne correspond à la description : " + text);
  }
}
