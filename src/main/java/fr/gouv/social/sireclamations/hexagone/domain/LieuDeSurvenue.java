package fr.gouv.social.sireclamations.hexagone.domain;

public abstract class LieuDeSurvenue {

  private final Integer codePostal;
  private final String typeDeLieu;

  public LieuDeSurvenue(Integer codePostal, String typeDeLieu) {
    this.codePostal = codePostal;
    this.typeDeLieu = typeDeLieu;
  }

  public Integer getCodePostal() {
    return codePostal;
  }

  public String libelleTypeDeLieu() {
    return typeDeLieu;
  }

  public abstract CodeTypeDeLieu getCodeTypeDeLieu();
}
