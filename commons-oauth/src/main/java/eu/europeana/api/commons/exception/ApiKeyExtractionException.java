package eu.europeana.api.commons.exception;


import org.springframework.http.HttpStatus;


public class ApiKeyExtractionException extends Exception {

	
    /**
     * 
     */
    private static final long serialVersionUID = 8496589899526050812L;

	public ApiKeyExtractionException(String message){
		super(message);
	}

//	public ApiKeyExtractionException(String message, String i18nKey){
//		super(message, i18nKey, null, HttpStatus.UNAUTHORIZED);
//	}
//
//	public ApiKeyExtractionException(String message, String i18nKey, String[] i18nParams, HttpStatus status, Throwable th){
//		super(message, i18nKey, i18nParams, status, th);
//		
//	}
//	
//	public ApiKeyExtractionException(String message, String i18nKey, String[] i18nParams) {
//		super(message, i18nKey, i18nParams, HttpStatus.UNAUTHORIZED);
//	}

}
