package fr.gouv.social.sireclamations.user_side.plateforme_telephonique;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ContactEtablissementApi {

  @NotNull(message = "Le champ 'contactEffectue' est requis.")
  @JsonProperty("contactEffectue")
  private Boolean contactEffectue;

  @Pattern(
      regexp = "^\\d{4}-\\d{2}-\\d{2}$",
      message = "La date de démarche doit être au format YYYY-MM-DD.")
  @JsonProperty("dateDemarche")
  private String dateDemarche;

  @JsonProperty("réponseObtenue")
  private Boolean reponseObtenue = false; // Valeur par défaut à false

  @Size(max = 10000, message = "La description ne peut pas dépasser 10000 caractères.")
  @JsonProperty("description")
  private String description;

  // Getters et Setters

  public Boolean getContactEffectue() {
    return contactEffectue;
  }

  public void setContactEffectue(Boolean contactEffectue) {
    this.contactEffectue = contactEffectue;
  }

  public String getDateDemarche() {
    return dateDemarche;
  }

  public void setDateDemarche(String dateDemarche) {
    this.dateDemarche = dateDemarche;
  }

  public Boolean getReponseObtenue() {
    return reponseObtenue;
  }

  public void setReponseObtenue(Boolean reponseObtenue) {
    this.reponseObtenue = reponseObtenue;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
