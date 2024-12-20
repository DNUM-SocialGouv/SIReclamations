package fr.gouv.social.sireclamations.user_side;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LieuDeSurvenueApiResponse {
  private final Integer codePostal;
  private final String codeTypeDeLieu;
  private final String adresse; // Spécifique à Domicile
  private final String numeroFiness; // Spécifique à Etablissement
  private final Integer codeSousCategorie; // Spécifique à Etablissement
  private final String nom; // Spécifique à Etablissement

  public LieuDeSurvenueApiResponse(
      Integer codePostal,
      String codeTypeDeLieu,
      String adresse,
      String numeroFiness,
      Integer codeSousCategorie,
      String nom) {
    this.codePostal = codePostal;
    this.codeTypeDeLieu = codeTypeDeLieu;
    this.adresse = adresse;
    this.numeroFiness = numeroFiness;
    this.codeSousCategorie = codeSousCategorie;
    this.nom = nom;
  }

  public Integer getCodePostal() {
    return codePostal;
  }

  public String getCodeTypeDeLieu() {
    return codeTypeDeLieu;
  }

  public String getAdresse() {
    return adresse;
  }

  public String getNumeroFiness() {
    return numeroFiness;
  }

  public Integer getCodeSousCategorie() {
    return codeSousCategorie;
  }

  public String getNom() {
    return nom;
  }
}
