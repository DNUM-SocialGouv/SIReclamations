package fr.gouv.social.sireclamations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class GraphQLClient {

    private static final Logger logger = LoggerFactory.getLogger(GraphQLClient.class);

    private final String graphQlEndpoint;
    private final String accessToken;
    private final RestTemplate restTemplate;

    public GraphQLClient(String graphQlEndpoint, String accessToken) {
        this.graphQlEndpoint = graphQlEndpoint;
        this.accessToken = accessToken;
        restTemplate = new RestTemplate();
    }

    public String fetchDossier(int dossierNumber) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String query = """
                    {
                              "queryId": "ds-query-v2",
                              "variables": {
                                "dossierNumber": %d,
                                "includeGeometry": true,
                                "includeInstructeurs": false
                              },
                              "operationName": "getDossier"
                            }
                """.formatted(dossierNumber);

        final HttpEntity<String> entity = new HttpEntity<>(query, headers);

        logger.debug("Executing query {}", query);
        final ResponseEntity<String> response = restTemplate.postForEntity(graphQlEndpoint, entity, String.class);

        return response.getBody();
    }

}