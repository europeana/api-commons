package eu.europeana.api.commons.auth.impl;

import eu.europeana.api.commons.auth.AuthenticationHandler;
import eu.europeana.api.commons.auth.exceptions.CommonAuthenticationException;
import eu.europeana.api.commons.auth.model.ClientCredentials;
import eu.europeana.api.commons.http.HttpConnection;
import eu.europeana.api.commons.http.HttpResponseHandler;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpStatus;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public abstract class BaseAuthenticationHandler implements AuthenticationHandler {

    private static final String access_token = "access_token";
    private static final String refresh_token = "refresh_token";
    private static final String Bearer = "Bearer";
    private static final String space = " ";
    private static final String expires_in = "expires_in";

    @Override
    public ClientCredentials instantiation(String oauthServiceUri, String oauthRequestParams) throws CommonAuthenticationException {
        try {
            HttpConnection connection = new HttpConnection();
            long tokenRequestTimeinSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()); // in seconds

            HttpResponseHandler response = connection.post(oauthServiceUri, oauthRequestParams, "application/x-www-form-urlencoded");
            if (HttpStatus.SC_OK == response.getStatus()) {
                JSONObject json = new JSONObject(response.getResponse());
                if (json.has(access_token)) {
                    ClientCredentials clientCredentials =  new ClientCredentials(Bearer + space + json.getString(access_token));
                    if (json.has(expires_in)) {
                        clientCredentials.setExpirationTime(getExpirationTime(tokenRequestTimeinSeconds, (json.getLong(expires_in))));
                    }
                    if (json.has(refresh_token)) {
                        clientCredentials.setRefreshToken(json.getString(refresh_token));
                    }
                    return clientCredentials;
                } else {
                    throw new CommonAuthenticationException("Cannot extract authentication token from reponse:" + response.getResponse());
                }
            } else {
                throw new CommonAuthenticationException("Error occured when calling oath service! " + response);
            }
        } catch (IOException | JSONException e) {
            throw new CommonAuthenticationException("Cannot retrieve authentication token!", e);
        }

    }

    /**
     * Set Authorisation method - Handles the issue of token if necessary and add it to the HttpRequest of the client
     * if the token provided is still valid - set the 'AUTHORIZATION' header in the request
     * if token is NOT valid - issue the new/refresh token and attach that to the request
     *
     * Also update the client credentials if new/refresh token is generated
     *
     * @param httpRequest htt request of the client
     * @throws CommonAuthenticationException
     */
    @Override
    public <T extends HttpRequest> void setAuthorization(T httpRequest) throws CommonAuthenticationException {
        if (getClientCredentials() != null && getClientCredentials().getToken() != null) {
            if (hasTokenExpired(getClientCredentials().getExpirationTime())) {
                // for refresh token request - the url is same but the poarams must have 'grant_type=refresh_token' and refresh_token
                ClientCredentials newCredentials = instantiation(getOuthServiceUri(), getOuthRequestParams());
                // update the old credentials
                getClientCredentials().setToken(newCredentials.getToken());
                getClientCredentials().setExpirationTime(newCredentials.getExpirationTime());
            }
            // set authentication in the http request
            httpRequest.setHeader(HttpHeaders.AUTHORIZATION, getClientCredentials().getToken());
        }

    }


    /**
     * expires = token_request_time  + expires_in (from jwt token request) - expires_adjustment ( 5minutes = 60 seconds)
     * @param tokenRequestTime
     * @param expirationTimeForJwtToken
     * @return
     */
    private static long getExpirationTime(long tokenRequestTime, long expirationTimeForJwtToken) {
        return tokenRequestTime + expirationTimeForJwtToken - 60;

    }

    /**
     * Checks if the token has expired or not
     * @param tokenExpirationTime
     * @return
     */
    private static boolean hasTokenExpired(long tokenExpirationTime) {
        long currentTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        if (tokenExpirationTime < currentTime) {
            return true;
        }
        return false;
    }

}
