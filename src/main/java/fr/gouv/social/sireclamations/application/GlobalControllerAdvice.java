package fr.gouv.social.sireclamations.application;

import fr.gouv.social.sireclamations.infrastructure.exceptions.AutoriteCompetenteNotFoundException;
import fr.gouv.social.sireclamations.infrastructure.exceptions.ContactNotFoundException;
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

    @ExceptionHandler(AutoriteCompetenteNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map<String, String> handleAutoriteCompetenteNotFoundException(AutoriteCompetenteNotFoundException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(ContactNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map<String, String> handleContactNotFoundException(ContactNotFoundException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Map<String, String> handleGenericException(Exception ex) {
        return Map.of("error", "Une erreur inattendue s'est produite.");
    }
}

