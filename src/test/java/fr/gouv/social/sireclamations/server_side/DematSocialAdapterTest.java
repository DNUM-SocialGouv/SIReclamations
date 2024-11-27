package fr.gouv.social.sireclamations.server_side;

import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DematSocialAdapterTest {

    @Mock
    private DematSocialApi dematSocialApi;

    private DematSocialAdapter dematSocialAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Retrofit retrofit = mock(Retrofit.class);
        when(retrofit.create(DematSocialApi.class)).thenReturn(dematSocialApi);
        dematSocialAdapter = new DematSocialAdapter(retrofit);
    }

    @Test
    void lorsquunDossierExisteEtConcerneUnEtablissement_alorsRetourneLeDossierEtLesInformationsDeLEtablissement() throws IOException {
        // Given
        String jsonResponse = """
        {
            "data": {
                "dossier": {
                    "number": 178291,
                    "champs": [
                        {
                            "id": "Q2hhbXAtMTk1MDg=",
                            "stringValue": "PHARMACIE DE L'ABBAYE, ST CYR L ECOLE 78210 (780012951 - 500)"
                        }
                    ]
                }
            }
        }
        """;
        ResponseBody responseBody = ResponseBody.create(jsonResponse, null);
        Response<ResponseBody> response = Response.success(responseBody);

        var call = mock(Call.class);
        when(call.execute()).thenReturn(response);
        when(dematSocialApi.executeGraphQLQueryRaw(any())).thenReturn(call);

        // When
        var dossier = dematSocialAdapter.recupererDossier(178291);

        // Then
        assertNotNull(dossier);
        assertEquals(178291, dossier.getNumeroDossier());
        assertEquals(78210, dossier.getCodePostal());
        assertEquals("PHARMACIE DE L'ABBAYE", dossier.getEtablissement().getNom());
        assertEquals("780012951", dossier.getEtablissement().getNumeroFiness());
        assertEquals(500, dossier.getEtablissement().getCodeSousCategorie());
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
}