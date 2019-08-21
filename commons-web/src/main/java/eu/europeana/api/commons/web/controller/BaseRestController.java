package eu.europeana.api.commons.web.controller;

import javax.servlet.http.HttpServletRequest;

import eu.europeana.api.commons.exception.ApiKeyExtractionException;
import eu.europeana.api.commons.exception.AuthorizationExtractionException;
import eu.europeana.api.commons.service.authorization.AuthorizationService;
import eu.europeana.api.commons.web.exception.ApplicationAuthenticationException;

public abstract class BaseRestController {

    /**
     * This method is used for validation of the provided api key
     * 
     * @param request web request 
     * @throws ApplicationAuthenticationException if the apikey submitted with the request cannot be validated
     */
    protected void validateApiKey(HttpServletRequest request) throws ApplicationAuthenticationException {
	getAuthorizationService().authorizeReadAccess(request);
    }
    
    protected abstract AuthorizationService getAuthorizationService();
 
    /**
     * This method adopts KeyCloack token from HTTP request and verifies write access rights for particular api and operation
     * @param request The HTTP request
     * @param operation The name of current operation
     * @return true if authenticated, false otherwise
     * @throws ApplicationAuthenticationException
     * @throws AuthorizationExtractionException 
     * @throws ApiKeyExtractionException 
     */
    public void verifyWriteAccess(String operation, HttpServletRequest request) 
	     throws ApplicationAuthenticationException, ApiKeyExtractionException, AuthorizationExtractionException {
	 getAuthorizationService().authorizeWriteAccess(request, operation); 	
    }
}
