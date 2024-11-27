package fr.gouv.social.sireclamations.server_side;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface DematSocialApi {

    @POST("api/v2/graphql")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> executeGraphQLQueryRaw(@Body GraphQLRequest request);
}

