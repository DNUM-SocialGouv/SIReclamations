package fr.gouv.social.sireclamations.hexagone;

import fr.gouv.social.sireclamations.hexagone.domain.CodeTypeDeLieu;
import fr.gouv.social.sireclamations.hexagone.domain.LieuDeSurvenue;

public class Domicile implements LieuDeSurvenue {
    private final Integer codePostal;
    private final String adresse;

    public Domicile(Integer codePostal, String adresse) {
        this.codePostal = codePostal;
        this.adresse = adresse;
    }
    @Override
    public Integer getCodePostal() {
        return this.codePostal;
    }

    @Override
    public CodeTypeDeLieu getCodeTypeDeLieu() {
        return CodeTypeDeLieu.DOM;
    }

    public String getAdresse() {
        return adresse;
    }
}
