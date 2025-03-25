package fr.gouv.social.sireclamations.user_side;

import fr.gouv.social.sireclamations.hexagone.exceptions.AutoriteCompetenteNotFoundException;
import fr.gouv.social.sireclamations.hexagone.exceptions.DematSocialException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalControllerAdvice {

  private static final Logger logger = LoggerFactory.getLogger(GlobalControllerAdvice.class);
  public static final String STATUS = "status";
  public static final String MESSAGE = "message";

  @ExceptionHandler(AutoriteCompetenteNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public Map<String, Object> handleAutoriteCompetenteNotFoundException(
      AutoriteCompetenteNotFoundException ex) {
    logger.error(ex.getMessage());
    return getGlobalControllerAdviceBodyResponse(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(DematSocialException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public Map<String, Object> handleDematSocialException(DematSocialException ex) {
    logger.error(ex.getMessage());
    return getGlobalControllerAdviceBodyResponse(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public Map<String, Object> handleGenericException(Exception ex) {
    logger.error(ex.getMessage());
    return getGlobalControllerAdviceBodyResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
    List<String> errors =
        ex.getBindingResult().getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .toList();
    return new ResponseEntity<>(new ValidationErrorResponse(errors), HttpStatus.BAD_REQUEST);
  }

  public static Map<String, Object> getGlobalControllerAdviceBodyResponse(
      HttpStatus httpStatus, String message) {
    return Map.of(STATUS, String.valueOf(httpStatus.value()), MESSAGE, message);
  }

  public static ResponseEntity<Map<String, Object>> getGlobalControllerAdviceResponse(
      HttpStatus httpStatus, String message) {
    return ResponseEntity.status(httpStatus)
        .body(getGlobalControllerAdviceBodyResponse(httpStatus, message));
  }

  public class ValidationErrorResponse {
    private List<String> errors;

    public ValidationErrorResponse(List<String> errors) {
      this.errors = errors;
    }

    public List<String> getErrors() {
      return errors;
    }

    public void setErrors(List<String> errors) {
      this.errors = errors;
    }
  }
}
