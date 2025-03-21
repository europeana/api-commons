package eu.europeana.api.commons.auth.token;

import eu.europeana.api.commons.auth.AuthenticationException;
import eu.europeana.api.commons.auth.AuthenticationHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.http.HttpRequest;

/**
 * @author Hugo
 * @since 11 Mar 2025
 */
public abstract class TokenBasedAuthentication implements AuthenticationHandler {

    protected abstract String getAccessToken();

    @Override
    public void setAuthorization(HttpRequest request)
            throws AuthenticationException {

        String token = getAccessToken();
        if(StringUtils.isBlank(token)) { 
            throw new AuthenticationException(AuthenticationException.TOKEN_MISSING);
        }

        String header = Bearer + " " + token;
        request.setHeader(HEADER_AUTHORIZATION, header);
    }
}
