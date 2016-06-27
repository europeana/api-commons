package eu.europeana.api.commons.oauth2.service;

import java.util.List;

import eu.europeana.api.commons.exception.CommonServiceException;
import eu.europeana.api.commons.exception.CommonServiceRuntimeException;
import eu.europeana.api.commons.oauth2.model.ApiKey;

public interface ApiKeyService {
	
	    
	    /**
	     * Find all API keys by ApiKey
	     *
	     * @param apiKey the value of the apiKey
	     * @return matching api key
	     */
	    ApiKey findByKey(String apiKey) throws CommonServiceException, CommonServiceRuntimeException;

	    /**
	     * Find all API keys by applicationName
	     *
	     * @param applicationName application name registered with the api key(s)
	     * @return all matching api keys
	     */
	    List<ApiKey> findByApplicationName(String applicationName) throws CommonServiceException, CommonServiceRuntimeException;


}
