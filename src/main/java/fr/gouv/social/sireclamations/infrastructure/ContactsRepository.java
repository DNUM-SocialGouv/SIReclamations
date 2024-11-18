package fr.gouv.social.sireclamations.infrastructure;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactsRepository {
    List<String> recupererContactsParCodePostal(String codePostal, String autoriteCompetente);
}
