package fr.gouv.social.sireclamations.user_side;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class DeposerReclamationRequest {
    @NotNull(message = "Le numéro de dossier est obligatoire.")
    @Positive(message = "Le numéro de dossier doit être un entier positif.")
    private int numeroDossier;

    public int getNumeroDossier() {
        return numeroDossier;
    }

    public void setNumeroDossier(int numeroDossier) {
        this.numeroDossier = numeroDossier;
    }
}


