package fr.gouv.social.sireclamations.hexagone;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import fr.gouv.social.sireclamations.hexagone.domain.*;
import fr.gouv.social.sireclamations.hexagone.domain.ports.*;
import fr.gouv.social.sireclamations.hexagone.exceptions.AutoriteCompetenteNotFoundException;
import fr.gouv.social.sireclamations.hexagone.exceptions.DematSocialException;
import java.io.IOException;
import java.util.List;
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

  @Mock
  private ReferentielDesAutoritesCompetentesParMisEnCauseADomicile
      referentielDesAutoritesCompetentesParMisEnCauseADomicile;

  @Mock
  private ReferentielDesAutoritesCompetentesParMisEnCauseEnEtablissement
      referentielDesAutoritesCompetentesParMisEnCauseEnEtablissement;

  @Mock
  private ReferentielDesAutoritesCompetentesParLieuDeSurvenue
      referentielDesAutoritesCompetentesParLieuDeSurvenue;

  @Mock
  private ReferentielDesAutoritesCompetentesParMotifs referentielDesAutoritesCompetentesParMotifs;

  @Nested
  class ExceptionDeposerReclamation {

    @Test
    void
        lorsqueLonSouhaiteDeposerUneReclamationMaisQuAucuneAutoriteCompetenteNestIdentifiee_alorsthrowsAutoriteCompetenteNotFoundException()
            throws IOException {
      // Given
      var numeroDossier = 12345;
      var codeSousCategorieEtablissementIntrouvable = 1234567910;
      var codePostal = 94300;
      var finess = "940003858";
      var nom = "EHPAD LE VERGER DE VINCENNES";
      String libelleDuMisEnCauseProvenantDuFormulaire =
          "Un professionnel de santé (médecin, infirmier, aide-soignant, kiné, ostéopathe...)";
      String typeDeLieu =
          "Dans un établissement de santé (hôpital, clinique, laboratoire, pharmacie ...)";
      var etablissement =
          new Etablissement(
              finess, codeSousCategorieEtablissementIntrouvable, codePostal, nom, typeDeLieu);
      var motifs =
          List.of("Problème comportemental, relationnel ou de communication avec une personne");
      var dossierReclamation =
          new DossierDeReclamation(
              numeroDossier, etablissement, libelleDuMisEnCauseProvenantDuFormulaire, true, motifs);
      when(dematSocial.recupererDossier(numeroDossier)).thenReturn(dossierReclamation);
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
