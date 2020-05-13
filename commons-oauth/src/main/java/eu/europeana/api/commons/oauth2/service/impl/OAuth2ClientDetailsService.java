package eu.europeana.api.commons.oauth2.service.impl;

import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;

import eu.europeana.api.commons.oauth2.model.ApiKey;
import eu.europeana.api.commons.oauth2.model.impl.ClientDetailsAdapter;
import eu.europeana.api.commons.oauth2.service.ApiKeyService;

/**
 * The entry point into the database of clients.
 * @deprecated Use EuropeanaClientDetailsService instead
 */
@Service("commons_oauth2_clientDetailsService")
public class OAuth2ClientDetailsService implements ClientDetailsService {

    @Resource
    private ApiKeyService apiKeyService;

    @Override
    /**
     * Loads ClientDetails object belongs to an apiKey
     */
    public ClientDetails loadClientByClientId(String key)
            throws OAuth2Exception {
        try {
            ApiKey apiKey = apiKeyService.findByKey(key);
            if (apiKey != null)
                return new ClientDetailsAdapter(apiKey);
            
        } catch (Throwable e) {
        	LogManager.getLogger(this.getClass()).error(e.getMessage());
            throw new OAuth2Exception("OAuth2 ClientId unknown");
        }
        throw new OAuth2Exception("OAuth2 ClientId unknown");
    }
	
}
