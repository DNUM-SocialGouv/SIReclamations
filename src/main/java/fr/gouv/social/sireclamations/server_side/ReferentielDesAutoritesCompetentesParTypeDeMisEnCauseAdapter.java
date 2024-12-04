package fr.gouv.social.sireclamations.server_side;

import fr.gouv.social.sireclamations.hexagone.domain.CodeTypeDuMisEnCause;
import fr.gouv.social.sireclamations.hexagone.domain.port.ReferentielDesAutoritesCompetentesParTypeDeMisEnCause;
import org.springframework.stereotype.Repository;

@Repository
public class ReferentielDesAutoritesCompetentesParTypeDeMisEnCauseAdapter implements ReferentielDesAutoritesCompetentesParTypeDeMisEnCause {
    @Override
    public String recupererAutoriteCompetentePourUnTypeDeMisEnCause(CodeTypeDuMisEnCause codeTypeDuMisEnCause) {
        return null;
    }
}
