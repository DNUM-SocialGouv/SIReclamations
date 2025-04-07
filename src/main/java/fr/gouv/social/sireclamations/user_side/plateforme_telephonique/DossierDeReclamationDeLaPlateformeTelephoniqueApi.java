package fr.gouv.social.sireclamations.user_side.plateforme_telephonique;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class DossierDeReclamationDeLaPlateformeTelephoniqueApi {

  @JsonProperty(value = "id", required = true)
  @NotNull(message = "Le champ 'id' est requis doit correspondre à un numéro de dossier")
  @Pattern(regexp = "\\d+", message = "Le champ 'id' doit être un entier")
  private String id;

  @JsonProperty(value = "declarant", required = true)
  @NotNull(message = "Le champ 'déclarant' est requis")
  @Valid
  private DeclarantApi declarant;

  @JsonProperty(value = "victime", required = true)
  @NotNull(message = "Le champ 'victime' est requis.")
  @Valid
  private VictimeApi victime;

  @JsonProperty(value = "lieuSurvenue", required = true)
  @NotNull(message = "Le champ 'lieuDeSurvenue' est requis.")
  @Valid
  private LieuDeSurvenueApi lieuDeSurvenueApi;

  @JsonProperty(value = "misEnCause", required = true)
  @NotNull(message = "Le champ 'misEnCause' est requis.")
  @Valid
  private MisEnCauseApi misEnCause;

  @JsonProperty(value = "description", required = true)
  @NotNull(message = "Le champs 'description' des faits est requis.")
  @Valid
  private DescriptionApi description;

  @JsonProperty("demarches")
  @Valid
  private DemarchesApi demarches;

  public String getId() {
    return id;
  }

  public LieuDeSurvenueApi getLieuDeSurvenueApi() {
    return lieuDeSurvenueApi;
  }

  public MisEnCauseApi getMisEnCause() {
    return misEnCause;
  }

  public DescriptionApi getDescription() {
    return description;
  }

  public boolean getMaltraitance() {
    return getDescription().getMaltraitance();
  }
}
