package eu.europeana.api.commons.web.controller;

import javax.servlet.http.HttpServletRequest;

import eu.europeana.api.commons.service.authorization.AuthorizationService;
import eu.europeana.api.commons.web.exception.ApplicationAuthenticationException;

public abstract class BaseRestController {

    /**
     * This method is used for validation of the provided api key
     * 
     * @param wsKey
     * @throws EntityAuthenticationException
     */
    protected void validateApiKey(HttpServletRequest request) throws ApplicationAuthenticationException {
	getAuthorizationService().authorizeReadAccess(request);
    }
    
    protected abstract AuthorizationService getAuthorizationService();
    
}
