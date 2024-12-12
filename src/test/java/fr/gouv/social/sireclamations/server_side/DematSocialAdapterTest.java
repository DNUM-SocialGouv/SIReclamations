package fr.gouv.social.sireclamations.server_side;

import fr.gouv.social.sireclamations.hexagone.Domicile;
import fr.gouv.social.sireclamations.hexagone.domain.CodeTypeDeLieu;
import fr.gouv.social.sireclamations.hexagone.domain.Etablissement;
import fr.gouv.social.sireclamations.hexagone.exceptions.CodePostalAbsentException;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DematSocialAdapterTest {

    @Mock
    private DematSocialApi dematSocialApi;

    @Mock
    private OpenDataSoftApi openDataSoftApi;

    private DematSocialAdapter dematSocialAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Retrofit retrofit = mock(Retrofit.class);
        when(retrofit.create(DematSocialApi.class)).thenReturn(dematSocialApi);
        when(retrofit.create(OpenDataSoftApi.class)).thenReturn(openDataSoftApi);
        dematSocialAdapter = new DematSocialAdapter(retrofit, openDataSoftApi);
    }

    @Test
    void lorsquunDossierExisteEtConcerneUnEtablissement_alorsRetourneLeDossierEtLesInformationsDeLEtablissement() throws IOException {
        // Given
        mockAppelDematSocialApi("""
                {
                    "data": {
                        "dossier": {
                            "number": 178291,
                            "champs": [
                                {
                                    "id": "Q2hhbXAtMTk1MDU=",
                                    "__typename": "TextChamp",
                                    "label": "Où a eu lieu le problème ?",
                                    "stringValue": "Dans un établissement de santé (hôpital, clinique, pharmacie, ...)"
                                },
                                {
                                    "id": "Q2hhbXAtMTk1MDg=",
                                    "stringValue": "PHARMACIE DE L'ABBAYE, ST CYR L ECOLE 78210 (780012951 - 500)"
                                }
                            ]
                        }
                    }
                }
                """);

        // When
        var dossier = dematSocialAdapter.recupererDossier(178291);

        // Then
        var lieuDeSurvenuAttendu = new Etablissement("780012951", 500, 78210, "PHARMACIE DE L'ABBAYE");
        assertNotNull(dossier);
        assertEquals(178291, dossier.getNumeroDossier());
        assertEquals(78210, dossier.getCodePostal());
        assertThat(dossier.getLieuDeSurvenu()).usingRecursiveComparison().isEqualTo(lieuDeSurvenuAttendu);

    }

    @Test
    void lorsquunDossierExisteEtConcerneUnEtablissementQuiNeContientPasDeCodeCategorieEtablissementDansSonDossierDematSocial_alorsRetourneLeDossierEtLesInformationsDeLEtablissement() throws IOException {
        // Given
        mockAppelDematSocialApi("""
                {
                    "data": {
                        "dossier": {
                            "number": 178291,
                            "champs": [
                                {
                                    "id": "Q2hhbXAtMTk1MDU=",
                                    "__typename": "TextChamp",
                                    "label": "Où a eu lieu le problème ?",
                                    "stringValue": "Dans un établissement de santé (hôpital, clinique, pharmacie, ...)"
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

        // JSON simulé pour OpenDataSoft
        mockAppelOpenDataSoftApi("""
                {
                   "total_count": 1,
                   "results": [
                      {
                         "categ_code": "500"
                      }
                   ]
                }
                """);

        // When
        var dossier = dematSocialAdapter.recupererDossier(178291);

        // Then
        var lieuDeSurvenuAttendu = new Etablissement("780012951", 500, 78210, "PHARMACIE DE L'ABBAYE");

        assertNotNull(dossier);
        assertEquals(178291, dossier.getNumeroDossier());
        assertEquals(78210, dossier.getCodePostal());
        assertThat(dossier.getLieuDeSurvenu()).usingRecursiveComparison().isEqualTo(lieuDeSurvenuAttendu);

    }

    //TODO test domicile
    @Test
    void lorsquunDossierExisteEtConcerneUnDomicileDontLadresseEstComplètementRenseignée_alorsRetourneLeDossierEtLesInformationsDuDomicile() throws IOException {
        // Given
        mockAppelDematSocialApi("""
                {
                    "data": {
                        "dossier": {
                            "number": 178291,
                            "champs": [
                                {
                                     "id": "Q2hhbXAtMTk1MDU=",
                                     "__typename": "TextChamp",
                                     "label": "Où a eu lieu le problème ?",
                                     "stringValue": "Au domicile (domicile de la victime, domicile d'un membre de la famille, domicile d'un aidant)"
                                 },
                                 {
                                     "id": "Q2hhbXAtMTk1MDY=",
                                     "__typename": "AddressChamp",
                                     "label": "Renseignez l'adresse où a eu lieu le problème :",
                                     "stringValue": "81 Avenue Pierre Curie 78210 Saint-Cyr-l'École",
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
                                     }
                                 }
                            ]
                        }
                    }
                }
                """);
        // When
        var dossier = dematSocialAdapter.recupererDossier(178291);
        // Then
        var lieuDeSurvenuAttendu = new Domicile(78210, "81 Avenue Pierre Curie");
        assertNotNull(dossier);
        assertEquals(178291, dossier.getNumeroDossier());
        assertEquals(78210, dossier.getCodePostal());
        assertThat(dossier.getLieuDeSurvenu()).usingRecursiveComparison().isEqualTo(lieuDeSurvenuAttendu);
    }

    @Test
    void lorsquunDossierExisteEtConcerneUnDomicileDontLadresseEstIncompleteMaisContientLeCodePostal_alorsRetourneLeDossierEtLesInformationsDuDomicile() throws IOException {
        // Given
        mockAppelDematSocialApi("""
                {
                    "data": {
                        "dossier": {
                            "number": 178291,
                            "champs": [
                                {
                                     "id": "Q2hhbXAtMTk1MDU=",
                                     "__typename": "TextChamp",
                                     "label": "Où a eu lieu le problème ?",
                                     "stringValue": "Au domicile (domicile de la victime, domicile d'un membre de la famille, domicile d'un aidant)"
                                 },
                                 {
                                     "id": "Q2hhbXAtMTk1MDY=",
                                     "__typename": "AddressChamp",
                                     "label": "Renseignez l'adresse où a eu lieu le problème :",
                                     "stringValue": "81 Avenue Pierre Curie 78210"
                                 }
                            ]
                        }
                    }
                }
                """);
        // When
        var dossier = dematSocialAdapter.recupererDossier(178291);
        // Then
        var lieuDeSurvenuAttendu = new Domicile(78210, "81 Avenue Pierre Curie 78210");
        assertNotNull(dossier);
        assertEquals(178291, dossier.getNumeroDossier());
        assertEquals(78210, dossier.getCodePostal());
        assertThat(dossier.getLieuDeSurvenu()).usingRecursiveComparison().isEqualTo(lieuDeSurvenuAttendu);
    }

    @Test
    void lorsquunDossierExisteEtConcerneUnDomicileDontLadresseNeContientPasDeCodePostal_alorsThrowCodePostalAbsentException() throws IOException {
        // Given
        mockAppelDematSocialApi("""
                {
                    "data": {
                        "dossier": {
                            "number": 178291,
                            "champs": [
                                {
                                     "id": "Q2hhbXAtMTk1MDU=",
                                     "__typename": "TextChamp",
                                     "label": "Où a eu lieu le problème ?",
                                     "stringValue": "Au domicile (domicile de la victime, domicile d'un membre de la famille, domicile d'un aidant)"
                                 },
                                 {
                                     "id": "Q2hhbXAtMTk1MDY=",
                                     "__typename": "AddressChamp",
                                     "label": "Renseignez l'adresse où a eu lieu le problème :",
                                     "stringValue": "81 Avenue Pierre Curie"
                                 }
                            ]
                        }
                    }
                }
                """);
        // When Then
        assertThrows(CodePostalAbsentException.class, () -> {
            dematSocialAdapter.recupererDossier(178291);
        });
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
        assertThrows(IOException.class, () -> {
            dematSocialAdapter.recupererDossier(178291);
        });
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
        IOException exception = assertThrows(IOException.class, () -> {
            dematSocialAdapter.recupererDossier(178291);
        });

        // Vérifier le message de l'exception
        assertTrue(exception.getMessage().contains("La réponse de l'API n'est pas un JSON valide"));
        assertTrue(exception.getMessage().contains(invalidJsonResponse));
    }

    @Test
    void quandApiOpenDataSoftRenvoiUneReponseNonJson_alorsThrowIOException() throws IOException {
        // Given
        mockAppelDematSocialApi("""
                {
                    "data": {
                        "dossier": {
                            "number": 178291,
                            "champs": [
                                {
                                    "id": "Q2hhbXAtMTk1MDU=",
                                    "__typename": "TextChamp",
                                    "label": "Où a eu lieu le problème ?",
                                    "stringValue": "Dans un établissement de santé (hôpital, clinique, pharmacie, ...)"
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

        String invalidJsonResponse = "Ceci n'est pas un JSON valide";
        ResponseBody responseBody = ResponseBody.create(invalidJsonResponse, null); // Aucune spécification de type MIME
        Response<ResponseBody> response = Response.success(responseBody); // Réponse réussie avec le contenu non-JSON

        Call<ResponseBody> call = mock(Call.class); // Mock du call
        when(call.execute()).thenReturn(response); // Retour de la réponse simulée
        when(openDataSoftApi.fetchCodeSousCategorie(anyString(), anyString(), anyInt())).thenReturn(call); // Mock du service API

        // When Then
        IOException exception = assertThrows(IOException.class, () -> {
            dematSocialAdapter.recupererDossier(178291);
        });

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
        ResponseBody responseOpenDataSoftBody = ResponseBody.create(jsonResponse, MediaType.get("application/json"));
        Response<ResponseBody> responseOpenDataSoft = Response.success(responseOpenDataSoftBody);
        Call<ResponseBody> callOpenDataSoft = mock(Call.class);
        when(callOpenDataSoft.execute()).thenReturn(responseOpenDataSoft);
        when(openDataSoftApi.fetchCodeSousCategorie("categ_code", "et_finess:\"780012951\"", 2)).thenReturn(callOpenDataSoft);

    }


}