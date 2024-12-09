package fr.gouv.social.sireclamations.server_side;

import fr.gouv.social.sireclamations.hexagone.domain.ports.ReferentielDeCategoriesDEtablissements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReferentielDeCategoriesDEtablissementsCsvAdapterTest {
    private ReferentielDeCategoriesDEtablissements referentielDeCategoriesDEtablissements;
    @BeforeEach
    void setUp() throws Exception {
        Resource csvResource = new ClassPathResource("data/sousCatFINESS-ac-test.csv");
        referentielDeCategoriesDEtablissements = new ReferentielDeCategorieDEtablissementCsvAdapter(csvResource);
    }
    @Test
    void lorsqueLonVeutRecupererAutoriteCompetentePourUnEhpad_alorsRenvoiARSetCD(){
        //Given
        var codeSousCategorie = 500;
        //When
        var autoritesCompetentes = referentielDeCategoriesDEtablissements.recupererAutoritesCompetentesParCodeSousCategorieEtablissement(codeSousCategorie);
        //Then
        assertEquals(List.of("ARS", "CD"), autoritesCompetentes);
    }

    @Test
    void lorsqueLonVeutRecupererAutoriteCompetentePourUneCategorieEtablissementInexistante_alorsRenvoiNull(){
        //Given
        var codeSousCategorieInexistant = 1234567891;
        //When
        var autoritesCompetentes = referentielDeCategoriesDEtablissements.recupererAutoritesCompetentesParCodeSousCategorieEtablissement(codeSousCategorieInexistant);
        //Then
        assertTrue(autoritesCompetentes.isEmpty());
    }
}