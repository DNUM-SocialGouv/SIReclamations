package fr.gouv.social.sireclamations.user_side.plateforme_telephonique;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class DeclarantApi {

  @JsonProperty(value = "civilite", required = true)
  @NotNull(message = "La civilité est obligatoire")
  private Civilite civilite;

  @JsonProperty(value = "nom", required = true)
  @NotNull(message = "Le nom est obligatoire")
  private String nom;

  @JsonProperty(value = "prenom", required = true)
  @NotNull(message = "Le prénom est obligatoire")
  private String prenom;

  @JsonProperty("email")
  @Email(message = "L'email doit être valide")
  private String email;

  @JsonProperty(value = "telephone", required = true)
  @NotNull(message = "Le numéro de téléphone du déclarant est obligatoire")
  @Pattern(
      regexp = "^\\d{10}$",
      message = "Le numéro de téléphone du déclarant doit être un nombre de 10 chiffres")
  private String telephone;

  @JsonProperty(value = "estLaVictime", required = true)
  @NotNull(message = "Il faut indiquer si le déclarant est la victime")
  private boolean estLaVictime;

  @JsonProperty("langueEtrangere")
  private boolean langueEtrangere;

  @JsonProperty(value = "lienVictime", required = true)
  @NotNull(message = "Le lien avec la victime est obligatoire")
  @Pattern(
      regexp = "^(Membre de la famille|Proche|Professionnel|Autre)$",
      message =
          "Le lien avec la victime doit être l'un des suivants : Membre de la famille, Proche, Professionnel, Autre")
  private String lienVictime;

  @JsonProperty("profession")
  private String profession;

  @JsonProperty(value = "victimeInformeeDemarche", required = true)
  @NotNull(message = "Il faut indiquer si la victime est informée de la démarche")
  @Pattern(
      regexp = "^(oui|non|non connu)$",
      message =
          "La victime informée de la démarche doit être l'une des valeurs suivantes : oui, non, non connu")
  private String victimeInformeeDemarche;

  @JsonProperty(value = "anonymatVictimeDemande", required = true)
  @NotNull(
      message = "Il faut indiquer si le déclarant souhaite rester anonyme vis à vis de la victime")
  private boolean anonymatVictimeDemande;

  @JsonProperty(value = "anonymatMisEnCauseDemande", required = true)
  @NotNull(
      message =
          "Il faut indiquer si le déclarant souhaite rester anonyme vis à vis du mis en cause")
  private boolean anonymatMisEnCauseDemande;

  @JsonProperty(value = "suiviDemande", required = true)
  @NotNull(message = "Il faut indiquer si le déclarant souhaite être contacté pour le suivi")
  private boolean suiviDemande;
}
