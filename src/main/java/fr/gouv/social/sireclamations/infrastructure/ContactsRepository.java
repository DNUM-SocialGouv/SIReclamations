package fr.gouv.social.sireclamations.infrastructure;

import java.util.List;

public interface ContactsRepository {
    List<String> recupererContacts(String finess, List<String> autoriteCompetente);
}


