package eu.europeana.api.commons.service.authorization;

import javax.servlet.http.HttpServletRequest;

import eu.europeana.api.commons.web.exception.ApplicationAuthenticationException;

public interface AuthorizationService {

    /**
     * Processes the HTTP request and validates the provided APIKey (see also Europeana APIKEY service) 
     * @param request api request
     * @throws ApplicationAuthenticationException if the APIKey was not submitted with the request or the APIKey could not be validated 
     */
    public void authorizeReadAccess(HttpServletRequest request)
		throws ApplicationAuthenticationException;
}
