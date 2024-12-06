package fr.gouv.social.sireclamations.config;

import fr.gouv.social.sireclamations.server_side.DematSocialApi;
import fr.gouv.social.sireclamations.server_side.OpenDataSoftApi;
import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.Objects;

@Configuration
public class RetrofitConfiguration {

    private final Dotenv dotenv;

    public RetrofitConfiguration(Dotenv dotenv) {
        this.dotenv = dotenv;
    }
    private OkHttpClient createClient(String token) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Interceptor authInterceptor = chain -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Request request = chain.request().newBuilder()
                    .addHeader("Authorization", Objects.requireNonNull(headers.getFirst(HttpHeaders.AUTHORIZATION)))
                    .addHeader("Content-Type", Objects.requireNonNull(headers.getFirst(HttpHeaders.CONTENT_TYPE)))
                    .build();
            return chain.proceed(request);
        };

        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .build();
    }

    // Bean Retrofit pour l'API DematSocial
    @Bean
    public Retrofit dematSocialRetrofit(@Value("${demat.social.graphql-endpoint}") String baseUrl) {
        // Vérifie d'abord si la variable d'environnement existe (en CI)
        String token = System.getenv("DEMAT_SOCIAL_GRAPHQL_TOKEN");
        // Si la variable d'environnement n'est pas définie, utilise dotenv (en local)
        if (token == null || token.isEmpty()) {
            token = dotenv.get("DEMAT_SOCIAL_GRAPHQL_TOKEN");
        }
        OkHttpClient client = createClient(token);
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client)
                .build();
    }

    @Bean
    public Retrofit openDataSoftRetrofit(@Value("${opendatasoft.base-url}") String baseUrl) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client)
                .build();
    }

    @Bean
    public OpenDataSoftApi openDataSoftApi(Retrofit openDataSoftRetrofit) {
        return openDataSoftRetrofit.create(OpenDataSoftApi.class);
    }

    @Bean
    public DematSocialApi dematSocialApi(Retrofit dematSocialRetrofit) {
        return dematSocialRetrofit.create(DematSocialApi.class);
    }

}

