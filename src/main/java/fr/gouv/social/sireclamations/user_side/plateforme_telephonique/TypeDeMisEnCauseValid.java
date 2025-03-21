package fr.gouv.social.sireclamations.user_side.plateforme_telephonique;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Annotation personnalisée de validation
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TypeDeMisEnCauseValidator.class)
public @interface TypeDeMisEnCauseValid {
  String message() default "Valeur non valide pour typeDeMisEnCause";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
