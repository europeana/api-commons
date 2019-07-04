package eu.europeana.api.commons.oauth2.service.impl;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.exceptions.ClientAuthenticationException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;

import eu.europeana.api.commons.definitions.config.i18n.I18nConstants;
import eu.europeana.api.commons.oauth2.model.impl.ClientDetailsAdapter;
import eu.europeana.apikey.client.ApiKeyValidationResult;
import eu.europeana.apikey.client.Client;

/**
 * The entry point into the database of clients.
 */
@Service("commons_oauth2_europeanaClientDetailsService")
public class EuropeanaClientDetailsService implements ClientDetailsService {

    private Client apiKeyClient = new Client();

    public Client getApiKeyClient() {
        return apiKeyClient;
    }

    public void setApiKeyClient(Client apiKeyClient) {
        this.apiKeyClient = apiKeyClient;
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
}
