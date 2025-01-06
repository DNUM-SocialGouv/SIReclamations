package fr.gouv.social.sireclamations.server_side;

import fr.gouv.social.sireclamations.hexagone.domain.CodeTypeDuMisEnCause;
import fr.gouv.social.sireclamations.hexagone.domain.ports.ReferentielDesAutoritesCompetentesParTypeDeMisEnCause;
import fr.gouv.social.sireclamations.server_side.utils.CsvReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class ReferentielDesAutoritesCompetentesParTypeDeMisEnCauseCsvAdapter
    implements ReferentielDesAutoritesCompetentesParTypeDeMisEnCause {

  private static final Logger logger =
      LoggerFactory.getLogger(
          ReferentielDesAutoritesCompetentesParTypeDeMisEnCauseCsvAdapter.class);
  private final Map<CodeTypeDuMisEnCause, String> codeTypeToAutorite = new HashMap<>();

  public ReferentielDesAutoritesCompetentesParTypeDeMisEnCauseCsvAdapter(
      @Value("${referentiel.autorite.type-mec}") Resource csvResource) {
    List<String[]> lignes = CsvReader.readCsv(csvResource);

    // Ignorer la première ligne (en-tête)
    for (int i = 1; i < lignes.size(); i++) {
      String[] colonnes = lignes.get(i);
      if (colonnes.length >= 2 && !colonnes[0].trim().isEmpty() && !colonnes[1].trim().isEmpty()) {
        String codeType = colonnes[0].trim();
        String autorite = colonnes[1].trim();

        try {
          CodeTypeDuMisEnCause codeTypeDuMisEnCause = CodeTypeDuMisEnCause.valueOf(codeType);
          codeTypeToAutorite.put(codeTypeDuMisEnCause, autorite);
        } catch (IllegalArgumentException e) {
          // Logger un avertissement pour les codes inconnus
          logger.warn(
              "Le referentiel ne prend actuellement pas en charge le type de mis en cause suivant : {}",
              codeType);
        }
      }
    }
  }

  @Override
  public String recupererAutoriteCompetentePourUnTypeDeMisEnCause(
      CodeTypeDuMisEnCause codeTypeDuMisEnCause) {
    if (codeTypeDuMisEnCause == null) {
      logger.warn("CodeTypeDuMisEnCause est null.");
      return null;
    }

    return codeTypeToAutorite.getOrDefault(codeTypeDuMisEnCause, null);
  }
}
