package fr.gouv.social.sireclamations.user_side;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.stream.Stream;

public class DeposerReclamationRequest {
  @NotNull(message = "Le numéro de procédure est obligatoire.")
  @Positive(message = "Le numéro de procédure doit être un entier positif.")
  private final int numeroProcedure;

  @NotNull(message = "Le numéro de dossier est obligatoire.")
  @Positive(message = "Le numéro de dossier doit être un entier positif.")
  private int numeroDossier;

  private final Etat etat;
  private final LocalDateTime dateDepot;

  public DeposerReclamationRequest(int numeroProcedure, int numeroDossier, Etat etat, LocalDateTime dateDepot) {
    this.numeroProcedure = numeroProcedure;
    this.numeroDossier = numeroDossier;
    this.etat = etat;
    this.dateDepot = dateDepot;
  }

  public int getNumeroProcedure() {
    return numeroProcedure;
  }

  public int getNumeroDossier() {
    return numeroDossier;
  }

  public void setNumeroDossier(int numeroDossier) {
    this.numeroDossier = numeroDossier;
  }

  public Etat getEtat() { return etat; }

  public LocalDateTime getDateDepot() { return dateDepot; }

  public enum Etat {
    BROUILLON("brouillon"),
    EN_CONSTRUCTION("en_construction"),
    EN_INSTRUCTION("en_instruction"),
    ACCEPTE("accepte"),
    REFUSE("refuse");

    private final String value;

    Etat(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    public static boolean isValid(String input) {
      return Stream.of(Etat.values())
              .anyMatch(state -> state.value.equals(input));
    }
  }
}