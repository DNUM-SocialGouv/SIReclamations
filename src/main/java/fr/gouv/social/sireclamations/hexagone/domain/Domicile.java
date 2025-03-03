package fr.gouv.social.sireclamations.hexagone.domain;

public class Domicile implements LieuDeSurvenue {
  private final Integer codePostal;
  private final String adresse;
  private final String typeDeLieu;

  public Domicile(Integer codePostal, String adresse, String typeDeLieu) {
    this.codePostal = codePostal;
    this.adresse = adresse;
    this.typeDeLieu = typeDeLieu;
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
}
