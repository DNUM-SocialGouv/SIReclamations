package fr.gouv.social.sireclamations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.gouv.social.sireclamations.generated.types.Dossier;

public class GraphQLResponseMapper {

    private final ObjectMapper objectMapper;

    public GraphQLResponseMapper() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    public Dossier parseDossierResponse(String jsonResponse) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        return objectMapper.readValue(jsonNode.get("data").get("dossier").toString(), Dossier.class);
    }
}