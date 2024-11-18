package fr.gouv.social.sireclamations.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Repository
public class CategorieEtablissementRepositoryImpl implements CategorieEtablissementRepository {

    private final Map<String, String> autoriteCompetenteMap = new HashMap<>();

    public CategorieEtablissementRepositoryImpl(@Value("${referentiel.categorie.etablissement}") String csvFilePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            reader.readLine();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(";");
                if (columns.length >= 5) {
                    String codeSousCategorie = columns[0].trim();
                    String autoriteCompetente = columns[4].trim();
                    autoriteCompetenteMap.put(codeSousCategorie, autoriteCompetente);
                }
            }
        }
    }

    @Override
    public String recupererAutoriteCompetenteParCodeSousCategorieEtablissement(String codeSousCategorieEtablissement) {
        return autoriteCompetenteMap.get(codeSousCategorieEtablissement);
    }
}
