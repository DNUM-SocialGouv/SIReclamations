package fr.gouv.social.sireclamations.hexagone.domain.port;

import fr.gouv.social.sireclamations.hexagone.domain.CodeTypeDuMisEnCause;

public interface ReferentielDesTypeDeMisEnCause {
    CodeTypeDuMisEnCause recupererTypeDuMisEnCause(String libelleDuMisEnCause);
}
