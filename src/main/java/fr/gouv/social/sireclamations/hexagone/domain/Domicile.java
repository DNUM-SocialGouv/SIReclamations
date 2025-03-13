package fr.gouv.social.sireclamations.hexagone.domain;

public class Domicile implements LieuDeSurvenue {

  private final Integer codePostal;
  private final String adresse;
  private final String typeDeLieu;

  private final String service;

  public Domicile(Integer codePostal, String adresse, String typeDeLieu, String service) {
    this.codePostal = codePostal;
    this.adresse = adresse;
    this.typeDeLieu = typeDeLieu;
    this.service = service;
  }

  @Override
  public Integer getCodePostal() {
    return this.codePostal;
  }

  @Override
  public CodeTypeDeLieu getCodeTypeDeLieu() {
    return CodeTypeDeLieu.DOM;
  }

  @Override
  public String libelleTypeDeLieu() {
    return typeDeLieu;
  }

  public String getAdresse() {
    return adresse;
  }

  public String getService() {
    return service;
  }
}
