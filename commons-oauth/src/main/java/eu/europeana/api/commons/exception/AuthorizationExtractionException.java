package eu.europeana.api.commons.exception;

public class AuthorizationExtractionException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 3390262539460423550L;

    public AuthorizationExtractionException(String message) {
	super(message);
    }

    public AuthorizationExtractionException(String message, Throwable th) {
	super(message, th);
    }

}
