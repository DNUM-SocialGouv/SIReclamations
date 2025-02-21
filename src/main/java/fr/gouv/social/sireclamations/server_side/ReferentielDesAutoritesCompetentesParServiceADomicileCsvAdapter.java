package fr.gouv.social.sireclamations.server_side;

import fr.gouv.social.sireclamations.hexagone.domain.ports.ReferentielDesAutoritesCompetentesParServicesADomicile;
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
public class ReferentielDesAutoritesCompetentesParServiceADomicileCsvAdapter
    implements ReferentielDesAutoritesCompetentesParServicesADomicile {

  private static final Logger logger =
      LoggerFactory.getLogger(
          ReferentielDesAutoritesCompetentesParServiceADomicileCsvAdapter.class);
  private final Map<String, String> serviceToAutorite = new HashMap<>();

  public ReferentielDesAutoritesCompetentesParServiceADomicileCsvAdapter(
      @Value("${referentiel.domicile.services}") Resource csvResource) {
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
          String serviceADomicile = columns[0].trim();
          String autoriteCompetente = columns[1].trim();
          if (!serviceADomicile.isEmpty() && !autoriteCompetente.isEmpty()) {
            serviceToAutorite.put(serviceADomicile, autoriteCompetente);
          }
        }
      }
    } catch (Exception e) {
      logger.error("Erreur lors de la lecture du fichier CSV des services à domicile : ", e);
    }
  }

  @Override
  public String recupererAutoriteCompetente(String serviceADomicile) {
    if (serviceADomicile == null || serviceADomicile.isEmpty()) {
      logger.warn("Le service à domicile est null ou vide.");
      return null;
    }

    return serviceToAutorite.getOrDefault(serviceADomicile, null);
  }
}
