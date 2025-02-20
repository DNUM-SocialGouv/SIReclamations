package fr.gouv.social.sireclamations.server_side;

import fr.gouv.social.sireclamations.hexagone.domain.ports.ReferentielDesAutoritesCompetentesParMisEnCauseADomicile;
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
public class ReferentielDesAutoritesCompetentesParMisEnCauseADomicileCsvAdapter
    implements ReferentielDesAutoritesCompetentesParMisEnCauseADomicile {

  private static final Logger logger =
      LoggerFactory.getLogger(
          ReferentielDesAutoritesCompetentesParMisEnCauseADomicileCsvAdapter.class);
  private final Map<String, String> misEnCauseToAutorite = new HashMap<>();

  public ReferentielDesAutoritesCompetentesParMisEnCauseADomicileCsvAdapter(
      @Value("${referentiel.domicile.misEnCause}") Resource csvResource) {
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
          String libelleMisEnCauseDomicile = columns[0].trim();
          String autoriteCompetente = columns[1].trim();
          if (!libelleMisEnCauseDomicile.isEmpty() && !autoriteCompetente.isEmpty()) {
            misEnCauseToAutorite.put(libelleMisEnCauseDomicile, autoriteCompetente);
          }
        }
      }
    } catch (Exception e) {
      logger.error("Erreur lors de la lecture du fichier CSV des mis en cause à domicile : ", e);
    }
  }

  @Override
  public String recupererAutoriteCompetente(String libelleDuMisEnCauseADomicile) {
    if (libelleDuMisEnCauseADomicile == null || libelleDuMisEnCauseADomicile.isEmpty()) {
      logger.warn("Le libellé du mis en cause à domicile est null ou vide.");
      return null;
    }

    return misEnCauseToAutorite.getOrDefault(libelleDuMisEnCauseADomicile, null);
  }
}
