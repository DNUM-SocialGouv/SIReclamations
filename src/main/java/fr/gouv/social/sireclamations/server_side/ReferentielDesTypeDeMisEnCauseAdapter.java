package fr.gouv.social.sireclamations.server_side;

import fr.gouv.social.sireclamations.hexagone.domain.CodeTypeDuMisEnCause;
import fr.gouv.social.sireclamations.hexagone.domain.port.ReferentielDesTypeDeMisEnCause;
import org.springframework.stereotype.Repository;

@Repository
public class ReferentielDesTypeDeMisEnCauseAdapter implements ReferentielDesTypeDeMisEnCause {
    @Override
    public CodeTypeDuMisEnCause recupererTypeDuMisEnCause(String libelleDuMisEnCause) {
        return null;
    }
}
