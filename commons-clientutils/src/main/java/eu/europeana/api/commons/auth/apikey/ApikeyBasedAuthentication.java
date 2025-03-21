/**
 * 
 */
package eu.europeana.api.commons.auth.apikey;

import eu.europeana.api.commons.auth.AuthenticationException;
import eu.europeana.api.commons.auth.AuthenticationHandler;
import org.apache.hc.core5.http.HttpRequest;

/**
 * @author Hugo
 * @since 11 Mar 2025
 */
public class ApikeyBasedAuthentication implements AuthenticationHandler {

    private String apikey;

    public ApikeyBasedAuthentication(String apikey) {
        this.apikey = apikey;
    }

    public void setApiKey(String apikey) {
        this.apikey = apikey;
    }

    @Override
    public void setAuthorization(HttpRequest request)
            throws AuthenticationException {
        request.setHeader(HEADER_APIKEY, this.apikey);
    }

}
