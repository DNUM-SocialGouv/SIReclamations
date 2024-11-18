package fr.gouv.social.sireclamations.infrastructure;

import fr.gouv.social.sireclamations.hexagone.domain.DossierReclamation;
import fr.gouv.social.sireclamations.hexagone.domain.Etablissement;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class ReclamationAdapterTest {

    @Autowired private ReclamationAdapter reclamationAdapter;
    @MockBean private CategorieEtablissementRepository categorieEtablissementRepository;
    @MockBean private ContactsRepository contactsRepository;
    @MockBean private EmailService emailService;
    @Test
    void lorsqueLonDeposeUneReclamationConcernantUneEHPADsurParis_alorsEnvoiUnMailAuContactDeLARSdeParis(){
        //Given
        var numeroDossier = "12345";
        var codeSousCategorieEtablissement = "500";
        var codePostal = "94300";
        String finess = "940003858";
        String nom = "EHPAD LE VERGER DE VINCENNES";
        var etablissement = new Etablissement(finess, codeSousCategorieEtablissement, codePostal, nom);
        var dossierReclamation = new DossierReclamation(numeroDossier, codeSousCategorieEtablissement,codePostal, etablissement);
        when(categorieEtablissementRepository.recupererAutoriteCompetenteParCodeSousCategorieEtablissement(codeSousCategorieEtablissement)).thenReturn("ARS");
        when(contactsRepository.recupererContactsParCodePostal(codePostal, "ARS")).thenReturn(List.of("idf@ars.com"));

        //When
        reclamationAdapter.deposerReclamation(dossierReclamation);
//        //Then
        ArgumentCaptor<List<String>> emailsCaptor = ArgumentCaptor.forClass(List.class);
        var destinatairesEmails = List.of("idf@ars.com");
        verify(emailService, times(1)).envoyer(emailsCaptor.capture(), any());
        assertThat(emailsCaptor.getValue()).isEqualTo(destinatairesEmails);

    }
}