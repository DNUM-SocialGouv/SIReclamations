package fr.gouv.social.sireclamations;

import fr.gouv.social.sireclamations.hexagone.port.DematSocialPort;
import fr.gouv.social.sireclamations.hexagone.domain.DossierReclamation;
import fr.gouv.social.sireclamations.hexagone.domain.Etablissement;
import fr.gouv.social.sireclamations.hexagone.port.ReclamationPort;
import fr.gouv.social.sireclamations.hexagone.useCase.DeposerReclamation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeposerReclamationTest {

    @InjectMocks
    DeposerReclamation deposerReclamation;

    @Mock
    DematSocialPort dematSocialPort;
    @Mock
    ReclamationPort reclamationPort;

    @Test
    void lorsquUnDossierExiste_alorsOnDeposeUneReclamationAssocieeAceDossier(){
        //Given
        var numeroDossier = "12345";
        var codeSousCategorieEtablissement = "500";
        var codePostal = "94300";
        String finess = "940003858";
        String nom = "EHPAD LE VERGER DE VINCENNES";
        var etablissement = new Etablissement(finess, codeSousCategorieEtablissement, codePostal, nom);
        var dossierReclamation = new DossierReclamation(numeroDossier, codeSousCategorieEtablissement, codePostal, etablissement);

        when(dematSocialPort.recupererDossier(numeroDossier)).thenReturn(dossierReclamation);
        //When
        deposerReclamation.executer(numeroDossier);
        //Then
        ArgumentCaptor<DossierReclamation> captor = ArgumentCaptor.forClass(DossierReclamation.class);
        verify(reclamationPort, times(1)).deposerReclamation(captor.capture());
        assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(dossierReclamation);
    }
}