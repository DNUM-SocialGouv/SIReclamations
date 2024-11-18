package fr.gouv.social.sireclamations.infrastructure.exceptions;

public class AutoriteCompetenteNotFoundException extends RuntimeException {
    public AutoriteCompetenteNotFoundException(String message) {
        super(message);
    }
}
