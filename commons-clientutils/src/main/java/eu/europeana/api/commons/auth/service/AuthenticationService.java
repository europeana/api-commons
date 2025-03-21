/**
 * 
 */
package eu.europeana.api.commons.auth.service;

import java.io.IOException;

import eu.europeana.api.commons.auth.AuthenticationException;
import eu.europeana.api.commons.http.HttpResponseHandler;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;


import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Hugo
 * @since 11 Mar 2025
 */
public class AuthenticationService {

    private final CloseableHttpClient httpClient;

    private String tokenEndpoint;

    private ObjectMapper objMapper = buildMapper();

    public AuthenticationService(String tokenEndpoint) {
        this.tokenEndpoint = tokenEndpoint;
        this.httpClient = HttpClientBuilder.create().build();
    }

    public TokenResponse newToken(AuthGrant grant) 
           throws AuthenticationException {

        try {
            HttpResponseHandler rsp = new HttpResponseHandler();
            this.httpClient.execute(newTokenRequest(grant), rsp);
    
            if (rsp.getStatus() != HttpStatus.SC_OK) {
                AuthenticationException e = this.objMapper.readerFor(AuthenticationException.class).readValue(rsp.getResponse());
                throw e;
            }

            return this.objMapper.readerFor(TokenResponse.class).readValue(rsp.getResponse());
        }
        catch (IOException e) {
            throw new AuthenticationException(AuthenticationException.AUTH_SERVICE_ERROR, e);
        }
    }

    private HttpPost newTokenRequest(AuthGrant grant) {
        HttpPost post = new HttpPost(this.tokenEndpoint);
        post.setEntity(grant.getEntity());
        return post;
    }


    public static ObjectMapper buildMapper() {
        ObjectMapper mapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        mapper.registerModule(module);

        mapper.setVisibility(
                mapper.getVisibilityChecker()
                        .withCreatorVisibility(NONE)
                        .withFieldVisibility(NONE)
                        .withGetterVisibility(NONE)
                        .withIsGetterVisibility(NONE)
                        .withSetterVisibility(NONE));

        
        mapper.findAndRegisterModules();
        return mapper;
    }

}
