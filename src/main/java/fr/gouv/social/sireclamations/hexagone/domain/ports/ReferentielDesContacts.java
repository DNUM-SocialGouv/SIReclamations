package fr.gouv.social.sireclamations.hexagone.domain.ports;

import java.util.List;

public interface ReferentielDesContacts {
    List<String> recupererContacts(String finess, List<String> autoriteCompetente);
}


