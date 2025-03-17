package fr.gouv.social.sireclamations.hexagone.domain;

public class Domicile extends LieuDeSurvenue {

  private final String adresse;
  private final String service;

  public Domicile(Integer codePostal, String adresse, String typeDeLieu, String service) {
    super(codePostal, typeDeLieu);
    this.adresse = adresse;
    this.service = service;
  }

  public String getAdresse() {
    return adresse;
  }

  public String getService() {
    return service;
  }

  @Override
  public CodeTypeDeLieu getCodeTypeDeLieu() {
    return CodeTypeDeLieu.DOM;
  }
}
