package eu.europeana.api.commons.web.exception;

import org.springframework.http.HttpStatus;


public class ParamValidationException extends HttpException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3664526076494279093L;
//	public static final String MESSAGE_BLANK_PARAMETER_VALUE = "Invalid request. Parameter value must not be null or empty!";
	
	public ParamValidationException(String message, String i18nKey, String[] i18nParams) {
		this(message, i18nKey, i18nParams, HttpStatus.BAD_REQUEST, null);
	}

	public ParamValidationException(String message, String i18nKey, String[] i18nParams, HttpStatus status, Throwable th){
		super(message, i18nKey, i18nParams, status, th);		
	}

	public ParamValidationException(String message, String i18nKey, String[] i18nParams, Throwable th){
		this(message, i18nKey, i18nParams, HttpStatus.BAD_REQUEST, th);
	}
}
