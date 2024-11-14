package fr.gouv.social.sireclamations.controller;

import fr.gouv.social.sireclamations.dto.DsWebhookPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DsWebhook {

    private static final Logger logger = LoggerFactory.getLogger(DsWebhook.class);

    @PostMapping("/dswebhook")
    public ResponseEntity<String> receiveWebhook(@RequestBody DsWebhookPayload dsWebhookPayload) {
        logger.info("Webhook reçu avec numéro de dossier : {}", dsWebhookPayload.getDossierId());
        return ResponseEntity.status(HttpStatus.OK).body("Webhook reçu avec succès !");
    }
}