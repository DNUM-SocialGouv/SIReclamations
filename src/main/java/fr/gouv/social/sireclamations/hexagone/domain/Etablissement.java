package fr.gouv.social.sireclamations.hexagone.domain;

public class Etablissement implements LieuDeSurvenue {
  private String numeroFiness;
  private int codeSousCategorie;
  private int codePostal;
  private String nom;

  private String typeDeLieu;

  public Etablissement(
      String numeroFiness, int codeSousCategorie, int codePostal, String nom, String typeDeLieu) {
    this.numeroFiness = numeroFiness;
    this.codeSousCategorie = codeSousCategorie;
    this.codePostal = codePostal;
    this.nom = nom;
    this.typeDeLieu = typeDeLieu;
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
  public Integer getCodePostal() {
    return this.codePostal;
  }

  @Override
  public CodeTypeDeLieu getCodeTypeDeLieu() {
    return CodeTypeDeLieu.ETAB;
  }

  @Override
  public String libelleTypeDeLieu() {
    return typeDeLieu;
  }
}
