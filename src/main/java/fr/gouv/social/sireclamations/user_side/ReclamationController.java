package fr.gouv.social.sireclamations.user_side;

import fr.gouv.social.sireclamations.hexagone.DeposerReclamation;
import fr.gouv.social.sireclamations.hexagone.domain.Reclamation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/reclamations")
@Tag(name = "Réclamations", description = "Endpoints pour gérer les réclamations")
public class ReclamationController {
  private static final Logger logger = LoggerFactory.getLogger(ReclamationController.class);
  private static final String NUMERO_DEMARCHE_KEY = "procedure_id";
  private static final String NUMERO_DOSSIER_KEY = "dossier_id";
  private static final String ETAT_KEY = "state";
  private static final String DATE_DEPOT_KEY = "updated_at";
  private static final String DATETIME_PATTERN_KEY = "yyyy-MM-dd HH:mm:ss Z";

  private final DeposerReclamation deposerReclamation;

  public ReclamationController(DeposerReclamation deposerReclamation) {
    this.deposerReclamation = deposerReclamation;
  }

  // hack: une nouvelle version du webhook doit prochainement être intégrée. Cette version enverra
  // une payload de type application/json. Cette méthode est créée dans l'attente.
  @PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  @Operation(
      summary = "Récupère une réclamation issue de demat.social",
      description =
          "Permet de récupérer une réclamation issue de demat.social grâce un numéro de dossier transmis. Cette API est un webhook destiné à être configuré dans demat.social",
      responses = {
        @ApiResponse(responseCode = "200", description = "Réclamation affectée avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides dans la requête"),
        @ApiResponse(responseCode = "404", description = "Ressources nécessaires introuvables"),
        @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
      })
  public ResponseEntity<Map<String, String>> handleNonBrowserSubmissions(
      @RequestParam Map<String, String> dsWebhookMap) {

    logger.info("Appel Webhook reçu avec les paramètres : {}", dsWebhookMap);

    String message = "";

    if (isValidPayload(dsWebhookMap)) {
      final DeposerReclamationRequest.Etat etat =
          DeposerReclamationRequest.Etat.valueOf(dsWebhookMap.get(ETAT_KEY).toUpperCase());
      final int numeroProcedure = Integer.parseInt(dsWebhookMap.get(NUMERO_DEMARCHE_KEY));
      final int numeroDossier = Integer.parseInt(dsWebhookMap.get(NUMERO_DOSSIER_KEY));
      final LocalDateTime dateDepot =
          LocalDateTime.parse(
              dsWebhookMap.get(DATE_DEPOT_KEY), DateTimeFormatter.ofPattern(DATETIME_PATTERN_KEY));
      final DeposerReclamationRequest deposerReclamationRequest =
          new DeposerReclamationRequest(numeroProcedure, numeroDossier, etat, dateDepot);

      if (deposerReclamationRequest.getEtat()
          == DeposerReclamationRequest.Etat.EN_CONSTRUCTION) { // todo: devrait être en_instruction
        final Reclamation reclamation =
            deposerReclamation.executer(deposerReclamationRequest.getNumeroDossier());
        message = ReclamationApiMapper.toReclamationApiResponse(reclamation).toString();
        logger.info("La réclamation : {} à été affectée.", message);
        return GlobalControllerAdvice.getGlobalControllerAdviceResponse(HttpStatus.OK, message);
      } else {
        message =
            String.format(
                "Le Dossier %s présenté n'est pas à l'état de Construction.",
                deposerReclamationRequest.getNumeroDossier());
        logger.error(message);
        return GlobalControllerAdvice.getGlobalControllerAdviceResponse(
            HttpStatus.BAD_REQUEST, message);
      }
    } else {
      message = "Paramètres envoyés invalides.";
      logger.error(message);
      return GlobalControllerAdvice.getGlobalControllerAdviceResponse(
          HttpStatus.BAD_REQUEST, message);
    }
  }

  private static boolean validateInteger(Map<String, String> params, String key) {
    try {
      Integer.parseInt(params.get(key));
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  private static boolean validateState(Map<String, String> params, String key) {
    return DeposerReclamationRequest.Etat.isValid(params.get(key));
  }

  private static boolean validateDate(Map<String, String> params, String key) {
    try {
      LocalDateTime.parse(params.get(key), DateTimeFormatter.ofPattern(DATETIME_PATTERN_KEY));
      return true;
    } catch (DateTimeParseException e) {
      return false;
    }
  }

  private static boolean isValidPayload(Map<String, String> params) {
    return params.size() == 4
        && validateInteger(params, NUMERO_DEMARCHE_KEY)
        && validateInteger(params, NUMERO_DOSSIER_KEY)
        && validateState(params, ETAT_KEY)
        && validateDate(params, DATE_DEPOT_KEY);
  }
}
