package eu.europeana.api.commons.web.exception;

import org.springframework.http.HttpStatus;

import eu.europeana.api.commons.definitions.config.i18n.I18nConstants;


public class InternalServerException extends HttpException{

	public static final String MESSAGE_UNEXPECTED_EXCEPTION = "An unexpected server exception occured!";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InternalServerException(String message, Throwable th){
		super(message, null, null, HttpStatus.INTERNAL_SERVER_ERROR, th);
	}

	public InternalServerException(Throwable th){
		super(th.getMessage(), I18nConstants.SERVER_ERROR_UNEXPECTED, null, HttpStatus.INTERNAL_SERVER_ERROR, th);
	}

}
