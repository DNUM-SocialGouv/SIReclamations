package fr.gouv.social.sireclamations.user_side.plateforme_telephonique;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ServiceADomicile {
  HOSPITALISATION_DOMICILE("Hospitalisation à domicile"),
  SERVICE_EDUCATION_SOINS("Service d'éducation spéciale et de soins"),
  SERVICE_AIDE_MENAGERE("Service d'aide ménagère"),
  SERVICE_REPAS("Service de repas"),
  TRAITEMENTS_SPECIAUX("Traitements spécialisés"),
  STRUCTURE_OXYGENE("Structure dispensatrice d'oxygène à usage médical"),
  SAADF("Service d'aide et d'accompagnement à Domicile aux Familles (SAADF)"),
  MJPM("Service Mandataire Judiciaire à la Protection des Majeurs (curatelle, tutelle)"),
  PROFESSIONNEL_LIBERAL("Intervention d'un professionnel libéral ou service (SAMU, médecin)"),
  SSIAD("Service de Soins Infirmier à Domicile (SSIAD)"),
  SAAD("Service d'Aide et d'Accompagnement à Domicile (SAAD)"),
  AUTRE("Autre");

  private final String description;

  ServiceADomicile(String description) {
    this.description = description;
  }

  @JsonValue
  public String getDescription() {
    return description;
  }

  // Méthode statique pour récupérer un ServiceADomicile depuis une chaîne
  @JsonCreator
  public static ServiceADomicile fromString(String text) {
    for (ServiceADomicile service : ServiceADomicile.values()) {
      if (service.description.equalsIgnoreCase(text)) {
        return service;
      }
    }
    throw new IllegalArgumentException(
        "Aucun ServiceADomicile ne correspond à la description : " + text);
  }
}
