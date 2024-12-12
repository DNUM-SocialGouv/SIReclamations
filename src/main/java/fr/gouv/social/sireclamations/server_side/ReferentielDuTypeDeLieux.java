package fr.gouv.social.sireclamations.server_side;

import fr.gouv.social.sireclamations.hexagone.domain.CodeTypeDeLieu;
import fr.gouv.social.sireclamations.server_side.utils.CsvReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ReferentielDuTypeDeLieux {

    private static final Logger logger = LoggerFactory.getLogger(ReferentielDuTypeDeLieux.class);
    private final Map<String, CodeTypeDeLieu> libelleToCodeTypeLieu = new HashMap<>();

    public ReferentielDuTypeDeLieux(@Value("${referentiel.formulaire.type-lieux}") Resource csvResource) {
        List<String[]> lignes = CsvReader.readCsv(csvResource);
        for (int i = 1; i < lignes.size(); i++) {
            String[] colonnes = lignes.get(i);
            if (colonnes.length >= 3 && !colonnes[0].trim().isEmpty() && !colonnes[2].trim().isEmpty()) {
                String libelle = colonnes[0].trim();
                try {
                    CodeTypeDeLieu codeTypeLieu = CodeTypeDeLieu.valueOf(colonnes[2].trim());
                    libelleToCodeTypeLieu.put(libelle, codeTypeLieu);
                } catch (IllegalArgumentException e) {
                    // Ignorer les codes non valides
                    logger.warn("Le referentiel ne prend actuellement pas en charge le type de mis en cause avec pour libellé : {}", libelle);
                }
            }
        }
    }

    public CodeTypeDeLieu recupererCodeTypeDeLieuxAPartirDuLibelle(String libelleDuLieuxDeSurvenu) {
        //Appeler le csv mappingFormulaireV2-typeLieux.csv qui via le libellé retourne le code
        return libelleToCodeTypeLieu.getOrDefault(libelleDuLieuxDeSurvenu, null);
    }
}
