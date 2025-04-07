package fr.gouv.social.sireclamations.user_side.plateforme_telephonique;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ContactAutreOrganismeApi {

  @NotNull(message = "Le champ 'organismesSaisis' est requis.")
  @Size(max = 1000, message = "Le champ 'organismesSaisis' ne peut pas dépasser 1000 caractères.")
  @JsonProperty("organismesSaisis")
  private String organismesSaisis;

  public String getOrganismesSaisis() {
    return organismesSaisis;
  }

  public void setOrganismesSaisis(String organismesSaisis) {
    this.organismesSaisis = organismesSaisis;
  }
}
