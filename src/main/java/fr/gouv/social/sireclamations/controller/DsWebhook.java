package fr.gouv.social.sireclamations.controller;

import fr.gouv.social.sireclamations.GraphQLClient;
import fr.gouv.social.sireclamations.GraphQLResponseMapper;
import fr.gouv.social.sireclamations.dto.DsWebhookPayload;
import fr.gouv.social.sireclamations.generated.types.Dossier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/webhook")
public class DsWebhook {

    private static final Logger logger = LoggerFactory.getLogger(DsWebhook.class);
    private final String dsGraphQlAccessToken = "TODO";
    @Value("${demat.social.graphql-endpoint}")
    private String dsGraphQlEndpoint;
    @Autowired
    private Environment environment;

    @PostMapping("/ds")
    public ResponseEntity<String> receiveWebhook(@RequestBody DsWebhookPayload dsWebhookPayload) {
        logger.info("Webhook reçu avec numéro de dossier : {}", dsWebhookPayload.getDossierId());

        final GraphQLClient graphQLClient = new GraphQLClient(dsGraphQlEndpoint, dsGraphQlAccessToken);
        final String response = graphQLClient.fetchDossier(dsWebhookPayload.getDossierId());
        final GraphQLResponseMapper graphQLResponseMapper = new GraphQLResponseMapper();

        try {
            final Dossier dossier = graphQLResponseMapper.parseDossierResponse(response);
            logger.info(dossier.getChamps().get(1).getStringValue());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return ResponseEntity.status(HttpStatus.OK).body("Webhook reçu avec succès !");
    }
}