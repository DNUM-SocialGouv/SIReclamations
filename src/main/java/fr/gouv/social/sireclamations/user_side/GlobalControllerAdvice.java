package fr.gouv.social.sireclamations.user_side;

import fr.gouv.social.sireclamations.hexagone.DeposerReclamation;
import fr.gouv.social.sireclamations.hexagone.domain.exceptions.DematSocialException;
import fr.gouv.social.sireclamations.server_side.exceptions.AutoriteCompetenteNotFoundException;
import fr.gouv.social.sireclamations.server_side.exceptions.ContactNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ControllerAdvice
public class GlobalControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(GlobalControllerAdvice.class);
    @ExceptionHandler(AutoriteCompetenteNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map<String, String> handleAutoriteCompetenteNotFoundException(AutoriteCompetenteNotFoundException ex) {
        logger.error(ex.getMessage());
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(ContactNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map<String, String> handleContactNotFoundException(ContactNotFoundException ex) {
        logger.error(ex.getMessage());
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(DematSocialException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map<String, String> handleDematSocialException(DematSocialException ex) {
        logger.error(ex.getMessage());
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Map<String, String> handleGenericException(Exception ex) {
        logger.error(ex.getMessage());
        return Map.of("error", "Une erreur inattendue s'est produite." + ex.getMessage());
    }
}

