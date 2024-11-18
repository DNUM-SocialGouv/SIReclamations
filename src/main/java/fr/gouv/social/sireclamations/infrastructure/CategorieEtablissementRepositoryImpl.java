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

    private final Map<String, List<String>> autoriteCompetenteMap = new HashMap<>();

    public CategorieEtablissementRepositoryImpl(@Value("${referentiel.categorie.etablissement}") Resource csvResource) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvResource.getInputStream()))) {
            reader.readLine();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(";");
                if (columns.length >= 5) {
                    String codeSousCategorie = columns[0].trim();

                    // Ajoute la première autorité compétente
                    List<String> autorites = new ArrayList<>();
                    if (!columns[4].trim().isEmpty()) {
                        autorites.add(columns[4].trim());
                    }

                    // Ajoute la deuxième autorité competente
                    if (columns.length > 5 && !columns[5].trim().isEmpty()) {
                        autorites.add(columns[5].trim());
                    }
                    autoriteCompetenteMap.put(codeSousCategorie, autorites);
                }
            }
        }
    }

    @Override
    public List<String> recupererAutoriteCompetenteParCodeSousCategorieEtablissement(String codeSousCategorieEtablissement) {
        return autoriteCompetenteMap.getOrDefault(codeSousCategorieEtablissement, Collections.emptyList());
    }
}
