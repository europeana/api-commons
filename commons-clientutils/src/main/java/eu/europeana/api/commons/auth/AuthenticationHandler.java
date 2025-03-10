package eu.europeana.api.commons.auth;

import eu.europeana.api.commons.auth.exceptions.CommonAuthenticationException;
import eu.europeana.api.commons.auth.model.ClientCredentials;
import org.apache.hc.core5.http.HttpRequest;

public interface AuthenticationHandler {

    String getOuthServiceUri();

    String getOuthRequestParams();

    ClientCredentials getClientCredentials();

    void setClientCredentials(ClientCredentials clientCredentials);

    ClientCredentials instantiation(String oauthServiceUri, String oauthRequestParams) throws CommonAuthenticationException;

    <T extends HttpRequest> void setAuthorization(T httpRequest) throws CommonAuthenticationException;
}
