package fr.gouv.social.sireclamations.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Repository
public class CategorieEtablissementRepositoryImpl implements CategorieEtablissementRepository {

    private final Map<String, List<String>> autoritesCompetentesParCodeCategorieEtablissement = new HashMap<>();

    public CategorieEtablissementRepositoryImpl(@Value("${referentiel.categorie.etablissement}") Resource csvResource) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvResource.getInputStream()))) {
            reader.readLine();
            String ligne;

            while ((ligne = reader.readLine()) != null) {
                String[] colonnes = ligne.split(";");
                if (colonnes.length >= 5) {
                    String codeSousCategorie = colonnes[0].trim();

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
    }

    @Override
    public List<String> recupererAutoriteCompetenteParCodeSousCategorieEtablissement(String codeSousCategorieEtablissement) {
        return autoritesCompetentesParCodeCategorieEtablissement.getOrDefault(codeSousCategorieEtablissement, Collections.emptyList());
    }
}
