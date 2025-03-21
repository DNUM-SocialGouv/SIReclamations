package fr.gouv.social.sireclamations.user_side.plateforme_telephonique;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class MisEnCauseApi {

  @JsonProperty(value = "typeDeMisEnCause", required = true)
  @NotNull(message = "Le type de mis en cause est obligatoire")
  private TypeDePersonneMisEnCause typeDePersonneMisEnCause;

  @JsonProperty("rpps")
  @Pattern(regexp = "^\\d{9}$", message = "Le RPPS doit être un identifiant à 9 chiffres")
  private String rpps;

  @JsonProperty("civilite")
  private Civilite civilite;

  @JsonProperty("nom")
  private String nom;

  @JsonProperty("prenom")
  private String prenom;

  @JsonProperty("profession")
  private String profession;

  // Getters et setters
  public TypeDePersonneMisEnCause getTypeDePersonneMisEnCause() {
    return typeDePersonneMisEnCause;
  }

  public void setTypeDePersonneMisEnCause(TypeDePersonneMisEnCause typeDePersonneMisEnCause) {
    this.typeDePersonneMisEnCause = typeDePersonneMisEnCause;
  }

  public String getRpps() {
    return rpps;
  }

  public void setRpps(String rpps) {
    this.rpps = rpps;
  }

  public Civilite getCivilite() {
    return civilite;
  }

  public void setCivilite(Civilite civilite) {
    this.civilite = civilite;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public String getPrenom() {
    return prenom;
  }

  public void setPrenom(String prenom) {
    this.prenom = prenom;
  }

  public String getProfession() {
    return profession;
  }

  public void setProfession(String profession) {
    this.profession = profession;
  }
}
