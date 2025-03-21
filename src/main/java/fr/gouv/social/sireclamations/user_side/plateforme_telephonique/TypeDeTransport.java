package fr.gouv.social.sireclamations.user_side.plateforme_telephonique;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TypeDeTransport {
  ASSU("Ambulance de secours et de soins d'urgence (ASSU)"),
  VSAV("Véhicule de secours et d'assistance aux victimes (VSAV)"),
  AMBULANCE("Ambulance"),
  VSL("Véhicule sanitaire léger"),
  TAXI("Chauffeur de taxi"),
  AUTRE("Autre type de transport");

  private final String description;

  TypeDeTransport(String description) {
    this.description = description;
  }

  @JsonValue
  public String getDescription() {
    return description;
  }

  // Méthode statique pour récupérer un ServiceADomicile depuis une chaîne
  @JsonCreator
  public static TypeDeTransport fromString(String text) {
    for (TypeDeTransport typeDeTransport : TypeDeTransport.values()) {
      if (typeDeTransport.description.equalsIgnoreCase(text)) {
        return typeDeTransport;
      }
    }
    throw new IllegalArgumentException(
        "Aucun TypeDeTransport ne correspond à la description : " + text);
  }
}
