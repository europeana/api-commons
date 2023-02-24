package eu.europeana.api.commons.service.authorization;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

import eu.europeana.api.commons.web.exception.ApplicationAuthenticationException;

public interface AuthorizationService {

    /**
     * Processes the HTTP request and validates the provided APIKey (see also Europeana APIKEY service) 
     * @param request the full HTTP request
     * @return 
     * @throws ApplicationAuthenticationException if the APIKey was not submitted with the request or the APIKey could not be validated 
     */
    Authentication authorizeReadAccess(HttpServletRequest request)
		throws ApplicationAuthenticationException;

    /**
     * This method adopts KeyCloack token from HTTP request and 
     * verifies write access rights for particular api and operation
     * @param request The full HTTP request
     * @param operation The name of operation invoked through the http request
     * @return authentication object containing user token
     * @throws ApplicationAuthenticationException if the access to the requested operation is not authorized
     */
    Authentication authorizeWriteAccess(HttpServletRequest request, String operation) 
	    throws ApplicationAuthenticationException;

 
    /**
     * Throws an exception in the case that the application is locked for maintenance and the request received is does not belong to maintenance operations 
     * Note, this method is not intended to verify if the user is allowed to perform the given operation 
     * @param operationName
     * @throws ApplicationAuthenticationException
     */
    void checkWriteLockInEffect(String operationName) throws ApplicationAuthenticationException;
    
}
