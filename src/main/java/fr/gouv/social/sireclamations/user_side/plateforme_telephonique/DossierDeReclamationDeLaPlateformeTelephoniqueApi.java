package fr.gouv.social.sireclamations.user_side.plateforme_telephonique;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class DossierDeReclamationDeLaPlateformeTelephoniqueApi {

  @JsonProperty(value = "id", required = true)
  @NotNull(message = "Le champ id ne peut pas être nul et doit correspondre à un numéro de dossier")
  @Pattern(regexp = "\\d+", message = "Le champ 'id' doit être un entier")
  private String id;

  @JsonProperty(value = "declarant", required = true)
  private DeclarantApi declarant;

  @JsonProperty(value = "victime", required = true)
  private VictimeApi victime;

  @JsonProperty(value = "lieuSurvenue", required = true)
  private LieuSurvenueApi lieuSurvenue;

  @JsonProperty(value = "misEnCause", required = true)
  private MisEnCauseApi misEnCause;

  @JsonProperty(value = "description", required = true)
  private DescriptionApi description;

  @JsonProperty("demarches")
  private DemarchesApi demarches;

  public String getId() {
    return id;
  }

  public LieuSurvenueApi getLieuSurvenue() {
    return lieuSurvenue;
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

  public static class LieuSurvenueApi {

    @JsonProperty(value = "codePostal", required = true)
    private String codePostal;

    @JsonProperty(value = "commune", required = true)
    private String commune;

    @JsonProperty(value = "natureLieu", required = true)
    private String natureLieu;

    @JsonProperty("etablissementSanitaireEtSocial")
    private EtablissementSanitaireEtSocialApi etablissementSanitaireEtSocial;

    @JsonProperty("domicile")
    private DomicileApi domicile;

    @JsonProperty("trajet")
    private TrajetApi trajet;

    @JsonProperty("cabinetMedical")
    private CabinetMedicalApi cabinetMedical;

    public String getCodePostal() {
      return codePostal;
    }

    public String getCommune() {
      return commune;
    }

    public String getNatureLieu() {
      return natureLieu;
    }

    public EtablissementSanitaireEtSocialApi getEtablissementSanitaireEtSocial() {
      return etablissementSanitaireEtSocial;
    }

    public DomicileApi getDomicile() {
      return domicile;
    }

    public TrajetApi getTrajet() {
      return trajet;
    }

    public CabinetMedicalApi getCabinetMedical() {
      return cabinetMedical;
    }
  }
}
