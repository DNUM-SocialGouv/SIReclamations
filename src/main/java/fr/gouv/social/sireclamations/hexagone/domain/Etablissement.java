package fr.gouv.social.sireclamations.hexagone.domain;

public class Etablissement extends LieuDeSurvenue {
  private final String numeroFiness;
  private final int codeSousCategorie;
  private final String nom;

  public Etablissement(
      String numeroFiness, int codeSousCategorie, int codePostal, String nom, String typeDeLieu) {
    super(codePostal, typeDeLieu);
    this.numeroFiness = numeroFiness;
    this.codeSousCategorie = codeSousCategorie;
    this.nom = nom;
  }

  public String getNumeroFiness() {
    return numeroFiness;
  }

  public int getCodeSousCategorie() {
    return codeSousCategorie;
  }

  public String getNom() {
    return nom;
  }

  @Override
  public CodeTypeDeLieu getCodeTypeDeLieu() {
    return CodeTypeDeLieu.ETAB;
  }
}
