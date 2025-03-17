package fr.gouv.social.sireclamations.hexagone.domain;

public class Trajet implements LieuDeSurvenue {

  private final Integer codePostal;
  private final String typeDeLieu;

  public Trajet(Integer codePostal, String typeDeLieu) {
    this.codePostal = codePostal;
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
}
