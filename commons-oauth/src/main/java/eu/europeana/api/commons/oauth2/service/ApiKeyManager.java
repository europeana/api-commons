package eu.europeana.api.commons.oauth2.service;

import javax.servlet.http.HttpServletRequest;

import eu.europeana.api.commons.exception.ApiKeyExtractionException;

public interface ApiKeyManager extends ApiKeyService {

	/**
	 * This method extracts apikey from a HTTP request using one of the following methods:
	 * 1. By means of a query parameter
	 * 2. By means of the "X-Api-Key" header
	 * 3. By means of a specialization of the "Authorization" header
	 * 4. By means of a JWT token
	 * if it exists.
	 * @param request that contains apikey in one of the above mentioned way
	 * @return apikey The extracted apikey
	 * @throws ApiKeyExtractionException 
	 * @throws ApplicationAuthenticationException 
	 */
	public String extractApiKey(HttpServletRequest request) throws ApiKeyExtractionException; 
	
}
