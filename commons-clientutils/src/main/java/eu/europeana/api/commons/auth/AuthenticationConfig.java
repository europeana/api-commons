package eu.europeana.api.commons.auth;

import java.util.Properties;

import eu.europeana.api.commons.auth.service.AuthGrant;
import static eu.europeana.api.commons.auth.service.GrantConstants.*;

/**
 * configuration for accessing remote api
 * @author GordeaS
 */
public class AuthenticationConfig extends Properties {

    private static final long serialVersionUID = 1L;

    public static final String CONFIG_TOKEN_ENDPOINT = "token_endpoint";
    public static final String CONFIG_GRANT_PARAMS   = "grant_params";
    public static final String CONFIG_APIKEY         = "apikey";
    

    public AuthenticationConfig() {
        super();
    }

    /**
     * CConstructor to inject properties
     * @param properties
     */
    public AuthenticationConfig(Properties properties) {
        super();
        putAll(properties);
    }

    public String getAuthTokenEndpoitUri() {
        return getProperty(CONFIG_TOKEN_ENDPOINT);
    }

    public String getApiKey() {
        return containsKey(CONFIG_APIKEY) ? getProperty(CONFIG_APIKEY) 
                                          : getProperty(client_id);
    }

    public String getAccessToken() {
        return getProperty(access_token);
    }

    public AuthGrant getAuthGrant() {
        if ( containsKey(CONFIG_GRANT_PARAMS) ) {
            return new AuthGrant(getProperty(CONFIG_GRANT_PARAMS));
        }
        return new AuthGrant(this);
    }
}
