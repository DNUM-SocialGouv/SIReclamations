package fr.gouv.social.sireclamations.server_side.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.io.Resource;

public class CsvReader {
  /**
   * Lit un fichier CSV et retourne une liste de lignes, où chaque ligne est représentée par un
   * tableau de chaînes.
   *
   * @param csvResource Ressource CSV à lire.
   * @return Liste de lignes du fichier CSV.
   * @throws IOException En cas de problème de lecture du fichier.
   */
  public static List<String[]> readCsv(Resource csvResource) {
    List<String[]> lignes = new ArrayList<>();

    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(csvResource.getInputStream()))) {
      String ligne;
      while ((ligne = reader.readLine()) != null) {
        lignes.add(ligne.split(";"));
      }
    } catch (IOException e) {
      throw new RuntimeException("Erreur lors de la lecture du CSV", e);
    }

    return lignes;
  }
}
