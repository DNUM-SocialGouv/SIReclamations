package fr.gouv.social.sireclamations.user_side.plateforme_telephonique;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

// L'énumération des types de mise en cause
public enum TypeDeMisEnCauseProfessionnel {
  PROFESSIONNEL_SANTE(
      "Un professionnel de santé (médecin, infirmier, aide-soignant, kiné, ostéopathe...)"),
  PROFESSIONNEL_SOINS("Un professionnel du soin (coiffeur, esthéticienne, naturopathe, ...)"),
  AUTRE_PROFESSIONNEL_ETABLISSEMENT(
      "Un autre professionnel d'établissement ou de service (directeur, animateur, agent d'entretien...)"),
  TRAVAILLEUR_SOCIAL("Travailleur social (éducateur, assistant social...)"),
  MANDATAIRE_JUDICIAIRE("Mandataire Judiciaire à la Protection des Majeurs (curatelle, tutelle)"),
  AUTRE_PROFESSIONNEL("Autre Professionnel");

  private final String description;

  // Constructeur pour l'énumération
  TypeDeMisEnCauseProfessionnel(String description) {
    this.description = description;
  }

  @JsonValue
  public String getDescription() {
    return description;
  }

  // Méthode statique pour récupérer un ServiceADomicile depuis une chaîne
  @JsonCreator
  public static TypeDeMisEnCauseProfessionnel fromString(String text) {
    for (TypeDeMisEnCauseProfessionnel misEnCauseProfessionnel :
        TypeDeMisEnCauseProfessionnel.values()) {
      if (misEnCauseProfessionnel.description.equalsIgnoreCase(text)) {
        return misEnCauseProfessionnel;
      }
    }
    throw new IllegalArgumentException(
        "Aucun mis en cause ne correspond à la description : " + text);
  }
}
