package fr.gouv.social.sireclamations.hexagone;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import fr.gouv.social.sireclamations.hexagone.domain.*;
import fr.gouv.social.sireclamations.hexagone.domain.ports.*;
import fr.gouv.social.sireclamations.hexagone.exceptions.AutoriteCompetenteNotFoundException;
import fr.gouv.social.sireclamations.hexagone.exceptions.DematSocialException;
import java.io.IOException;
import java.util.Collections;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeposerReclamationTest {

  @InjectMocks DeposerReclamation deposerReclamation;
  @Mock DematSocial dematSocial;

  @Mock
  private ReferentielDesAutoritesCompetentesParCategoriesDEtablissements
      referentielDesAutoritesCompetentesParCategoriesDEtablissements;

  @Mock private ReferentielDesTypeDeMisEnCause referentielDesTypeDeMisEnCause;

  @Mock
  private ReferentielDesAutoritesCompetentesParTypeDeMisEnCause
      referentielDesAutoritesCompetentesParTypeDeMisEnCause;

  @Nested
  class ExceptionDeposerReclamation {
    @Test
    void
        lorsqueLonDeposeUneReclamationConcernantUneCategorieEtablissementInconnu_alorsAutoriteCompetenteNotFoundException()
            throws IOException {
      // Given
      var numeroDossier = 12345;
      var codeSousCategorieEtablissementIntrouvable = 1234567910;
      var codePostal = 94300;
      var finess = "940003858";
      var nom = "EHPAD LE VERGER DE VINCENNES";
      String libelleDuMisEnCauseProvenantDuFormulaire =
          "Un professionnel de santé (médecin, infirmier, aide-soignant, kiné, ostéopathe...)";
      var etablissement =
          new Etablissement(finess, codeSousCategorieEtablissementIntrouvable, codePostal, nom);
      var dossierReclamation =
          new DossierDeReclamation(
              numeroDossier, etablissement, libelleDuMisEnCauseProvenantDuFormulaire);
      when(dematSocial.recupererDossier(numeroDossier)).thenReturn(dossierReclamation);
      when(referentielDesAutoritesCompetentesParCategoriesDEtablissements
              .recupererAutoritesCompetentesParCodeSousCategorieEtablissement(
                  codeSousCategorieEtablissementIntrouvable))
          .thenReturn(Collections.emptyList());
      when(referentielDesTypeDeMisEnCause.recupererTypeDuMisEnCause(
              libelleDuMisEnCauseProvenantDuFormulaire))
          .thenReturn(CodeTypeDuMisEnCause.PS);
      when(referentielDesAutoritesCompetentesParTypeDeMisEnCause
              .recupererAutoriteCompetentePourUnTypeDeMisEnCause(CodeTypeDuMisEnCause.PS))
          .thenReturn(null);
      // When Then
      assertThatThrownBy(() -> deposerReclamation.executer(numeroDossier))
          .isInstanceOf(AutoriteCompetenteNotFoundException.class)
          .hasMessage("Aucune autorité compétente n'a été trouvée pour le dossier : 12345");
    }

    @Test
    void
        lorsqueLonSouhaiteDeposerUneReclamationConcernantUnDossierNexistantPasChezDematSocial_alorsRetourneDematSocialException()
            throws IOException {
      // Given
      var numeroDossier = 12345;
      when(dematSocial.recupererDossier(numeroDossier))
          .thenThrow(new IOException("erreur sur le dossier numero :" + numeroDossier));
      // When Then
      assertThatThrownBy(() -> deposerReclamation.executer(numeroDossier))
          .isInstanceOf(DematSocialException.class)
          .hasMessage("erreur sur le dossier numero :12345");
    }
  }
}
