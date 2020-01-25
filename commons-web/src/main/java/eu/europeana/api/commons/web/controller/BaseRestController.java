package eu.europeana.api.commons.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

import eu.europeana.api.commons.exception.ApiKeyExtractionException;
import eu.europeana.api.commons.exception.AuthorizationExtractionException;
import eu.europeana.api.commons.service.authorization.AuthorizationService;
import eu.europeana.api.commons.web.exception.ApplicationAuthenticationException;

public abstract class BaseRestController {

    /**
     * This method is used for validation of the provided api key
     * 
     * This method is deprecated, to be replaced by verifyReadAccess
     * 
     * @param request web request 
     * @throws ApplicationAuthenticationException if the apikey submitted with the request cannot be validated
     * @throws AuthorizationExtractionException 
     * @throws ApiKeyExtractionException 
     */
    @Deprecated
    protected void validateApiKey(HttpServletRequest request) throws ApplicationAuthenticationException{
	verifyReadAccess(request);
    }
    
    protected abstract AuthorizationService getAuthorizationService();
 
    /**
     * This method adopts KeyCloack token from HTTP request and verifies write access rights for particular api and operation
     * @param request The HTTP request
     * @param operation The name of current operation
     * @return authentication object containing user token
     * @throws ApplicationAuthenticationException
     * @throws AuthorizationExtractionException 
     * @throws ApiKeyExtractionException 
     */
    public Authentication verifyWriteAccess(String operation, HttpServletRequest request) 
	     throws ApplicationAuthenticationException, ApiKeyExtractionException, AuthorizationExtractionException {
    	return getAuthorizationService().authorizeWriteAccess(request, operation); 	
    }

    /**
     * Processes the HTTP request and validates the provided APIKey (see also Europeana APIKEY service) 
     * @param request the full HTTP request
     * @throws ApplicationAuthenticationException if the APIKey was not submitted with the request or the APIKey could not be validated 
     */
    public void verifyReadAccess(HttpServletRequest request) 
	     throws ApplicationAuthenticationException{
	 getAuthorizationService().authorizeReadAccess(request); 	
   }
}
