package fr.gouv.social.sireclamations.server_side;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenDataSoftApi {

    @GET("finess-et-data-gouv/records")
    Call<ResponseBody> fetchCodeSousCategorie(
            @Query("select") String select,
            @Query("where") String where,
            @Query("limit") int limit
    );
}

