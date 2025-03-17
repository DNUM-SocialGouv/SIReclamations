package fr.gouv.social.sireclamations.hexagone.domain;

public class Trajet extends LieuDeSurvenue {

  public Trajet(Integer codePostal, String typeDeLieu) {
    super(codePostal, typeDeLieu);
  }

  @Override
  public CodeTypeDeLieu getCodeTypeDeLieu() {
    return CodeTypeDeLieu.DOM;
  }
}
