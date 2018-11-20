package eu.europeana.api.commons.oauth2.service.impl;
/*
 * Copyright 2007-2015 The Europeana Foundation
 *
 * Licenced under the EUPL, Version 1.1 (the "Licence") and subsequent versions as approved
 * by the European Commission;
 * You may not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the Licence is distributed on an "AS IS" basis, without warranties or conditions of
 * any kind, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under
 * the Licence.
 */

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
