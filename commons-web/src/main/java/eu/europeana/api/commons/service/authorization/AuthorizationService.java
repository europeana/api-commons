package eu.europeana.api.commons.service.authorization;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

import eu.europeana.api.commons.web.exception.ApplicationAuthenticationException;
import eu.europeana.api.commons.web.exception.HttpException;

public interface AuthorizationService {

    /**
     * Processes the HTTP request and validates the provided APIKey (see also Europeana APIKEY service) 
     * @param request the full HTTP request
     * @throws ApplicationAuthenticationException if the APIKey was not submitted with the request or the APIKey could not be validated 
     */
    public void authorizeReadAccess(HttpServletRequest request)
		throws ApplicationAuthenticationException;

    /**
     * This method adopts KeyCloack token from HTTP request and 
     * verifies write access rights for particular api and operation
     * @param request The full HTTP request
     * @param operation The name of operation invoked through the http request
     * @return authentication object containing user token
     * @throws ApplicationAuthenticationException if the access to the requested operation is not authorized
     */
    public Authentication authorizeWriteAccess(HttpServletRequest request, String operation) 
	    throws ApplicationAuthenticationException;
    
    /**
     * This method compares If-Match header with the current etag value.
     * 
     * @param etag    The current etag value
     * @param request The request containing If-Match header
     * @throws HttpException
     */
    public void checkIfMatchHeader(String etag, HttpServletRequest request) throws HttpException;
     
    /**
     * This method generates etag for response header.
     * 
     * @param timestamp The date of the last modification
     * @param format       The MIME format
     * @param version      The API version
     * @return etag value
     */
    public String generateETag(Date timestamp, String format, String version);
        
}
