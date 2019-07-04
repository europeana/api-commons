package eu.europeana.api.commons.exception;

public class ApiKeyExtractionException extends Exception {

	
    /**
     * 
     */
    private static final long serialVersionUID = 8496589899526050812L;

	public ApiKeyExtractionException(String message){
		super(message);
	}

	public ApiKeyExtractionException(String message, Throwable th){
		super(message, th);
	}

}
