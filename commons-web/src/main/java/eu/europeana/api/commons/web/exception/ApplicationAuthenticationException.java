package eu.europeana.api.commons.web.exception;


import eu.europeana.api.commons.oauth2.model.KeyValidationResult;
import org.springframework.http.HttpStatus;

public class ApplicationAuthenticationException extends HttpException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8994054535719881829L;

	KeyValidationResult result;
	public KeyValidationResult getResult() {
		return result;
	}
	
	public ApplicationAuthenticationException(String message, String i18nKey){
		super(message, i18nKey, null, HttpStatus.UNAUTHORIZED);
	}

	public ApplicationAuthenticationException(String message, String i18nKey, String[] i18nParams, HttpStatus status, Throwable th){
		super(message, i18nKey, i18nParams, status, th);
		
	}
	
	public ApplicationAuthenticationException(String message, String i18nKey, String[] i18nParams) {
		super(message, i18nKey, i18nParams, HttpStatus.UNAUTHORIZED);
	}

	public ApplicationAuthenticationException(String message, String i18nKey, String[] i18nParams, HttpStatus status) {
		super(message, i18nKey, i18nParams, status);
	}

	public ApplicationAuthenticationException(String message, String i18nKey,
			String[] i18nParams,HttpStatus status, Throwable th, KeyValidationResult result) {
		super(message, i18nKey, i18nParams, status, th);
		this.result =result;
	}
}