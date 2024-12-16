package fr.gouv.social.sireclamations.hexagone.domain.ports;

import fr.gouv.social.sireclamations.hexagone.domain.AutoriteCompetente;

import java.util.List;
import java.util.Set;

public interface ReferentielDesContacts {
    List<String> recupererContacts(Integer codePostal, Set<AutoriteCompetente> ars);
}


