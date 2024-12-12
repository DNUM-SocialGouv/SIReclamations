package fr.gouv.social.sireclamations.server_side;
import fr.gouv.social.sireclamations.hexagone.domain.ports.ReferentielDesContacts;
import fr.gouv.social.sireclamations.server_side.utils.CsvReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ReferentielDesContactsCsvAdapter implements ReferentielDesContacts {

    private final Map<String, Map<String, String>> csvData = new HashMap<>();

    public ReferentielDesContactsCsvAdapter(@Value("${referentiel.autorite.contact}") Resource csvResource){
        List<String[]> lignes = CsvReader.readCsv(csvResource);
        if (lignes.isEmpty()) {
            throw new IllegalArgumentException("Le fichier CSV est vide.");
        }
        // Lire l'en-tête pour mapper les colonnes
        String[] headers = lignes.get(0);
        Map<String, Integer> nomColonnesEtIndex = mapHeaderIndices(headers);

        // Charger les données dans csvData
        for (int i = 1; i < lignes.size(); i++) {  // Ignorer la première ligne (en-tête)
            String[] colonnes = lignes.get(i);
            if (colonnes.length == 0 || colonnes[0].trim().isEmpty()) continue;

            String code = colonnes[0].trim();
            Map<String, String> contacts = new HashMap<>();

            for (Map.Entry<String, Integer> colonne : nomColonnesEtIndex.entrySet()) {
                int indexColonne = colonne.getValue();
                String valeurColonne = colonne.getKey();
                if (indexColonne < colonnes.length && !colonnes[indexColonne].trim().isEmpty()) {
                    contacts.put(valeurColonne, colonnes[indexColonne].trim());
                }
            }
            csvData.put(code, contacts);
        }
    }

    @Override
    public List<String> recupererContacts(Integer codePostal, Set<String> autoriteCompetente) {
        if(codePostal == null) return Collections.emptyList();
        String codeDepartement = extraireCodeDepartement(codePostal);
        // Si pas de contacts pour ce département return liste vide
        if (!csvData.containsKey(codeDepartement)) {
            return Collections.emptyList();
        }

        Map<String, String> contactsParAutorite = csvData.get(codeDepartement);
        List<String> emails = new ArrayList<>();

        // Étape 3 : Récupérer les emails des colonnes spécifiées
        for (String autorite : autoriteCompetente) {
            String nomDeLaColonneDeContactParAutoritéRecherchée = "contacts" + autorite.toUpperCase();
            if (contactsParAutorite.containsKey(nomDeLaColonneDeContactParAutoritéRecherchée)) {
                emails.addAll(Arrays.asList(contactsParAutorite.get(nomDeLaColonneDeContactParAutoritéRecherchée).split("\\|")));
            }
        }

        // Étape 4 : Ajouter les emails de "contactsAutres" s'ils existent
        if (contactsParAutorite.containsKey("contactsAutres")) {
            emails.addAll(Arrays.asList(contactsParAutorite.get("contactsAutres").split("\\|")));
        }

        return emails.stream()
                .map(String::trim)  //supprime des eventuels espaces inutiles
                .distinct()         //supprime des eventuels doublons d'emails
                .collect(Collectors.toList());
    }

    private String extraireCodeDepartement(int codePostal) {
        var codePostalString = Integer.toString(codePostal);
        var codeDepartement = codePostalString.substring(0, 2);

        if (codeDepartement.equals("97")) {
            codeDepartement = codePostalString.substring(0, 3);
        } else if (codeDepartement.startsWith("2")) {
            codeDepartement = getCodeCorseParCodePostal(codePostalString);
        }
        return codeDepartement;
    }

    private String getCodeCorseParCodePostal(String codePostalString) {
            int code = Integer.parseInt(codePostalString);
            if (code >= 20000 && code <= 20190) {
                return "2A";
            } else if (code >= 20200 && code <= 20620) {
                return "2B";
            }
        return "Code postal non valide ou hors de Corse";
    }

    //map qui associe chaque colonne à son index dans le tableau
    private Map<String, Integer> mapHeaderIndices(String[] headers) {
        Map<String, Integer> columnIndices = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            columnIndices.put(headers[i].trim(), i);
        }
        return columnIndices;
    }
}



