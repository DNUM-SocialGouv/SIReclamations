package fr.gouv.social.sireclamations.server_side;

import fr.gouv.social.sireclamations.hexagone.domain.ports.ReferentielDesAutoritesCompetentesParMisEnCausePourMaltraitance;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class ReferentielDesAutoritesCompetentesParMisEnCausePourMaltraitanceCsvAdapter
    implements ReferentielDesAutoritesCompetentesParMisEnCausePourMaltraitance {

  private static final Logger logger =
      LoggerFactory.getLogger(
          ReferentielDesAutoritesCompetentesParMisEnCausePourMaltraitanceCsvAdapter.class);
  private final Map<String, String> misEnCauseToAutorite = new HashMap<>();

  public ReferentielDesAutoritesCompetentesParMisEnCausePourMaltraitanceCsvAdapter(
      @Value("${referentiel.maltraitance.misEnCause}") Resource csvResource) {
    try (BufferedReader reader =
        new BufferedReader(
            new InputStreamReader(csvResource.getInputStream(), StandardCharsets.UTF_8))) {
      String line;
      boolean isHeader = true; // Ignorer la première ligne (en-tête)

      while ((line = reader.readLine()) != null) {
        if (isHeader) {
          isHeader = false;
          continue;
        }

        String[] columns = line.split(";");
        if (columns.length >= 2) {
          String libelleMisEnCause = columns[0].trim();
          String autoriteCompetente = columns[1].trim();
          if (!libelleMisEnCause.isEmpty() && !autoriteCompetente.isEmpty()) {
            misEnCauseToAutorite.put(libelleMisEnCause, autoriteCompetente);
          }
        }
      }
    } catch (Exception e) {
      logger.error(
          "Erreur lors de la lecture du fichier CSV des mis en cause pour maltraitance : ", e);
    }
  }

  @Override
  public String recupererAutoriteCompetente(String libelleDuMisEnCausePourMaltraitance) {
    if (libelleDuMisEnCausePourMaltraitance == null
        || libelleDuMisEnCausePourMaltraitance.isEmpty()) {
      logger.warn("Le libellé du mis en cause pour maltraitance est null ou vide.");
      return null;
    }

    return misEnCauseToAutorite.getOrDefault(libelleDuMisEnCausePourMaltraitance, null);
  }
}
