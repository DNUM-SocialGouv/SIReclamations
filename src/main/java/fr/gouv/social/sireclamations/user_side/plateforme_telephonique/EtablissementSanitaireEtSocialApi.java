package fr.gouv.social.sireclamations.user_side.plateforme_telephonique;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.Objects;

public class EtablissementSanitaireEtSocialApi {

  @JsonProperty(value = "et_finess", required = true)
  @NotNull(message = "Le champ et_finess ne peut pas être nul")
  @Pattern(
      regexp = "\\d[AB\\d]\\d{7}",
      message = "Le champ et_finess doit correspondre au pattern d[AB\\d]\\d{7}")
  private String etFiness;

  @JsonProperty(value = "codeCategorieEtablissement", required = true)
  @NotNull(message = "Le champ codeCategorieEtablissement ne peut pas être nul")
  @Pattern(regexp = "\\d+", message = "Le champ 'codeCategorieEtablissement' doit être un entier")
  private String codeCategorieEtablissement;

  @JsonProperty("nomEtablissement")
  private String nomEtablissement;

  @JsonProperty(value = "typeDeMisEnCause", required = true)
  @TypeDeMisEnCauseProfessionnelValid(message = "La valeur de typeDeMisEnCause n'est pas valide")
  private TypeDeMisEnCauseProfessionnel typeDeMisEnCauseProfessionnel;

  public EtablissementSanitaireEtSocialApi() {
    // Ce constructeur est nécessaire pour la désérialisation
  }

  public EtablissementSanitaireEtSocialApi(
      String etFiness,
      String codeCategorieEtablissement,
      String nomEtablissement,
      TypeDeMisEnCauseProfessionnel typeDeMisEnCauseProfessionnel) {
    this.etFiness = etFiness;
    this.codeCategorieEtablissement = codeCategorieEtablissement;
    this.nomEtablissement = nomEtablissement;
    this.typeDeMisEnCauseProfessionnel = typeDeMisEnCauseProfessionnel;
  }

  public String getEtFiness() {
    return etFiness;
  }

  public String getCodeCategorieEtablissement() {
    return codeCategorieEtablissement;
  }

  public String getNomEtablissement() {
    return nomEtablissement;
  }

  public TypeDeMisEnCauseProfessionnel getTypeDeMisEnCause() {
    return typeDeMisEnCauseProfessionnel;
  }

  public void setEtFiness(String etFiness) {
    this.etFiness = etFiness;
  }

  public void setCodeCategorieEtablissement(String codeCategorieEtablissement) {
    this.codeCategorieEtablissement = codeCategorieEtablissement;
  }

  public void setNomEtablissement(String nomEtablissement) {
    this.nomEtablissement = nomEtablissement;
  }

  public void setTypeDeMisEnCause(TypeDeMisEnCauseProfessionnel typeDeMisEnCauseProfessionnel) {
    this.typeDeMisEnCauseProfessionnel = typeDeMisEnCauseProfessionnel;
  }

  public int getCodeCategorieEtablissementAsInt() throws NumberFormatException {
    return Integer.parseInt(this.codeCategorieEtablissement);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EtablissementSanitaireEtSocialApi that = (EtablissementSanitaireEtSocialApi) o;
    return etFiness.equals(that.etFiness)
        && codeCategorieEtablissement.equals(that.codeCategorieEtablissement)
        && Objects.equals(nomEtablissement, that.nomEtablissement)
        && typeDeMisEnCauseProfessionnel == that.typeDeMisEnCauseProfessionnel;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        etFiness, codeCategorieEtablissement, nomEtablissement, typeDeMisEnCauseProfessionnel);
  }

  @Override
  public String toString() {
    return "EtablissementSanitaireEtSocialApi{"
        + "etFiness='"
        + etFiness
        + '\''
        + ", codeCategorieEtablissement="
        + codeCategorieEtablissement
        + ", nomEtablissement='"
        + nomEtablissement
        + '\''
        + ", typeDeMisEnCause="
        + typeDeMisEnCauseProfessionnel
        + '}';
  }
}
