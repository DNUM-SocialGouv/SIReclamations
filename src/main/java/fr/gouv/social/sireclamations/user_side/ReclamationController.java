package fr.gouv.social.sireclamations.user_side;

import fr.gouv.social.sireclamations.hexagone.DeposerReclamation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/reclamations")
@Tag(name = "Réclamations", description = "Endpoints pour gérer les réclamations")
public class ReclamationController {
  private final DeposerReclamation deposerReclamation;
  private static final Logger logger = LoggerFactory.getLogger(ReclamationController.class);

  public ReclamationController(DeposerReclamation deposerReclamation) {
    this.deposerReclamation = deposerReclamation;
  }

  /* @PostMapping
  @Operation(
      summary = "Déposer une réclamation",
      description = "Permet de déposer une réclamation en envoyant un numéro de dossier",
      responses = {
        @ApiResponse(responseCode = "200", description = "Réclamation déposée avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides dans la requête"),
        @ApiResponse(responseCode = "404", description = "Ressources nécessaires introuvables"),
        @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
      })
  public ReclamationApiResponse deposerReclamation(
      @RequestBody DeposerReclamationRequest deposerReclamationRequest) {
    logger.info(
        "Webhook reçu avec numéro de dossier : {}", deposerReclamationRequest.getNumeroDossier());
    var reclamation = deposerReclamation.executer(deposerReclamationRequest.getNumeroDossier());
    logger.info(
        "La réclamation pour le dossier numéro de dossier : {}",
        deposerReclamationRequest.getNumeroDossier() + "à été créée.");
    return ReclamationApiMapper.toReclamationApiResponse(reclamation);
  }

  */

  @PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  public ResponseEntity<String> handleNonBrowserSubmissions(
          @RequestParam MultiValueMap<String, String> paramMap) {

    logger.info("MultiValueMap : {}", paramMap.toString());

    return new ResponseEntity<String>(paramMap.toString(), HttpStatus.OK);
  }
}
