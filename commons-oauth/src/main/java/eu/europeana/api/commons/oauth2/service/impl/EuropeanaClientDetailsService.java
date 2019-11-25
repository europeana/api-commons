package eu.europeana.api.commons.oauth2.service.impl;
import org.apache.http.HttpStatus;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import eu.europeana.api.commons.oauth2.model.impl.ClientDetailsAdapter;
import eu.europeana.apikey.client.ApiKeyValidationResult;
import eu.europeana.apikey.client.Client;
import eu.europeana.apikey.client.exception.ApiKeyValidationException;

/**
 * The entry point into the database of clients.
 */
@Service("commons_oauth2_europeanaClientDetailsService")
public class EuropeanaClientDetailsService implements ClientDetailsService {

    private String apiKeyServiceUrl;
    private Client apiKeyClient;

    public Client getApiKeyClient() {
        if(apiKeyClient == null) {
            apiKeyClient = new Client(getApiKeyServiceUrl());
        }
	return apiKeyClient;
    }

    

    @Override
    /**
     * Loads ClientDetails object belongs to an apiKey
     */
    public ClientDetails loadClientByClientId(String key)
            throws OAuth2Exception, ClientRegistrationException {
        
	ApiKeyValidationResult validation;
	try {
            validation = getApiKeyClient().validateApiKey(key);
        } catch (ApiKeyValidationException | RuntimeException e) {
            //service not accessible (e.g. IO Exception wrapped by the client, or other runtime exception )
      	  throw new OAuth2Exception("Invocation of api key service failed. Cannot validate ApiKey : " + key, e);
        }

	//validation is never null after the invokation of validateApiKyy
	if (validation != null && validation.isValidApiKey()) {
	    // valid api key
	    return new ClientDetailsAdapter(key);
	} 
	
	if (validation == null || HttpStatus.SC_INTERNAL_SERVER_ERROR == validation.getHttpStatus()) {
	    // apikey service is faulty
	    String message = (validation == null) ? "unknown" : validation.getErrorMessage(); 
	    throw new OAuth2Exception("Invocation of api key service failed. Cannot validate ApiKey : " + key
		    + ". Reason:" + message);
	} else {
	    // invalid apikey
	    throw new ClientRegistrationException("Invalid API key: " + key + " Reason: " + validation.getHttpStatus()
		    + " : " + validation.getErrorMessage());
	}        	
    }
    
    public String getApiKeyServiceUrl() {
        return apiKeyServiceUrl;
    }

    public void setApiKeyServiceUrl(String apiKeyServiceUrl) {
        this.apiKeyServiceUrl = apiKeyServiceUrl;
    }
}
