package eu.europeana.api.commons.auth;

import org.apache.commons.lang3.StringUtils;

import eu.europeana.api.commons.auth.apikey.ApikeyBasedAuthentication;
import eu.europeana.api.commons.auth.service.AuthGrant;
import eu.europeana.api.commons.auth.service.AuthenticationService;
import eu.europeana.api.commons.auth.token.LongLastingTokenAuthentication;
import eu.europeana.api.commons.auth.token.StaticTokenAuthentication;

/**
 * @author Hugo
 * @since 13 Mar 2025
 */
public class AuthenticationBuilder {

    public static AuthenticationHandler newAuthentication(AuthenticationConfig config) {
        String accessToken = config.getAccessToken();
        if ( StringUtils.isNotBlank(accessToken) ) {
            return new StaticTokenAuthentication(accessToken);
        }

        String endpointUri = config.getAuthTokenEndpoitUri();
        if ( StringUtils.isNotBlank(endpointUri) ) {
            AuthenticationService service = new AuthenticationService(endpointUri);
            return new LongLastingTokenAuthentication(service, config.getAuthGrant());
        }

        String apikey = config.getApiKey();
        if ( StringUtils.isNotBlank(apikey) ) {
            return new ApikeyBasedAuthentication(apikey);
        }

        throw new AuthenticationException(AuthenticationException.CONFIG_MISSING);
    }

}
