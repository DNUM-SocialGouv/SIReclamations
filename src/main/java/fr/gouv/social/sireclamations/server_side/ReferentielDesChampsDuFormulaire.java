package fr.gouv.social.sireclamations.server_side;

import fr.gouv.social.sireclamations.hexagone.domain.ChampsArbreDeDecision;
import fr.gouv.social.sireclamations.server_side.utils.CsvReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Repository
public class ReferentielDesChampsDuFormulaire {
    private static final Logger logger = LoggerFactory.getLogger(ReferentielDesChampsDuFormulaire.class);
    private final Map<ChampsArbreDeDecision, String> champsPourArbreDeDecision = new EnumMap<>(ChampsArbreDeDecision.class);

    public ReferentielDesChampsDuFormulaire(@Value("${referentiel.formulaire.champs}") Resource csvResource) {
        List<String[]> lignes = CsvReader.readCsv(csvResource);

        for (int i = 1; i < lignes.size(); i++) {
            String[] ligne = lignes.get(i);

            // Ne prend que les lignes dont la colonne proprieteArbreDeDecision est remplie
            if (ligne.length < 5) {
                continue;
            }

            String id = ligne[1];
            String proprieteArbreDecision = ligne[4];

            if (proprieteArbreDecision != null && !proprieteArbreDecision.isEmpty()) {
                try {
                    ChampsArbreDeDecision enumValue = ChampsArbreDeDecision.valueOf(proprieteArbreDecision);
                    champsPourArbreDeDecision.put(enumValue, id);
                } catch (IllegalArgumentException e) {
                    logger.error("La valeur '{}' ne correspond à aucun ChampsArbreDeDecision valide", proprieteArbreDecision);
                }
            }
        }

        logger.info("Référentiel des champs initialisé avec {} entrées.", champsPourArbreDeDecision.size());
    }

    public Map<ChampsArbreDeDecision, String> getChampsPourArbreDeDecision() {
        return champsPourArbreDeDecision;
    }
}
