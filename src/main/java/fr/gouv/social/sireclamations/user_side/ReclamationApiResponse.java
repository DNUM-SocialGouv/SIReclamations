package fr.gouv.social.sireclamations.user_side;

import java.util.List;

public class ReclamationApiResponse {
    private int numeroDossier;
    private int codeSousCategorieEtablissement;
    private List<String> autoritesCompetentes;
    private List<String> contacts;

    public ReclamationApiResponse(int numeroDossier,
                                  int codeSousCategorieEtablissement,
                                  List<String> autoritesCompetentes,
                                  List<String> contacts) {
        this.numeroDossier = numeroDossier;
        this.codeSousCategorieEtablissement = codeSousCategorieEtablissement;
        this.autoritesCompetentes = autoritesCompetentes;
        this.contacts = contacts;
    }

    public int getNumeroDossier() {
        return numeroDossier;
    }

    public int getCodeSousCategorieEtablissement() {
        return codeSousCategorieEtablissement;
    }

    public List<String> getAutoritesCompetentes() {
        return autoritesCompetentes;
    }

    public List<String> getContacts() {
        return contacts;
    }
}
