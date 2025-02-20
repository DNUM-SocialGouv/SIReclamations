package fr.gouv.social.sireclamations.server_side;

import fr.gouv.social.sireclamations.hexagone.domain.ports.ReferentielDesAutoritesCompetentesParMotifs;
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
public class ReferentielDesAutoritesCompetentesParMotifsCsvAdapter
    implements ReferentielDesAutoritesCompetentesParMotifs {

  private static final Logger logger =
      LoggerFactory.getLogger(ReferentielDesAutoritesCompetentesParMotifsCsvAdapter.class);
  private final Map<String, String> libelleMotifToAutorite = new HashMap<>();

  public ReferentielDesAutoritesCompetentesParMotifsCsvAdapter(
      @Value("${referentiel.motifs}") Resource csvResource) {
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
          String libelleMotif = columns[0].trim();
          String autoriteCompetente = columns[1].trim();
          if (!libelleMotif.isEmpty() && !autoriteCompetente.isEmpty()) {
            libelleMotifToAutorite.put(libelleMotif, autoriteCompetente);
          }
        }
      }
    } catch (Exception e) {
      logger.error("Erreur lors de la lecture du fichier CSV des motifs : ", e);
    }
  }

  @Override
  public String recupererAutoriteCompetente(String libelleMotif) {
    if (libelleMotif == null || libelleMotif.isEmpty()) {
      logger.warn("Le libellé du motif est null ou vide.");
      return null;
    }

    return libelleMotifToAutorite.getOrDefault(libelleMotif, null);
  }
}
