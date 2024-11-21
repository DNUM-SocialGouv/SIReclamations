package fr.gouv.social.sireclamations.hexagone.port;

import java.util.List;

public interface ContactsPort {
    List<String> recupererContacts(String finess, List<String> autoriteCompetente);
}


