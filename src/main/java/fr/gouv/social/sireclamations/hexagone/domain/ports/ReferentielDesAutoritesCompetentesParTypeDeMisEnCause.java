package fr.gouv.social.sireclamations.hexagone.domain.ports;

import fr.gouv.social.sireclamations.hexagone.domain.CodeTypeDuMisEnCause;

public interface ReferentielDesAutoritesCompetentesParTypeDeMisEnCause {
    String recupererAutoriteCompetentePourUnTypeDeMisEnCause(CodeTypeDuMisEnCause codeTypeDuMisEnCause);
}
