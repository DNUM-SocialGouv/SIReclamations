package fr.gouv.social.sireclamations.user_side;

import java.util.List;

public class ReclamationApiResponse {
    private String numeroDossier;
    private String codeSousCategorieEtablissement;
    private List<String> autoritesCompetentes;
    private List<String> contacts;

    public ReclamationApiResponse(String numeroDossier,
                                  String codeSousCategorieEtablissement,
                                  List<String> autoritesCompetentes,
                                  List<String> contacts) {
        this.numeroDossier = numeroDossier;
        this.codeSousCategorieEtablissement = codeSousCategorieEtablissement;
        this.autoritesCompetentes = autoritesCompetentes;
        this.contacts = contacts;
    }

    public String getNumeroDossier() {
        return numeroDossier;
    }

    public String getCodeSousCategorieEtablissement() {
        return codeSousCategorieEtablissement;
    }

    public List<String> getAutoritesCompetentes() {
        return autoritesCompetentes;
    }

    public List<String> getContacts() {
        return contacts;
    }
}
