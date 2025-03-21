package fr.gouv.social.sireclamations.user_side.plateforme_telephonique;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// Le validateur qui vérifie si la valeur fait partie de l'énumération
public class TypeDeMisEnCauseValidator
    implements ConstraintValidator<TypeDeMisEnCauseValid, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return true; // La validation @NotNull est déjà assurée
    }
    try {
      TypeDeMisEnCauseProfessionnel.valueOf(
          value); // Vérifie si la valeur fait partie de l'énumération
      return true;
    } catch (IllegalArgumentException e) {
      return false; // La valeur ne correspond à aucune constante de l'énumération
    }
  }
}
