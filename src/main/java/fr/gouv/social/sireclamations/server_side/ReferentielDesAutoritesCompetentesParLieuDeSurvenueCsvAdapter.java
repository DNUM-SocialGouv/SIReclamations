package fr.gouv.social.sireclamations.server_side;

import fr.gouv.social.sireclamations.hexagone.domain.ports.ReferentielDesAutoritesCompetentesParLieuDeSurvenue;
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
public class ReferentielDesAutoritesCompetentesParLieuDeSurvenueCsvAdapter
    implements ReferentielDesAutoritesCompetentesParLieuDeSurvenue {

  private static final Logger logger =
      LoggerFactory.getLogger(ReferentielDesAutoritesCompetentesParLieuDeSurvenueCsvAdapter.class);
  private final Map<String, String> lieuToAutorite = new HashMap<>();

  public ReferentielDesAutoritesCompetentesParLieuDeSurvenueCsvAdapter(
      @Value("${referentiel.lieu-survenue}") Resource csvResource) {
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
          String libelleLieuDeSurvenue = columns[0].trim();
          String autoriteCompetente = columns[1].trim();
          if (!libelleLieuDeSurvenue.isEmpty() && !autoriteCompetente.isEmpty()) {
            lieuToAutorite.put(libelleLieuDeSurvenue, autoriteCompetente);
          }
        }
      }
    } catch (Exception e) {
      logger.error("Erreur lors de la lecture du fichier CSV des lieux de survenue : ", e);
    }
  }

  @Override
  public String recupererAutoriteCompetente(String libelleDuLieuDeSurvenue) {
    if (libelleDuLieuDeSurvenue == null || libelleDuLieuDeSurvenue.isEmpty()) {
      logger.warn("Le libellé du lieu de survenue est null ou vide.");
      return null;
    }

    return lieuToAutorite.getOrDefault(libelleDuLieuDeSurvenue, null);
  }
}
