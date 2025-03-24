package fr.gouv.social.sireclamations.user_side.plateforme_telephonique;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class CabinetMedicalApi {

  @JsonProperty("informations")
  private String informations;

  @JsonProperty("adresse")
  private String adresse;

  @JsonProperty("typeDeMisEnCause")
  @NotNull(message = "Le champ typeDeMisEnCause ne peut pas être nul")
  @TypeDeMisEnCauseProfessionnelValid(message = "La valeur de typeDeMisEnCause n'est pas valide")
  private TypeDeMisEnCauseProfessionnel typeDeMisEnCauseProfessionnel;

  public String getInformations() {
    return informations;
  }

  public String getAdresse() {
    return adresse;
  }

  public TypeDeMisEnCauseProfessionnel getTypeDeMisEnCause() {
    return typeDeMisEnCauseProfessionnel;
  }

  public void setInformations(String informations) {
    this.informations = informations;
  }

  public void setAdresse(String adresse) {
    this.adresse = adresse;
  }

  public void setTypeDeMisEnCause(TypeDeMisEnCauseProfessionnel typeDeMisEnCauseProfessionnel) {
    this.typeDeMisEnCauseProfessionnel = typeDeMisEnCauseProfessionnel;
  }
}
