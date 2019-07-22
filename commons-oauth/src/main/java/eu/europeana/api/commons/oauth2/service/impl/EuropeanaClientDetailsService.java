package eu.europeana.api.commons.oauth2.service.impl;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;

import eu.europeana.api.commons.oauth2.model.impl.ClientDetailsAdapter;
import eu.europeana.apikey.client.ApiKeyValidationResult;
import eu.europeana.apikey.client.Client;

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
            throws OAuth2Exception {
        try {
            ApiKeyValidationResult validation = getApiKeyClient().validateApiKey(key);
            if (validation.isValidApiKey())
                return new ClientDetailsAdapter(key);
            else
        	throw new OAuth2Exception("Unauthorized ApiKey : " + key + ". Reason:" + validation.getErrorMessage());
        } catch (Exception e) {
            throw new OAuth2Exception("Cannot authorize ApiKey : " + key, e);
        }
    }
    
    public String getApiKeyServiceUrl() {
        return apiKeyServiceUrl;
    }

    public void setApiKeyServiceUrl(String apiKeyServiceUrl) {
        this.apiKeyServiceUrl = apiKeyServiceUrl;
    }
}
