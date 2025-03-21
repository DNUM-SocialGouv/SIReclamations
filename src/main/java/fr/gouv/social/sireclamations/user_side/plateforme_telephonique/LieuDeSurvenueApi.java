package fr.gouv.social.sireclamations.user_side.plateforme_telephonique;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class LieuDeSurvenueApi {

  @NotNull(message = "Le champ 'codePostal' est requis.")
  @Pattern(
      regexp = "^(?:0[1-9]|[1-8]\\d|9[0-8]\\d{2}|2[AB]\\d{2}|971|972|973|974|976\\d{2})$",
      message = "Le code postal doit être valide.")
  @JsonProperty("codePostal")
  private String codePostal;

  @NotNull(message = "Le champ 'commune' est requis.")
  @JsonProperty("commune")
  private String commune;

  @NotNull(message = "Le champ 'natureLieu' est requis.")
  @JsonProperty("natureLieu")
  private String natureLieu;

  @JsonProperty("etablissementSanitaireEtSocial")
  private EtablissementSanitaireEtSocialApi etablissementSanitaireEtSocialapi;

  @JsonProperty("domicile")
  private DomicileApi domicileApi;

  @JsonProperty("trajet")
  private TrajetApi trajetApi;

  @JsonProperty("cabinetMedical")
  private CabinetMedicalApi cabinetMedicalApi;

  public String getCodePostal() {
    return codePostal;
  }

  public String getCommune() {
    return commune;
  }

  public String getNatureLieu() {
    return natureLieu;
  }

  public EtablissementSanitaireEtSocialApi getEtablissementSanitaireEtSocialapi() {
    return etablissementSanitaireEtSocialapi;
  }

  public DomicileApi getDomicileApi() {
    return domicileApi;
  }

  public TrajetApi getTrajetApi() {
    return trajetApi;
  }

  public CabinetMedicalApi getCabinetMedicalApi() {
    return cabinetMedicalApi;
  }

  public void setCodePostal(String codePostal) {
    this.codePostal = codePostal;
  }

  public void setCommune(String commune) {
    this.commune = commune;
  }

  public void setNatureLieu(String natureLieu) {
    this.natureLieu = natureLieu;
  }

  public void setEtablissementSanitaireEtSocialapi(
      EtablissementSanitaireEtSocialApi etablissementSanitaireEtSocialapi) {
    this.etablissementSanitaireEtSocialapi = etablissementSanitaireEtSocialapi;
  }

  public void setDomicileApi(DomicileApi domicileApi) {
    this.domicileApi = domicileApi;
  }

  public void setTrajetApi(TrajetApi trajetApi) {
    this.trajetApi = trajetApi;
  }

  public void setCabinetMedicalApi(CabinetMedicalApi cabinetMedicalApi) {
    this.cabinetMedicalApi = cabinetMedicalApi;
  }
}
