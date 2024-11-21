package fr.gouv.social.sireclamations.infrastructure;
import fr.gouv.social.sireclamations.hexagone.port.ContactsPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ContactsAdapter implements ContactsPort {

    private final Map<String, Map<String, String>> csvData = new HashMap<>();

    public ContactsAdapter(@Value("${referentiel.autorite.contact}") Resource csvResource) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvResource.getInputStream()))) {
            // Lire l'en-tête pour mapper les colonnes
            String headerLine = reader.readLine();
            if (headerLine == null) {
                throw new IllegalArgumentException("Le fichier CSV est vide.");
            }
            String[] headers = headerLine.split(";");
            Map<String, Integer> nomColonnesEtIndex = mapHeaderIndices(headers);

            // Charger les données dans csvData
            String line;
            while ((line = reader.readLine()) != null) {
                String[] colonnes = line.split(";");
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
    }

    @Override
    public List<String> recupererContacts(String finess, List<String> autoriteCompetente) {
        String codeDepartement = extraireCodeDepartement(finess);

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

    private String extraireCodeDepartement(String finess) {
        String codeDepartement = finess.substring(0, 2);

        if (codeDepartement.equals("97")) {
            codeDepartement = getCodeOutreMer(finess.charAt(3));
        }
        else if (codeDepartement.equals("98")) {
            codeDepartement = "976";
        }
        else if (codeDepartement.startsWith("2")) {
            codeDepartement = getCodeCorse(finess.charAt(1));
        }

        return codeDepartement;
    }

    private String getCodeOutreMer(char caractereQuatriemePosition) {
        switch (caractereQuatriemePosition) {
            case '1': return "971";
            case '2': return "972";
            case '3': return "973";
            case '4': return "974";
            case '5': return "975";
            default: throw new IllegalArgumentException("Département d'outremer non reconnu.");
        }
    }

    private String getCodeCorse(char deuxiemeCaractere) {
        if (deuxiemeCaractere == 'A') {
            return "2A";
        } else if (deuxiemeCaractere == 'B') {
            return "2B";
        } else {
            throw new IllegalArgumentException("Code FINESS invalide pour la Corse. Le deuxième caractère doit être 'A' ou 'B'.");
        }
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



