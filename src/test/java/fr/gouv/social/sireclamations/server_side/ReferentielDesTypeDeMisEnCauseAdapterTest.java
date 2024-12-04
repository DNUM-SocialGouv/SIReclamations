package fr.gouv.social.sireclamations.server_side;

import fr.gouv.social.sireclamations.hexagone.domain.CodeTypeDuMisEnCause;
import fr.gouv.social.sireclamations.hexagone.domain.port.ReferentielDesTypeDeMisEnCause;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import static org.junit.jupiter.api.Assertions.*;

class ReferentielDesTypeDeMisEnCauseAdapterTest {
    private ReferentielDesTypeDeMisEnCause referentielDesTypeDeMisEnCause;

    @BeforeEach
    void setup() throws Exception {
        Resource csvResource = new ClassPathResource("data/mappingFormulaireV2-typeMEC-test.csv");
        referentielDesTypeDeMisEnCause = new ReferentielDesTypeDeMisEnCauseAdapter(csvResource);
    }

    @Test
    void lorsqueLonVeutRecupererLeTypeDuMiseEnCausePourUnProfessionnelDeSante_alorsRenvoiPS(){
        // Given
        String libelleDuMisEnCause = "Un professionnel de santé (médecin, infirmier, aide-soignant, kiné, ostéopathe...)";
        // When
        var codeTypeDuMisEnCause = referentielDesTypeDeMisEnCause.recupererTypeDuMisEnCause(libelleDuMisEnCause);
        // Then
        assertEquals(CodeTypeDuMisEnCause.PS, codeTypeDuMisEnCause);
    }
    @Test
    void lorsqueLonVeutRecupererLeTypeDuMiseEnCausePourUnProfessionnelDuSoin_alorsRenvoiAP(){
        // Given
        String libelleDuMisEnCause = "Un professionnel du soin (coiffeur, esthéticienne, naturopathe, ...)";
        // When
        var codeTypeDuMisEnCause = referentielDesTypeDeMisEnCause.recupererTypeDuMisEnCause(libelleDuMisEnCause);
        // Then
        assertEquals(CodeTypeDuMisEnCause.AP, codeTypeDuMisEnCause);
    }
    @Test
    void lorsqueLonVeutRecupererLeTypeDuMiseEnCausePourUnAutreProfessionnelDeLEtablissement_alorsRenvoiAP(){
        // Given
        String libelleDuMisEnCause = "Un autre professionnel de l'établissement (directeur, animateur, agent d'entretien, ambulancier...)";
        // When
        var codeTypeDuMisEnCause = referentielDesTypeDeMisEnCause.recupererTypeDuMisEnCause(libelleDuMisEnCause);
        // Then
        assertEquals(CodeTypeDuMisEnCause.AP, codeTypeDuMisEnCause);
    }
    @Test
    void lorsqueLonVeutRecupererLeTypeDuMiseEnCausePourUnUnAutreResidentOuUnAutrePatient_alorsRenvoiP(){
        // Given
        String libelleDuMisEnCause = "Un autre résident ou un autre patient";
        // When
        var codeTypeDuMisEnCause = referentielDesTypeDeMisEnCause.recupererTypeDuMisEnCause(libelleDuMisEnCause);
        // Then
        assertEquals(CodeTypeDuMisEnCause.P, codeTypeDuMisEnCause);
    }


}