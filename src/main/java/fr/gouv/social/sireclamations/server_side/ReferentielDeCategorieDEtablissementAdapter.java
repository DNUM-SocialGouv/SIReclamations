package fr.gouv.social.sireclamations.server_side;

import fr.gouv.social.sireclamations.hexagone.domain.port.ReferentielDeCategoriesDEtablissements;
import fr.gouv.social.sireclamations.server_side.utils.CsvReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

@Repository
public class ReferentielDeCategorieDEtablissementAdapter implements ReferentielDeCategoriesDEtablissements {

    private final Map<Integer, List<String>> autoritesCompetentesParCodeCategorieEtablissement = new HashMap<>();

    public ReferentielDeCategorieDEtablissementAdapter(@Value("${referentiel.categorie.etablissement}") Resource csvResource) throws IOException {
        List<String[]> lignes = CsvReader.readCsv(csvResource);
        if (lignes.isEmpty()) {
            throw new IllegalArgumentException("Le fichier CSV est vide.");
        }

        for (int i = 1; i < lignes.size(); i++) {  // Ignorer la première ligne (en-tête)
            String[] colonnes = lignes.get(i);
            if (colonnes.length >= 5) {
                int codeSousCategorie;
                try {
                    codeSousCategorie = Integer.parseInt(colonnes[0].trim());
                } catch (NumberFormatException e) {
                    // Ignorer les lignes avec une clé non valide
                    continue;
                }

                // Ajoute la première autorité compétente
                List<String> autorites = new ArrayList<>();
                if (!colonnes[4].trim().isEmpty()) {
                    autorites.add(colonnes[4].trim());
                }

                // Ajoute la deuxième autorité competente
                if (colonnes.length > 5 && !colonnes[5].trim().isEmpty()) {
                    autorites.add(colonnes[5].trim());
                }
                autoritesCompetentesParCodeCategorieEtablissement.put(codeSousCategorie, autorites);
            }
        }
    }

    @Override
    public List<String> recupererAutoritesCompetentesParCodeSousCategorieEtablissement(int codeSousCategorieEtablissement) {
        return autoritesCompetentesParCodeCategorieEtablissement.getOrDefault(codeSousCategorieEtablissement, Collections.emptyList());
    }
}
