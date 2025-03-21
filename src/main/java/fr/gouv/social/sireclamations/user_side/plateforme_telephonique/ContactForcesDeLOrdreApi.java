package fr.gouv.social.sireclamations.user_side.plateforme_telephonique;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class ContactForcesDeLOrdreApi {

  @Size(max = 100, message = "Le champ 'juridiction' ne peut pas dépasser 100 caractères.")
  @JsonProperty("juridiction")
  private String juridiction;

  @NotNull(message = "Le champ 'dateDemarche' est requis.")
  @JsonProperty("dateDemarche")
  private LocalDate dateDemarche;

  // Getters et Setters

  public String getJuridiction() {
    return juridiction;
  }

  public void setJuridiction(String juridiction) {
    this.juridiction = juridiction;
  }

  public LocalDate getDateDemarche() {
    return dateDemarche;
  }

  public void setDateDemarche(LocalDate dateDemarche) {
    this.dateDemarche = dateDemarche;
  }
}
