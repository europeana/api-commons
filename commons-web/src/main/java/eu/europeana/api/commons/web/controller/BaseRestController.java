package eu.europeana.api.commons.web.controller;

import eu.europeana.api.commons.web.service.AbstractRequestPathMethodService;
import java.util.Date;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

import eu.europeana.api.commons.definitions.config.i18n.I18nConstants;
import eu.europeana.api.commons.exception.ApiKeyExtractionException;
import eu.europeana.api.commons.exception.AuthorizationExtractionException;
import eu.europeana.api.commons.service.authorization.AuthorizationService;
import eu.europeana.api.commons.web.exception.ApplicationAuthenticationException;
import eu.europeana.api.commons.web.exception.HeaderValidationException;
import eu.europeana.api.commons.web.exception.HttpException;
import eu.europeana.api.commons.web.http.HttpHeaders;

public abstract class BaseRestController {

    protected abstract AuthorizationService getAuthorizationService();

    /**
     * This method adopts KeyCloack token from HTTP request and verifies write
     * access rights for particular api and operation
     * 
     * @param request   The HTTP request
     * @param operation The name of current operation
     * @return authentication object containing user token
     * @throws ApplicationAuthenticationException
     * @throws AuthorizationExtractionException
     * @throws ApiKeyExtractionException
     */
    public Authentication verifyWriteAccess(String operation, HttpServletRequest request)
	    throws ApplicationAuthenticationException {
	return getAuthorizationService().authorizeWriteAccess(request, operation);
    }

    /**
     * Processes the HTTP request and validates the provided APIKey (see also
     * Europeana APIKEY service)
     * 
     * @param request the full HTTP request
     * @throws ApplicationAuthenticationException if the APIKey was not submitted
     *                                            with the request or the APIKey
     *                                            could not be validated
     */
    public Authentication verifyReadAccess(HttpServletRequest request) throws ApplicationAuthenticationException {
	return getAuthorizationService().authorizeReadAccess(request);
    }

    /**
     * This method compares If-Match header with the current etag value.
     * 
     * @param etag    The current etag value
     * @param request The request containing If-Match header
     * @throws HttpException
     */
    public void checkIfMatchHeader(String etag, HttpServletRequest request) throws HttpException {
	String ifMatchHeader = request.getHeader(HttpHeaders.IF_MATCH);
	if (ifMatchHeader != null && !ifMatchHeader.equals(etag)) {
	    //if the tags doesn't match throw exception
	    throw new HeaderValidationException(I18nConstants.INVALID_PARAM_VALUE, HttpHeaders.IF_MATCH, ifMatchHeader);
	}
    }

    /**
     * This method generates etag for response header.
     * 
     * @param timestamp The date of the last modification
     * @param format    The MIME format
     * @param version   The API version
     * @return etag value
     */
    public String generateETag(Date timestamp, String format, String version) {
	// add timestamp, format and version to an etag
	Integer hashCode = (timestamp + format + version).hashCode();
	return hashCode.toString();
    }

  /**
   * Gets all HTTP methods implemented across the application, that match the url pattern for the
   * current request. This is useful in populating the HTTP Allow header when generating API
   * responses
   */
  protected String getMethodsForRequestPattern(HttpServletRequest request,
      AbstractRequestPathMethodService requestMethodService) {
    Optional<String> methodsForRequestPattern =
        requestMethodService.getMethodsForRequestPattern(request);

    return methodsForRequestPattern.orElse(request.getMethod());
  }

}
