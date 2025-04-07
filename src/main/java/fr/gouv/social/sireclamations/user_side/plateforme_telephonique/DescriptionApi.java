package fr.gouv.social.sireclamations.user_side.plateforme_telephonique;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public class DescriptionApi {

  @NotNull(message = "Le champ 'maltraitance' est requis.")
  @JsonProperty("maltraitance")
  private Boolean maltraitance;

  @NotNull(message = "Le champ 'typeDeMaltraitance' est requis.")
  @JsonProperty("typeDeMaltraitance")
  private List<String> typeDeMaltraitance;

  @NotNull(message = "Le champ 'typesDeFaits' est requis.")
  @JsonProperty("typesDeFaits")
  private List<String> typesDeFaits;

  @NotNull(message = "La date de survenue est requise.")
  @Pattern(
      regexp = "^\\d{4}-\\d{2}-\\d{2}$",
      message = "La date de survenue doit être au format YYYY-MM-DD.")
  @JsonProperty("dateSurvenue")
  private String dateSurvenue;

  @NotNull(message = "Le champ 'consequenceSurLaVictime' est requis.")
  @JsonProperty("consequenceSurLaVictime")
  private List<String> consequenceSurLaVictime;

  @NotNull(message = "Le champ 'situationToujoursActuelle' est requis.")
  @JsonProperty("situationToujoursActuelle")
  private String situationToujoursActuelle;

  @JsonProperty("dateDeFin")
  private String dateDeFin;

  @NotNull(message = "La description est requise.")
  @Size(min = 1, max = 10000, message = "La description doit contenir entre 1 et 10000 caractères.")
  @JsonProperty("description")
  private String description;

  public Boolean getMaltraitance() {
    return maltraitance;
  }

  public void setMaltraitance(Boolean maltraitance) {
    this.maltraitance = maltraitance;
  }

  public List<String> getTypeDeMaltraitance() {
    return typeDeMaltraitance;
  }

  public void setTypeDeMaltraitance(List<String> typeDeMaltraitance) {
    this.typeDeMaltraitance = typeDeMaltraitance;
  }

  public List<String> getTypesDeFaits() {
    return typesDeFaits;
  }

  public void setTypesDeFaits(List<String> typesDeFaits) {
    this.typesDeFaits = typesDeFaits;
  }

  public String getDateSurvenue() {
    return dateSurvenue;
  }

  public void setDateSurvenue(String dateSurvenue) {
    this.dateSurvenue = dateSurvenue;
  }

  public List<String> getConsequenceSurLaVictime() {
    return consequenceSurLaVictime;
  }

  public void setConsequenceSurLaVictime(List<String> consequenceSurLaVictime) {
    this.consequenceSurLaVictime = consequenceSurLaVictime;
  }

  public String getSituationToujoursActuelle() {
    return situationToujoursActuelle;
  }

  public void setSituationToujoursActuelle(String situationToujoursActuelle) {
    this.situationToujoursActuelle = situationToujoursActuelle;
  }

  public String getDateDeFin() {
    return dateDeFin;
  }

  public void setDateDeFin(String dateDeFin) {
    this.dateDeFin = dateDeFin;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
