package fr.gouv.social.sireclamations.user_side.plateforme_telephonique;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DemarchesApi {

  @JsonProperty(value = "contactEtablissementOuPersonneResponsable", required = true)
  private ContactEtablissementApi contactEtablissementOuPersonneResponsable;

  @JsonProperty("contactAutreOrganisme")
  private ContactAutreOrganismeApi contactAutreOrganise;

  @JsonProperty("contactForcesDeLOrdre")
  private ContactForcesDeLOrdreApi contactForcesDeLOrdre;

  public ContactEtablissementApi getContactEtablissementOuPersonneResponsable() {
    return contactEtablissementOuPersonneResponsable;
  }

  public ContactForcesDeLOrdreApi getContactForcesDeLOrdre() {
    return contactForcesDeLOrdre;
  }

  public ContactAutreOrganismeApi getContactAutreOrganise() {
    return contactAutreOrganise;
  }

  public void setContactEtablissementOuPersonneResponsable(
      ContactEtablissementApi contactEtablissementOuPersonneResponsable) {
    this.contactEtablissementOuPersonneResponsable = contactEtablissementOuPersonneResponsable;
  }

  public void setContactAutreOrganise(ContactAutreOrganismeApi contactAutreOrganise) {
    this.contactAutreOrganise = contactAutreOrganise;
  }

  public void setContactForcesDeLOrdre(ContactForcesDeLOrdreApi contactForcesDeLOrdre) {
    this.contactForcesDeLOrdre = contactForcesDeLOrdre;
  }
}
