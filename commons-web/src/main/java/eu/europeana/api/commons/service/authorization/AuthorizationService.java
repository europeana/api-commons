package eu.europeana.api.commons.service.authorization;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

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
     * This method adopts KeyCloack token from HTTP request
     * @param request The HTTP request
     * @return list of Authentication objects
     * @throws ApplicationAuthenticationException
     * @throws ApiKeyExtractionException
     */
    public List<? extends Authentication> processJwtToken(HttpServletRequest request) 
	    throws ApplicationAuthenticationException, ApiKeyExtractionException, AuthorizationExtractionException;
    
    /**
     * This method verifies write access rights for particular api and operation
     * @param authenticationList The list of authentications extracted from the JWT token
     * @param operation The name of current operation
     * @return true if authenticated, false otherwise
     * @throws ApplicationAuthenticationException
     */
    public boolean authorizeWriteAccess(List<? extends Authentication> authenticationList, String operation) throws ApplicationAuthenticationException;
    
}
