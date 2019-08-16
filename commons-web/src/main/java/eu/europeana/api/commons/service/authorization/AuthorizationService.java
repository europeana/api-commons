package eu.europeana.api.commons.service.authorization;

import javax.servlet.http.HttpServletRequest;

import eu.europeana.api.commons.definitions.vocabulary.Roles;
import eu.europeana.api.commons.exception.ApiKeyExtractionException;
import eu.europeana.api.commons.exception.AuthorizationExtractionException;
import eu.europeana.api.commons.web.exception.ApplicationAuthenticationException;

public interface AuthorizationService {

    /**
     * Processes the HTTP request and validates the provided APIKey (see also Europeana APIKEY service) 
     * @param request api request
     * @throws ApplicationAuthenticationException if the APIKey was not submitted with the request or the APIKey could not be validated 
     */
    public void authorizeReadAccess(HttpServletRequest request)
		throws ApplicationAuthenticationException;

    /**
     * This method adopts KeyCloack token from HTTP request and 
     * verifies write access rights for particular api and operation
     * @param request The HTTP request
     * @param operation The name of current operation
     * @param userRoles
     * @return true if authenticated, false otherwise
     * @throws ApplicationAuthenticationException
     */
    public void authorizeWriteAccess(HttpServletRequest request, String operation, Roles[] userRoles) 
	    throws ApplicationAuthenticationException, ApiKeyExtractionException, AuthorizationExtractionException;
}
