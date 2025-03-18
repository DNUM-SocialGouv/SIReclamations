package fr.gouv.social.sireclamations.user_side;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class DossierDeReclamationDeLaPlateformeTelephoniqueApi {

  @JsonProperty(value = "id", required = true)
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

  public static class DeclarantApi {

    @JsonProperty(value = "civilite", required = true)
    private String civilite;

    @JsonProperty(value = "nom", required = true)
    private String nom;

    @JsonProperty(value = "prenom", required = true)
    private String prenom;

    @JsonProperty("email")
    private String email;

    @JsonProperty(value = "telephone", required = true)
    private String telephone;

    @JsonProperty(value = "estLaVictime", required = true)
    private boolean estLaVictime;

    @JsonProperty("langueEtrangere")
    private boolean langueEtrangere;

    @JsonProperty(value = "lienVictime", required = true)
    private String lienVictime;

    @JsonProperty("profession")
    private String profession;

    @JsonProperty(value = "victimeInformeeDemarche", required = true)
    private String victimeInformeeDemarche;

    @JsonProperty(value = "anonymatVictimeDemande", required = true)
    private boolean anonymatVictimeDemande;

    @JsonProperty(value = "anonymatMisEnCauseDemande", required = true)
    private boolean anonymatMisEnCauseDemande;

    @JsonProperty(value = "suiviDemande", required = true)
    private boolean suiviDemande;
  }

  public static class VictimeApi {

    @JsonProperty(value = "civilite", required = true)
    private String civilite;

    @JsonProperty(value = "nom", required = true)
    private String nom;

    @JsonProperty(value = "prenom", required = true)
    private String prenom;

    @JsonProperty("email")
    private String email;

    @JsonProperty("telephone")
    private String telephone;

    @JsonProperty(value = "trancheAge", required = true)
    private String trancheAge;

    @JsonProperty(value = "enSituationDeHandicap", required = true)
    private boolean enSituationDeHandicap;

    @JsonProperty(value = "anonymatMisEnCauseDemande", required = true)
    private String anonymatMisEnCauseDemande;

    @JsonProperty(value = "autresPersonnesVictimes", required = true)
    private String autresPersonnesVictimes;
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

  public static class EtablissementSanitaireEtSocialApi {

    @JsonProperty(value = "et_finess", required = true)
    private String etFiness;

    @JsonProperty("nomEtablissement")
    private String nomEtablissement;

    @JsonProperty("typeDeMisEnCause")
    private String typeDeMisEnCause;

    @JsonProperty(value = "codeCategorieEtablissement", required = true)
    private String codeCategorieEtablissement;

    public String getEtFiness() {
      return etFiness;
    }

    public String getNomEtablissement() {
      return nomEtablissement;
    }

    public String getTypeDeMisEnCause() {
      return typeDeMisEnCause;
    }

    public String getCodeCategorieEtablissement() {
      return codeCategorieEtablissement;
    }
  }

  public static class DomicileApi {

    @JsonProperty(value = "adresse", required = true)
    private String adresse;

    @JsonProperty("serviceADomicile")
    private String serviceADomicile;

    public String getAdresse() {
      return adresse;
    }

    public String getServiceADomicile() {
      return serviceADomicile;
    }
  }

  public static class TrajetApi {

    @JsonProperty(value = "typeDeTransport", required = true)
    private String typeDeTransport;

    @JsonProperty("nomSociete")
    private String nomSociete;

    @JsonProperty("typeDeMisEnCause")
    private String typeDeMisEnCause;

    public String getTypeDeMisEnCause() {
      return typeDeMisEnCause;
    }
  }

  public static class CabinetMedicalApi {

    @JsonProperty("informations")
    private String informations;

    @JsonProperty("adresse")
    private String adresse;

    @JsonProperty("typeDeMisEnCause")
    private String typeDeMisEnCause;

    public String getAdresse() {
      return adresse;
    }

    public String getTypeDeMisEnCause() {
      return typeDeMisEnCause;
    }
  }

  public static class MisEnCauseApi {

    @JsonProperty(value = "typeDeMisEnCause", required = true)
    private String typeDeMisEnCause;

    @JsonProperty("rpps")
    private String rpps;

    @JsonProperty("civilite")
    private String civilite;

    @JsonProperty("nom")
    private String nom;

    @JsonProperty("prenom")
    private String prenom;

    @JsonProperty("profession")
    private String profession;

    public String getTypeDeMisEnCause() {
      return typeDeMisEnCause;
    }
  }

  public static class DescriptionApi {

    @JsonProperty(value = "maltraitance", required = true)
    private boolean maltraitance;

    @JsonProperty("typeDeMaltraitance")
    private List<String> typeDeMaltraitance;

    @JsonProperty(value = "typesDeFaits", required = true)
    private List<String> typesDeFaits;

    @JsonProperty("dateSurvenue")
    private String dateSurvenue;

    @JsonProperty(value = "consequenceSurLaVictime", required = true)
    private List<String> consequenceSurLaVictime;

    @JsonProperty("situationToujoursActuelle")
    private String situationToujoursActuelle;

    @JsonProperty("dateDeFin")
    private String dateDeFin;

    @JsonProperty(value = "description", required = true)
    private String description;

    public boolean getMaltraitance() {
      return maltraitance;
    }

    public List<String> getTypesDeFaits() {
      return typesDeFaits;
    }
  }

  public static class DemarchesApi {

    @JsonProperty(value = "contactEtablissementOuPersonneResponsable", required = true)
    private ContactEtablissementApi contactEtablissementOuPersonneResponsable;

    @JsonProperty("contactAutreOrganisme")
    private ContactAutreOrganismeApi contactAutreOrganise;

    @JsonProperty("contactForcesDeLOrdre")
    private ContactForcesDeLOrdreApi contactForcesDeLOrdre;
  }

  public static class ContactEtablissementApi {

    @JsonProperty(value = "contactEffectue", required = true)
    private boolean contactEffectue;

    @JsonProperty("dateDemarche")
    private String dateDemarche;

    @JsonProperty("reponseObtenue")
    private boolean reponseObtenue;

    @JsonProperty("description")
    private String description;
  }

  public static class ContactAutreOrganismeApi {

    @JsonProperty("organismesSaisis")
    private String organismesSaisis;
  }

  public static class ContactForcesDeLOrdreApi {

    @JsonProperty("juridiction")
    private String juridiction;

    @JsonProperty("dateDemarche")
    private String dateDemarche;
  }
}
