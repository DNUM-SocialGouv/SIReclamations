package fr.gouv.social.sireclamations.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategorieEtablissementRepositoryImplTest {
    private CategorieEtablissementRepository categorieEtablissementRepository;
    @BeforeEach
    void setUp() throws Exception {
        // Utilisation d'un fichier CSV en mémoire ou d'un chemin fictif
        Resource csvResource = new ClassPathResource("data/sousCatFINESS-ac-test.csv");
        categorieEtablissementRepository = new CategorieEtablissementRepositoryImpl(csvResource);
    }
    @Test
    void lorsqueLonVeutRecupererAutoriteCompetentePourUnEhpad_alorsRenvoiARSetCD(){
        //Given
        String codeSousCategorie = "500";
        //When
        var autoritesCompetentes = categorieEtablissementRepository.recupererAutoriteCompetenteParCodeSousCategorieEtablissement(codeSousCategorie);
        //Then
        assertEquals(List.of("ARS", "CD"), autoritesCompetentes);
    }

    @Test
    void lorsqueLonVeutRecupererAutoriteCompetentePourUneCategorieEtablissementInexistante_alorsRenvoiNull(){
        //Given
        String codeSousCategorieInexistant = "12345678910";
        //When
        var autoritesCompetentes = categorieEtablissementRepository.recupererAutoriteCompetenteParCodeSousCategorieEtablissement(codeSousCategorieInexistant);
        //Then
        assertTrue(autoritesCompetentes.isEmpty());
    }
}