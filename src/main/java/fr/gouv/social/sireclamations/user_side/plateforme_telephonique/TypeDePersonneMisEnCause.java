package fr.gouv.social.sireclamations.user_side.plateforme_telephonique;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TypeDePersonneMisEnCause {
  MEMBRE_FAMILLE("Membre de la famille"),
  PROCHE("Proche (ami, voisin...)"),
  PROFESSIONNEL("Professionnel"),
  AUTRE("Autre");

  private final String description;

  TypeDePersonneMisEnCause(String description) {
    this.description = description;
  }

  @JsonValue
  public String getDescription() {
    return description;
  }

  // Méthode statique pour récupérer un ServiceADomicile depuis une chaîne
  @JsonCreator
  public static TypeDePersonneMisEnCause fromString(String text) {
    for (TypeDePersonneMisEnCause misEnCause : TypeDePersonneMisEnCause.values()) {
      if (misEnCause.description.equalsIgnoreCase(text)) {
        return misEnCause;
      }
    }
    throw new IllegalArgumentException(
        "Aucun mis en cause ne correspond à la description : " + text);
  }
}
