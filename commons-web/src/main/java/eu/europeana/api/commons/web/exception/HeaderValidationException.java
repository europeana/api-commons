package eu.europeana.api.commons.web.exception;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import eu.europeana.api.commons.definitions.config.i18n.I18nConstants;


public class HeaderValidationException extends HttpException{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6638981164823301905L;

	public HeaderValidationException(String parameterName, String parameterValue){
		this(I18nConstants.INVALID_PARAM_VALUE, parameterName, parameterValue, null);
	}
	
	public HeaderValidationException(String i18nKey, String parameterName, String parameterValue){
		this(i18nKey, parameterName, parameterValue, null);
	}
	public HeaderValidationException(String i18nKey, String parameterName, String parameterValue, Throwable th){
		this(i18nKey, parameterName, parameterValue, HttpStatus.PRECONDITION_FAILED, th);
	}
	
	public HeaderValidationException(String i18nKey, String parameterName, String parameterValue, HttpStatus status, Throwable th){
		this(i18nKey, i18nKey, new String[]{parameterName, parameterValue}, status, th);
	}
	
	public HeaderValidationException(String message, String i18nKey, String[] i18nParams, HttpStatus status, Throwable th){
		super(message + " " + StringUtils.join(i18nParams, ':'), i18nKey, i18nParams, status, th);
	}
}
