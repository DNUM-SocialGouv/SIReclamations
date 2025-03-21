package fr.gouv.social.sireclamations.user_side.plateforme_telephonique;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class DomicileApi {

  @JsonProperty(value = "adresse", required = true)
  @NotNull(message = "L'adresse est obligatoire")
  private String adresse;

  @JsonProperty("serviceADomicile")
  private ServiceADomicile serviceADomicile;

  // Getters et setters
  public String getAdresse() {
    return adresse;
  }

  public void setAdresse(String adresse) {
    this.adresse = adresse;
  }

  public ServiceADomicile getServiceADomicile() {
    return serviceADomicile;
  }

  public void setServiceADomicile(ServiceADomicile serviceADomicile) {
    this.serviceADomicile = serviceADomicile;
  }
}
