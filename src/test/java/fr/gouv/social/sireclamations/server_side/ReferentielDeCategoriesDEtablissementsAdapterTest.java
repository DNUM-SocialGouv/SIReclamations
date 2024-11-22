package fr.gouv.social.sireclamations.server_side;

import fr.gouv.social.sireclamations.hexagone.domain.port.ReferentielDeCategoriesDEtablissements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReferentielDeCategoriesDEtablissementsAdapterTest {
    private ReferentielDeCategoriesDEtablissements referentielDeCategoriesDEtablissements;
    @BeforeEach
    void setUp() throws Exception {
        Resource csvResource = new ClassPathResource("data/sousCatFINESS-ac-test.csv");
        referentielDeCategoriesDEtablissements = new ReferentielDeCategorieDEtablissementAdapter(csvResource);
    }
    @Test
    void lorsqueLonVeutRecupererAutoriteCompetentePourUnEhpad_alorsRenvoiARSetCD(){
        //Given
        String codeSousCategorie = "500";
        //When
        var autoritesCompetentes = referentielDeCategoriesDEtablissements.recupererAutoritesCompetentesParCodeSousCategorieEtablissement(codeSousCategorie);
        //Then
        assertEquals(List.of("ARS", "CD"), autoritesCompetentes);
    }

    @Test
    void lorsqueLonVeutRecupererAutoriteCompetentePourUneCategorieEtablissementInexistante_alorsRenvoiNull(){
        //Given
        String codeSousCategorieInexistant = "12345678910";
        //When
        var autoritesCompetentes = referentielDeCategoriesDEtablissements.recupererAutoritesCompetentesParCodeSousCategorieEtablissement(codeSousCategorieInexistant);
        //Then
        assertTrue(autoritesCompetentes.isEmpty());
    }
}