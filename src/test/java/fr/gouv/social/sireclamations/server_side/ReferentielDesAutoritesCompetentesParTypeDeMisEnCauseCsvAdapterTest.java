package fr.gouv.social.sireclamations.server_side;

import fr.gouv.social.sireclamations.hexagone.domain.CodeTypeDuMisEnCause;
import fr.gouv.social.sireclamations.hexagone.domain.ports.ReferentielDesAutoritesCompetentesParTypeDeMisEnCause;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import static org.junit.jupiter.api.Assertions.*;

class ReferentielDesAutoritesCompetentesParTypeDeMisEnCauseCsvAdapterTest {

    private ReferentielDesAutoritesCompetentesParTypeDeMisEnCause referentielDesAutoritesCompetentesParTypeDeMisEnCause;
    @BeforeEach
    void setup() throws Exception {
        Resource csvResource = new ClassPathResource("data/typeMEC-ac-test.csv");
        referentielDesAutoritesCompetentesParTypeDeMisEnCause = new ReferentielDesAutoritesCompetentesParTypeDeMisEnCauseCsvAdapter(csvResource);
    }

    @Test
    void lorsqueLeMisEnCauseEstUnPersonnelDeSanté_alorsOnRetourneArsCommeAutoriteCompetente(){
        // Given
        var misEnCauseDeTypePs = CodeTypeDuMisEnCause.PS;
        // When
        var autoriteCompetenteObtenue = referentielDesAutoritesCompetentesParTypeDeMisEnCause.recupererAutoriteCompetentePourUnTypeDeMisEnCause(misEnCauseDeTypePs);
        // Then
        assertEquals("ARS", autoriteCompetenteObtenue);
    }

}