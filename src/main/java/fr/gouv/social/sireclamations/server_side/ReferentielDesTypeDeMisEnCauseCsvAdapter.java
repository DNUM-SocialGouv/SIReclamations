package fr.gouv.social.sireclamations.server_side;

import fr.gouv.social.sireclamations.hexagone.domain.CodeTypeDuMisEnCause;
import fr.gouv.social.sireclamations.hexagone.domain.ports.ReferentielDesTypeDeMisEnCause;
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
public class ReferentielDesTypeDeMisEnCauseCsvAdapter implements ReferentielDesTypeDeMisEnCause {

    private static final Logger logger = LoggerFactory.getLogger(ReferentielDesTypeDeMisEnCauseCsvAdapter.class);
    private final Map<String, CodeTypeDuMisEnCause> libelleToCodeTypeMEC = new HashMap<>();

    public ReferentielDesTypeDeMisEnCauseCsvAdapter(@Value("${referentiel.formulaire.type-mec}") Resource csvResource){
        List<String[]> lignes = CsvReader.readCsv(csvResource);

        // Ignorer la première ligne (en-tête)
        for (int i = 1; i < lignes.size(); i++) {
            String[] colonnes = lignes.get(i);
            if (colonnes.length >= 3 && !colonnes[0].trim().isEmpty() && !colonnes[2].trim().isEmpty()) {
                String libelle = colonnes[0].trim();
                try {
                    CodeTypeDuMisEnCause codeTypeMEC = CodeTypeDuMisEnCause.valueOf(colonnes[2].trim());
                    libelleToCodeTypeMEC.put(libelle, codeTypeMEC);
                } catch (IllegalArgumentException e) {
                    // Ignorer les codes non valides
                    logger.warn("Le referentiel ne prend actuellement pas en charge le type de mis en cause avec pour libellé : {}", libelle);
                }
            }
        }
    }

    @Override
    public CodeTypeDuMisEnCause recupererTypeDuMisEnCause(String libelleDuMisEnCause) {
        return libelleToCodeTypeMEC.getOrDefault(libelleDuMisEnCause, null);
    }
}
