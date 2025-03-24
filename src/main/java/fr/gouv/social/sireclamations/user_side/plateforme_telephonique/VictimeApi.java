package fr.gouv.social.sireclamations.user_side.plateforme_telephonique;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class VictimeApi {

  @JsonProperty(value = "civilite", required = true)
  @NotNull(message = "La civilité de la victime est obligatoire")
  private Civilite civilite;

  @JsonProperty(value = "nom", required = true)
  @NotNull(message = "Le nom de la victime est obligatoire")
  private String nom;

  @JsonProperty(value = "prenom", required = true)
  @NotNull(message = "Le prénom de la victime est obligatoire")
  private String prenom;

  @JsonProperty("email")
  @Email(message = "L'email de la victime doit être valide")
  private String email;

  @JsonProperty("adresse")
  private String adresse;

  @JsonProperty(value = "telephone", required = true)
  @Pattern(
      regexp = "^\\d{10}$",
      message = "Le numéro de téléphone de la victime doit être un nombre de 10 chiffres")
  private String telephone;

  @JsonProperty(value = "trancheAge", required = true)
  @NotNull(message = "La tranche d'âge est obligatoire")
  @Pattern(
      regexp = "^(\\-18|18-29|30-59|60-79|\\>= 80|Inconnu)$",
      message =
          "La tranche d'âge doit être l'une des valeurs suivantes : -18, 18-29, 30-59, 60-79, >= 80, Inconnu")
  private String trancheAge;

  @JsonProperty(value = "enSituationDeHandicap", required = true)
  @NotNull(message = "Il faut indiquer si la victime est en situation de handicap")
  private boolean enSituationDeHandicap;

  @JsonProperty(value = "anonymatMisEnCauseDemande", required = true)
  @NotNull(
      message = "Il faut indiquer si la victime souhaite rester anonyme vis-à-vis du mis en cause")
  @Pattern(
      regexp = "^(Oui|Non|Inconnu)$",
      message =
          "L'anonymat vis-à-vis du mis en cause doit être l'une des valeurs suivantes : Oui, Non, Inconnu")
  private String anonymatMisEnCauseDemande;

  @JsonProperty(value = "autresPersonnesVictimes", required = true)
  @NotNull(message = "Il faut indiquer s'il y a d'autres personnes victimes")
  @Pattern(
      regexp = "^(Oui|Non|Inconnu)$",
      message =
          "La présence d'autres personnes victimes doit être l'une des valeurs suivantes : Oui, Non, Inconnu")
  private String autresPersonnesVictimes;
}
