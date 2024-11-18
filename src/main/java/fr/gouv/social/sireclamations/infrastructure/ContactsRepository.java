package fr.gouv.social.sireclamations.infrastructure;

import java.util.List;

public interface ContactsRepository {
    List<String> recupererContactsParCodePostal(String codePostal, List<String> autoriteCompetente);
}


