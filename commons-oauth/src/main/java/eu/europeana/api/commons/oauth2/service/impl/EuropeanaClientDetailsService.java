package eu.europeana.api.commons.oauth2.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europeana.api.commons.auth.AuthenticationHandler;
import eu.europeana.api.commons.exception.ApiKeyValidationException;
import eu.europeana.api.commons.exception.EuropeanaClientRegistrationException;
import eu.europeana.api.commons.http.HttpConnection;
import eu.europeana.api.commons.http.HttpResponseHandler;
import eu.europeana.api.commons.oauth2.model.KeyValidationError;
import eu.europeana.api.commons.oauth2.model.KeyValidationResult;
import eu.europeana.api.commons.oauth2.model.impl.ClientDetailsAdapter;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static eu.europeana.api.commons.oauth2.utils.OAuthUtils.*;

/**
 * The entry point into the database of clients.
 */
@Service("commons_oauth2_europeanaClientDetailsService")
public class EuropeanaClientDetailsService implements ClientDetailsService {
    private String apiKeyServiceUrl;
    private AuthenticationHandler authHandler;

    public String getApiKeyServiceUrl() {
        return apiKeyServiceUrl;
    }
    public void setApiKeyServiceUrl(String apiKeyServiceUrl) {
        this.apiKeyServiceUrl = apiKeyServiceUrl;
    }
    public AuthenticationHandler getAuthHandler() {
        return authHandler;
    }
    public void setAuthHandler(AuthenticationHandler authHandler) {
        this.authHandler = authHandler;
    }
    @Override
    /* Loads ClientDetails object belongs to an apiKey
     */
    public ClientDetails loadClientByClientId(String key) throws OAuth2Exception, ClientRegistrationException {
        //allow disabling apikey validation for read access
        if (StringUtils.isBlank(getApiKeyServiceUrl())) {
            return null;
        }
        KeyValidationResult result;
        try {
            result = validateApiKeyKeycloakClient(key);
        } catch (ApiKeyValidationException | RuntimeException e) {
            //service not accessible (e.g. IO Exception wrapped by the client, or other runtime exception )
            throw new OAuth2Exception(
                    "Invocation of api key service failed. Cannot validate ApiKey : " + key, e);
        }
        //If the key validation result contains the error message return the error
        if (result != null && result.getValidationError() != null) {
            throw new EuropeanaClientRegistrationException(null, result);
        }
        // valid api key
        return new ClientDetailsAdapter(key);
    }

    /**
     * Method calls the validation endpoint of keycloak to validate the input apikey returns
     * no-content on success and  error response object upon failure.
     *
     * @return either null or result containing keycloak error messages
     */
    public KeyValidationResult validateApiKeyKeycloakClient(String apikey)
            throws ApiKeyValidationException {

        HttpConnection httpConnection = new HttpConnection();
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            if (apiKeyServiceUrl != null && !StringUtils.startsWith(apiKeyServiceUrl, HTTPS)
                    && apiKeyServiceUrl.contains(K8S_FQDN_SUFFIX)) {
                headers.put(X_FORWARDED_PROTO, HTTPS);
            }
            HttpResponseHandler response = httpConnection.post(VALIDATION_PARAMS.formatted(apiKeyServiceUrl, apikey), null,
                    headers, authHandler);
            if (response == null || response.getStatus() == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                throw new OAuth2Exception("Invocation of api key service failed. Cannot validate ApiKey : " + apikey);
                // with previous implementation it looks like validation object was always null for
                // internal server error, hence skipping the part to print the reason for now.
                //  + ". Reason:" + response.getResponse());
            }
            //In case key is invalid , return the keycloak validation error response.
            if (response.getStatus() != HttpStatus.SC_NO_CONTENT) {
                return new KeyValidationResult(response.getStatus(),
                        getKeyValidationError(response.getResponse()));
            }
        } catch (IOException e) {
            throw new ApiKeyValidationException(
                    "Unexpected exception occurred when trying to validate API KEY: " + apikey, e);
        }
        return null;
    }

    private static KeyValidationError getKeyValidationError(String response)
            throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response, KeyValidationError.class);
    }
}