package fr.gouv.social.sireclamations.user_side;

import fr.gouv.social.sireclamations.hexagone.domain.LieuDeSurvenue;

import java.util.List;
import java.util.Set;

public class ReclamationApiResponse {
    private int numeroDossier;
    private LieuDeSurvenueApiResponse lieuDeSurvenue;
    private Set<String> autoritesCompetentes;
    private List<String> contacts;

    public ReclamationApiResponse(int numeroDossier,
                                  Set<String> autoritesCompetentes,
                                  List<String> contacts,
                                  LieuDeSurvenueApiResponse lieuDeSurvenue) {
        this.numeroDossier = numeroDossier;
        this.autoritesCompetentes = autoritesCompetentes;
        this.contacts = contacts;
        this.lieuDeSurvenue = lieuDeSurvenue;
    }

    public int getNumeroDossier() {
        return numeroDossier;
    }

    public LieuDeSurvenueApiResponse getLieuDeSurvenue() {
        return lieuDeSurvenue;
    }

    public Set<String> getAutoritesCompetentes() {
        return autoritesCompetentes;
    }

    public List<String> getContacts() {
        return contacts;
    }
}
