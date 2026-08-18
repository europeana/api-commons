package eu.europeana.api.commons.web.exception;

import eu.europeana.api.commons.error.EuropeanaI18nApiException;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ApplicationAuthenticationException extends EuropeanaI18nApiException {

	private static final long serialVersionUID = -8994054535719881829L;

	private  Map<String, Object> additionalInformation;

	public ApplicationAuthenticationException(String message, String i18nKey) {
		super(message, null, null, HttpStatus.UNAUTHORIZED, i18nKey, null);
	}

	public ApplicationAuthenticationException(String message, String code, String error, HttpStatus status) {
		super(message, code, error, status, null, null);
	}

	public ApplicationAuthenticationException(String message, String i18nKey,  String[] i18nParams, HttpStatus status, Throwable th) {
		super(message, null, null, status, i18nKey, i18nParams, th);
	}

	public ApplicationAuthenticationException(String message, String code, String error, HttpStatus status, Map<String, Object> additionalInformation) {
		super(message, code, error, status, null, null);
		this.additionalInformation = additionalInformation;
	}

	public ApplicationAuthenticationException(String message, String i18nKey, String[] i18nParams) {
		super(message, null, null, HttpStatus.UNAUTHORIZED, i18nKey, i18nParams);
	}

	public ApplicationAuthenticationException(String message, String i18nKey, String[] i18nParams, HttpStatus status) {
		super(message, null, null, status, i18nKey, i18nParams);
	}

	public ApplicationAuthenticationException(String message, String i18nKey, HttpStatus status) {
		super(message, null, null, status, i18nKey, null);
	}

	/**
	 * We do not want to log the exception stack trace just the error message
	 * @return
	 */
	@Override
	public boolean doLogStacktrace() {
		return false;
	}

	public Map<String, Object> getAdditionalInformation() {
		if (additionalInformation == null ) {
			this.additionalInformation = new HashMap<>();
		}
		return this.additionalInformation;
	}
}