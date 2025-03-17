package fr.gouv.social.sireclamations.hexagone.domain;

public class AutreEtablissement extends LieuDeSurvenue {

  private final String adresse;

  public AutreEtablissement(Integer codePostal, String adresse, String typeDeLieu) {
    super(codePostal, typeDeLieu);
    this.adresse = adresse;
  }

  public String getAdresse() {
    return adresse;
  }

  @Override
  public CodeTypeDeLieu getCodeTypeDeLieu() {
    return CodeTypeDeLieu.DOM;
  }
}
