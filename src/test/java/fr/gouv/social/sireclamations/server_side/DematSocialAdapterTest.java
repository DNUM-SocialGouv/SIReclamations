package fr.gouv.social.sireclamations.server_side;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import fr.gouv.social.sireclamations.hexagone.domain.ChampsArbreDeDecision;
import fr.gouv.social.sireclamations.hexagone.domain.CodeTypeDeLieu;
import fr.gouv.social.sireclamations.hexagone.domain.Domicile;
import fr.gouv.social.sireclamations.hexagone.domain.DossierDeReclamation;
import fr.gouv.social.sireclamations.hexagone.domain.Etablissement;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

class DematSocialAdapterTest {

  @Mock private DematSocialApi dematSocialApi;

  @Mock private OpenDataSoftApi openDataSoftApi;

  @Mock private ReferentielDuTypeDeLieux referentielDuTypeDeLieux;

  @Mock private ReferentielDesChampsDuFormulaire referentielDesChampsDuFormulaire;

  private DematSocialAdapter dematSocialAdapter;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    Retrofit retrofit = mock(Retrofit.class);
    when(retrofit.create(DematSocialApi.class)).thenReturn(dematSocialApi);
    when(retrofit.create(OpenDataSoftApi.class)).thenReturn(openDataSoftApi);
    dematSocialAdapter =
        new DematSocialAdapter(
            retrofit, openDataSoftApi, referentielDuTypeDeLieux, referentielDesChampsDuFormulaire);
    Map<ChampsArbreDeDecision, String> champsArbreDeDecision =
        Map.ofEntries(
            Map.entry(ChampsArbreDeDecision.TYPE_DE_LIEU, "Q2hhbXAtMTk1MDU="),
            Map.entry(ChampsArbreDeDecision.LIEU_ETAB, "Q2hhbXAtMTk1MDg="),
            Map.entry(ChampsArbreDeDecision.LIEU_DOM, "Q2hhbXAtMjcxNjE="),
            Map.entry(ChampsArbreDeDecision.TYPE_DE_MEC_ETAB, "Q2hhbXAtMTk1MTU="),
            Map.entry(ChampsArbreDeDecision.MALTRAITANCE, "Q2hhbXAtMjcxNTU="),
            Map.entry(ChampsArbreDeDecision.MOTIF, "Q2hhbXAtMTk1MjY="),
            Map.entry(ChampsArbreDeDecision.SERVICE, "Q2hhbXAtMjcxNjg="),
            Map.entry(ChampsArbreDeDecision.PERS_RESP_ETAB, "Q2hhbXAtMjgzNjg="),
            Map.entry(ChampsArbreDeDecision.PERS_RESP_DOM, "Q2hhbXAtMjg3ODE="),
            Map.entry(ChampsArbreDeDecision.CODE_POSTAL, "Q2hhbXAtMjgzNjc="));
    when(referentielDesChampsDuFormulaire.getChampsPourArbreDeDecision())
        .thenReturn(champsArbreDeDecision);
  }

  @Test
  void
      lorsquunDossierExisteEtConcerneUneReclamationADomicileDontLadresseEstSaisieEtDontLeMisEnCauseEstUnServiceADomicile_alorsRecupereTousLesChampsNecessaireALaffectation()
          throws IOException {
    // Given
    mockAppelDematSocialApi(
        """
            {
                "data": {
                    "dossier": {
                        "number": 178291,
                        "champs": [
                            {
                                "id": "Q2hhbXAtMjcxNTU=",
                                "__typename": "TextChamp",
                                "label": "Des actes de maltraitance ont-ils eu lieu ?",
                                "stringValue": "Oui"
                            },
                            {
                                "id": "Q2hhbXAtMTk1MjY=",
                                "__typename": "MultipleDropDownListChamp",
                                "label": "Le ou les types de fait(s)",
                                "stringValue": "Problème comportemental, relationnel ou de communication avec une personne",
                                "updatedAt": "2025-03-06T10:25:25+01:00",
                                "values": [
                                    "Problème comportemental, relationnel ou de communication avec une personne"
                                ]
                            },
                            {
                                "id": "Q2hhbXAtMTk1MDU=",
                                "__typename": "TextChamp",
                                "label": "Lieu principal de survenue",
                                "stringValue": "Au domicile (domicile de la victime, domicile d'un membre de la famille, domicile d'un aidant...)",
                                "updatedAt": "2025-03-06T10:25:37+01:00"
                            },
                            {
                                "id": "Q2hhbXAtMjcxNjE=",
                                "__typename": "AddressChamp",
                                "label": "Adresse concernée",
                                "stringValue": "81 Avenue Pierre Curie 78210 Saint-Cyr-l'École",
                                "updatedAt": "2025-03-06T10:25:57+01:00",
                                "address": {
                                    "label": "81 Avenue Pierre Curie 78210 Saint-Cyr-l'École",
                                    "type": "housenumber",
                                    "streetAddress": "81 Avenue Pierre Curie",
                                    "streetNumber": "81",
                                    "streetName": "Avenue Pierre Curie",
                                    "postalCode": "78210",
                                    "cityName": "Saint-Cyr-l'École",
                                    "cityCode": "78545",
                                    "departmentName": "Yvelines",
                                    "departmentCode": "78",
                                    "regionName": "Île-de-France",
                                    "regionCode": "11"
                                },
                                "commune": {
                                    "name": "Saint-Cyr-l’École",
                                    "code": "78545",
                                    "postalCode": "78210"
                                },
                                "departement": {
                                    "name": "Yvelines",
                                    "code": "78"
                                }
                            },
                            {
                                "id": "Q2hhbXAtMjgzNjc=",
                                "__typename": "IntegerNumberChamp",
                                "label": "Code postal",
                                "stringValue": "78210",
                                "updatedAt": "2025-03-06T10:25:55+01:00",
                                "integerNumber": "78210"
                            },
                            {
                                "id": "Q2hhbXAtMjg3ODE=",
                                "__typename": "TextChamp",
                                "label": "Personne responsable des faits",
                                "stringValue": "Professionnel dans le cadre d'un service ou d'une intervention à domicile",
                                "updatedAt": "2025-03-10T17:16:50+01:00"
                            },
                            {
                                "id": "Q2hhbXAtMjcxNjg=",
                                "__typename": "TextChamp",
                                "label": "Professionnel dans le cadre d'un service ou d'une intervention à domicile",
                                "stringValue": "Service de Soins Infirmier à Domicile (SSIAD)",
                                "updatedAt": "2025-03-10T17:16:57+01:00"
                            }
                        ]
                    }
                }
            }
            """);

    var libelleTypeLieu =
        "Au domicile (domicile de la victime, domicile d'un membre de la famille, domicile d'un aidant...)";
    when(referentielDuTypeDeLieux.recupererCodeTypeDeLieuxAPartirDuLibelle(libelleTypeLieu))
        .thenReturn(CodeTypeDeLieu.DOM);
    // When
    var dossierObtenu = dematSocialAdapter.recupererDossier(178291);

    // Then
    var domicile =
        new Domicile(
            78210,
            "81 Avenue Pierre Curie",
            libelleTypeLieu,
            "Service de Soins Infirmier à Domicile (SSIAD)");
    var dossierAttendu =
        new DossierDeReclamation(
            178291,
            domicile,
            "Service de Soins Infirmier à Domicile (SSIAD)",
            true,
            List.of("Problème comportemental, relationnel ou de communication avec une personne"));
    assertNotNull(dossierObtenu);
    assertThat(dossierObtenu).usingRecursiveComparison().isEqualTo(dossierAttendu);
  }

  @Test
  void
      lorsquunDossierExisteEtConcerneUneReclamationADomicileDontLadresseNEstPasSaisieEtDontLeMisEnCauseEstUnServiceADomicile_alorsRecupereTousLesChampsNecessaireALaffectation()
          throws IOException {
    // Given
    mockAppelDematSocialApi(
        """
            {
                "data": {
                    "dossier": {
                        "number": 178291,
                        "champs": [
                            {
                                "id": "Q2hhbXAtMjcxNTU=",
                                "__typename": "TextChamp",
                                "label": "Des actes de maltraitance ont-ils eu lieu ?",
                                "stringValue": "Oui"
                            },
                            {
                                "id": "Q2hhbXAtMTk1MjY=",
                                "__typename": "MultipleDropDownListChamp",
                                "label": "Le ou les types de fait(s)",
                                "stringValue": "Problème comportemental, relationnel ou de communication avec une personne",
                                "updatedAt": "2025-03-06T10:25:25+01:00",
                                "values": [
                                    "Problème comportemental, relationnel ou de communication avec une personne"
                                ]
                            },
                            {
                                "id": "Q2hhbXAtMTk1MDU=",
                                "__typename": "TextChamp",
                                "label": "Lieu principal de survenue",
                                "stringValue": "Au domicile (domicile de la victime, domicile d'un membre de la famille, domicile d'un aidant...)",
                                "updatedAt": "2025-03-06T10:25:37+01:00"
                            },
                            {
                                "id": "Q2hhbXAtMjcxNjE=",
                                "__typename": "AddressChamp",
                                "label": "Adresse concernée",
                                "stringValue": "",
                                "updatedAt": "2025-03-10T17:42:59+01:00",
                                "address": null,
                                "commune": null,
                                "departement": null
                            },
                            {
                                "id": "Q2hhbXAtMjgzNjc=",
                                "__typename": "CommuneChamp",
                                "label": "Code postal",
                                "stringValue": "Saint-Cyr-l’École (78210)",
                                "updatedAt": "2025-03-10T17:42:49+01:00",
                                "commune": {
                                    "name": "Saint-Cyr-l’École",
                                    "code": "78545",
                                    "postalCode": "78210"
                                },
                                "departement": {
                                    "name": "Yvelines",
                                    "code": "78"
                                }
                            },
                            {
                                "id": "Q2hhbXAtMjg3ODE=",
                                "__typename": "TextChamp",
                                "label": "Personne responsable des faits",
                                "stringValue": "Professionnel dans le cadre d'un service ou d'une intervention à domicile",
                                "updatedAt": "2025-03-10T17:43:11+01:00"
                            },
                            {
                                "id": "Q2hhbXAtMjcxNjg=",
                                "__typename": "TextChamp",
                                "label": "Professionnel dans le cadre d'un service ou d'une intervention à domicile",
                                "stringValue": "Service de Soins Infirmier à Domicile (SSIAD)",
                                "updatedAt": "2025-03-10T17:43:16+01:00"
                            }
                        ]
                    }
                }
            }
            """);

    var libelleTypeLieu =
        "Au domicile (domicile de la victime, domicile d'un membre de la famille, domicile d'un aidant...)";
    when(referentielDuTypeDeLieux.recupererCodeTypeDeLieuxAPartirDuLibelle(libelleTypeLieu))
        .thenReturn(CodeTypeDeLieu.DOM);
    // When
    var dossierObtenu = dematSocialAdapter.recupererDossier(178291);

    // Then
    var domicile =
        new Domicile(78210, null, libelleTypeLieu, "Service de Soins Infirmier à Domicile (SSIAD)");
    var dossierAttendu =
        new DossierDeReclamation(
            178291,
            domicile,
            "Service de Soins Infirmier à Domicile (SSIAD)",
            true,
            List.of("Problème comportemental, relationnel ou de communication avec une personne"));
    assertNotNull(dossierObtenu);
    assertThat(dossierObtenu).usingRecursiveComparison().isEqualTo(dossierAttendu);
  }

  @Test
  void
      lorsquunDossierExisteEtConcerneUneReclamationEnEtablissementSanteContreUnProfessionnelDeSante_alorsRecupereTousLesChampsNecessaireALaffectation()
          throws IOException {
    // Given
    mockAppelDematSocialApi(
        """
            {
                "data": {
                    "dossier": {
                        "number": 178291,
                        "champs": [
                            {
                                "id": "Q2hhbXAtMjcxNTU=",
                                "__typename": "TextChamp",
                                "label": "Des actes de maltraitance ont-ils eu lieu ?",
                                "stringValue": "Oui"
                            },
                            {
                                "id": "Q2hhbXAtMTk1MjY=",
                                "__typename": "MultipleDropDownListChamp",
                                "label": "Le ou les types de fait(s)",
                                "stringValue": "Problème comportemental, relationnel ou de communication avec une personne",
                                "updatedAt": "2025-03-06T10:25:25+01:00",
                                "values": [
                                    "Problème comportemental, relationnel ou de communication avec une personne"
                                ]
                            },
                            {
                                "id": "Q2hhbXAtMTk1MDU=",
                                "__typename": "TextChamp",
                                "label": "Lieu principal de survenue",
                                "stringValue": "Dans un établissement de santé (hôpital, clinique, laboratoire, pharmacie ...)",
                                "updatedAt": "2025-03-06T10:25:37+01:00"
                            },
                            {
                                "id": "Q2hhbXAtMTk1MDg=",
                                "__typename": "TextChamp",
                                "label": "Sélectionnez l'établissement concerné",
                                "stringValue": "PHARMACIE HAMEAU, ST CYR L ECOLE 78210 (780012993 - 620)",
                                "updatedAt": "2025-03-06T14:29:41+01:00"
                            },
                            {
                                "id": "Q2hhbXAtMjgzNjc=",
                                "__typename": "IntegerNumberChamp",
                                "label": "Code postal",
                                "stringValue": "78210",
                                "updatedAt": "2025-03-06T10:25:55+01:00",
                                "integerNumber": "78210"
                            },
                            {
                                "id": "Q2hhbXAtMjgzNjg=",
                                "__typename": "TextChamp",
                                "label": "Personne responsable des faits",
                                "stringValue": "Professionnel",
                                "updatedAt": "2025-03-06T10:25:59+01:00"
                            },
                            {
                                "id": "Q2hhbXAtMTk1MTU=",
                                "__typename": "TextChamp",
                                "label": "Avec qui les faits ont-ils eu lieu ?",
                                "stringValue": "Un professionnel de santé (médecin, infirmier, aide-soignant, kiné, ostéopathe...)",
                                "updatedAt": "2025-03-06T14:29:52+01:00"
                            }
                        ]
                    }
                }
            }
            """);

    var libelleTypeLieu =
        "Dans un établissement de santé (hôpital, clinique, laboratoire, pharmacie ...)";
    when(referentielDuTypeDeLieux.recupererCodeTypeDeLieuxAPartirDuLibelle(libelleTypeLieu))
        .thenReturn(CodeTypeDeLieu.ETAB);
    // When
    var dossierObtenu = dematSocialAdapter.recupererDossier(178291);

    // Then
    var etablissement =
        new Etablissement(
            "780012993",
            620,
            78210,
            "PHARMACIE HAMEAU",
            "Dans un établissement de santé (hôpital, clinique, laboratoire, pharmacie ...)");
    var dossierAttendu =
        new DossierDeReclamation(
            178291,
            etablissement,
            "Un professionnel de santé (médecin, infirmier, aide-soignant, kiné, ostéopathe...)",
            true,
            List.of("Problème comportemental, relationnel ou de communication avec une personne"));
    assertNotNull(dossierObtenu);
    assertThat(dossierObtenu).usingRecursiveComparison().isEqualTo(dossierAttendu);
  }

  @Test
  void quandApiDematSocialNeRenvoiRien_alorsThrowDematSocialException() throws IOException {
    // Given
    ResponseBody responseBody = ResponseBody.create("Erreur", null);
    Response<ResponseBody> response = Response.error(400, responseBody);
    Call<ResponseBody> call = mock(Call.class);
    when(call.execute()).thenReturn(response);
    when(dematSocialApi.executeGraphQLQueryRaw(any())).thenReturn(call);

    // When Then
    assertThrows(IOException.class, () -> dematSocialAdapter.recupererDossier(178291));
  }

  @Test
  void quandApiDematSocialRenvoiUneReponseNonJson_alorsThrowIOException() throws IOException {
    // Given
    String invalidJsonResponse = "Ceci n'est pas un JSON valide";
    ResponseBody responseBody = ResponseBody.create(invalidJsonResponse, null);
    Response<ResponseBody> response = Response.success(responseBody);

    Call<ResponseBody> call = mock(Call.class);
    when(call.execute()).thenReturn(response);
    when(dematSocialApi.executeGraphQLQueryRaw(any())).thenReturn(call);

    // When Then
    IOException exception =
        assertThrows(IOException.class, () -> dematSocialAdapter.recupererDossier(178291));

    // Vérifier le message de l'exception
    assertTrue(exception.getMessage().contains("La réponse de l'API n'est pas un JSON valide"));
    assertTrue(exception.getMessage().contains(invalidJsonResponse));
  }

  @Test
  void quandApiOpenDataSoftRenvoiUneReponseNonJson_alorsThrowIOException() throws IOException {
    // Given
    mockAppelDematSocialApi(
        """
            {
                "data": {
                    "dossier": {
                        "number": 178291,
                        "champs": [
                            {
                                "id": "Q2hhbXAtMTk1MDU=",
                                "__typename": "TextChamp",
                                "label": "Où a eu lieu le problème ?",
                                "stringValue": "Dans un établissement de santé (hôpital, clinique, laboratoire, pharmacie ...)"
                            },
                            {
                                "id": "Q2hhbXAtMjgzNjc=",
                                "__typename": "IntegerNumberChamp",
                                "label": "Code postal",
                                "stringValue": "78210",
                                "updatedAt": "2025-03-06T10:25:55+01:00",
                                "integerNumber": "78210"
                            },
                            {
                                "id": "Q2hhbXAtMTk1MDg=",
                                "stringValue": "PHARMACIE DE L'ABBAYE, ST CYR L ECOLE 78210 (780012951)"
                            }
                        ]
                    }
                }
            }
            """);
    var libelleTypeLieu =
        "Dans un établissement de santé (hôpital, clinique, laboratoire, pharmacie ...)";
    when(referentielDuTypeDeLieux.recupererCodeTypeDeLieuxAPartirDuLibelle(libelleTypeLieu))
        .thenReturn(CodeTypeDeLieu.ETAB);

    String invalidJsonResponse = "Ceci n'est pas un JSON valide";
    ResponseBody responseBody =
        ResponseBody.create(invalidJsonResponse, null); // Aucune spécification de type MIME
    Response<ResponseBody> response =
        Response.success(responseBody); // Réponse réussie avec le contenu non-JSON

    Call<ResponseBody> call = mock(Call.class); // Mock du call
    when(call.execute()).thenReturn(response); // Retour de la réponse simulée
    when(openDataSoftApi.fetchCodeSousCategorie(anyString(), anyString(), anyInt()))
        .thenReturn(call); // Mock du service API

    // When Then
    IOException exception =
        assertThrows(IOException.class, () -> dematSocialAdapter.recupererDossier(178291));

    // Vérification du message d'erreur
    assertTrue(exception.getMessage().contains("La réponse de l'API n'est pas un JSON valide"));
    assertTrue(exception.getMessage().contains(invalidJsonResponse));
  }

  private void mockAppelDematSocialApi(String jsonResponse) throws IOException {
    ResponseBody responseDematSocialBody = ResponseBody.create(jsonResponse, null);
    Response<ResponseBody> responseDematSocial = Response.success(responseDematSocialBody);

    var callDematSocial = mock(Call.class);
    when(callDematSocial.execute()).thenReturn(responseDematSocial);
    when(dematSocialApi.executeGraphQLQueryRaw(any())).thenReturn(callDematSocial);
  }

  private void mockAppelOpenDataSoftApi(String jsonResponse) throws IOException {
    ResponseBody responseOpenDataSoftBody =
        ResponseBody.create(jsonResponse, MediaType.get("application/json"));
    Response<ResponseBody> responseOpenDataSoft = Response.success(responseOpenDataSoftBody);
    Call<ResponseBody> callOpenDataSoft = mock(Call.class);
    when(callOpenDataSoft.execute()).thenReturn(responseOpenDataSoft);
    when(openDataSoftApi.fetchCodeSousCategorie("categ_code", "et_finess:\"780012951\"", 2))
        .thenReturn(callOpenDataSoft);
  }
}
