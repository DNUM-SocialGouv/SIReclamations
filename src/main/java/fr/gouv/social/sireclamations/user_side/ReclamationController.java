package fr.gouv.social.sireclamations.user_side;

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
  private static final Logger logger = LoggerFactory.getLogger(ReclamationController.class);

  @PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  public ResponseEntity<String> handleNonBrowserSubmissions(
          @RequestParam MultiValueMap<String, String> paramMap) {

    logger.info("MultiValueMap : {}", paramMap.toString());

    return new ResponseEntity<String>(paramMap.toString(), HttpStatus.OK);
  }
}
