package fr.gouv.social.sireclamations.hexagone.domain.port;

import java.util.List;

public interface ReferentielDesContacts {
    List<String> recupererContacts(String finess, List<String> autoriteCompetente);
}


