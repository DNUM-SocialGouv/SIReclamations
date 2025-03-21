package fr.gouv.social.sireclamations.user_side.plateforme_telephonique;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class TrajetApi {

  @JsonProperty(value = "typeDeTransport", required = true)
  @NotNull(message = "Le type de transport est obligatoire")
  private TypeDeTransport typeDeTransport;

  @JsonProperty("nomSociete")
  private String nomSociete;

  @JsonProperty("typeDeMisEnCause")
  private TypeDeMisEnCauseProfessionnel typeDeMisEnCauseProfessionnel;

  // Getters et setters
  public TypeDeTransport getTypeDeTransport() {
    return typeDeTransport;
  }

  public void setTypeDeTransport(TypeDeTransport typeDeTransport) {
    this.typeDeTransport = typeDeTransport;
  }

  public String getNomSociete() {
    return nomSociete;
  }

  public void setNomSociete(String nomSociete) {
    this.nomSociete = nomSociete;
  }

  public TypeDeMisEnCauseProfessionnel getTypeDeMisEnCause() {
    return typeDeMisEnCauseProfessionnel;
  }

  public void setTypeDeMisEnCause(TypeDeMisEnCauseProfessionnel typeDeMisEnCauseProfessionnel) {
    this.typeDeMisEnCauseProfessionnel = typeDeMisEnCauseProfessionnel;
  }
}
