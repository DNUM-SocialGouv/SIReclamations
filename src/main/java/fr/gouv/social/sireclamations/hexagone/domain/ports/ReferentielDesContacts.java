package fr.gouv.social.sireclamations.hexagone.domain.ports;

import java.util.List;
import java.util.Set;

public interface ReferentielDesContacts {
    List<String> recupererContacts(Integer codePostal, Set<String> ars);
}


