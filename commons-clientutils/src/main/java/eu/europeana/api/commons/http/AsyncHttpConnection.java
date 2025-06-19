package eu.europeana.api.commons.http;

import eu.europeana.api.commons.auth.AuthenticationHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultRedirectStrategy;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * class that creates basic CloseableHttpAsyncClient.
 * @author srishti singh
 * @since 25 May 2025
 */
public class AsyncHttpConnection {

    private final CloseableHttpAsyncClient httpClient;

    /**
     * Creates a AsyncHttpConnection without redirect strategy
     */
    public AsyncHttpConnection() {
        this(false);
    }


    /**
     * By default - DefaultRedirectStrategy is initialised if nothing is set.
     * To create a connection without following redirects set 'redirectHandlingDisabled' to false
     * @param withRedirect
     */
    public AsyncHttpConnection(boolean withRedirect) {
        // creates a default pool of DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 5 and DEFAULT_MAX_TOTAL_CONNECTIONS = 25
        PoolingAsyncClientConnectionManager connPool = new PoolingAsyncClientConnectionManager();
        if (withRedirect) {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setCircularRedirectsAllowed(true)
                    .build();

            this.httpClient = HttpAsyncClients.custom()
                    .setConnectionManager(connPool)
                    .setDefaultRequestConfig(requestConfig)
                    .setRedirectStrategy(new DefaultRedirectStrategy()).
                    build();

        } else {
            this.httpClient = HttpAsyncClients.custom()
                    .setConnectionManager(connPool)
                    .disableRedirectHandling().build();
        }
    }

    /**
     * Creates the async client with the desired connection pool settings
     * This client will follow the redirects by default
     * @param connectionPool
     */
    public AsyncHttpConnection(PoolingAsyncClientConnectionManager connectionPool) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setCircularRedirectsAllowed(true)
                .build();

        this.httpClient = HttpAsyncClients.custom()
                .setConnectionManager(connectionPool)
                .setDefaultRequestConfig(requestConfig)
                .setRedirectStrategy(new DefaultRedirectStrategy()).
                build();

    }

    /**
     * we need to start the async client before using it;
     * without that, we would get the following exception:
     *   java.lang.IllegalStateException: Request cannot be executed; I/O reactor status: INACTIVE
     */
    public void start() {
        this.httpClient.start();
    }

    /**
     * This method makes GET request for given URL.
     * @param url
     * @param acceptHeaderValue
     * @param auth Authentication handler for the request
     * @return HttpResponseHandler that comprises response body as String and status code.
     * @throws IOException
     */

    public SimpleHttpResponse get(String url, String acceptHeaderValue
            , AuthenticationHandler auth) throws ExecutionException, InterruptedException {
        SimpleHttpRequest httpRequest = new SimpleHttpRequest("GET", url);
        if (StringUtils.isNotBlank(acceptHeaderValue)) {
            httpRequest.setHeader(HttpHeaders.ACCEPT, acceptHeaderValue);
        }
        if (auth!= null)         auth.setAuthorization(httpRequest);
       return httpClient.execute(httpRequest, null).get();
    }

    /**
     * This method makes POST request for given URL and JSON body parameter.
     *
     * @param url
     * @param requestBody
     * @param contentType
     * @param auth Authentication handler for the request
     * @return HttpResponseHandler that comprises response body as String and status code.
     * @throws IOException
     */
    public SimpleHttpResponse post(String url, String requestBody, String contentType
            , AuthenticationHandler auth) throws ExecutionException, InterruptedException {
        SimpleHttpRequest httpRequest = new SimpleHttpRequest("POST", url);
        if (StringUtils.isNotBlank(contentType)) {
            httpRequest.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
        }
        if (auth!= null)         auth.setAuthorization(httpRequest);
        if (requestBody != null) {
            httpRequest.setBody(requestBody, ContentType.create(contentType));
        }
        return httpClient.execute(httpRequest, null).get();
    }



    /**
     * This method makes PUT request for given URL and JSON body parameter.
     *
     * @param url
     * @param jsonParamValue
     * @param auth Authentication handler for the request
     * @return HttpResponseHandler that comprises response body as String and status code.
     * @throws IOException
     */
    public SimpleHttpResponse put(String url, String jsonParamValue
            , AuthenticationHandler auth) throws ExecutionException, InterruptedException {
        SimpleHttpRequest httpRequest = new SimpleHttpRequest("PUT", url);
        if (auth!= null)         auth.setAuthorization(httpRequest);
        if (jsonParamValue != null) {
            httpRequest.setBody(jsonParamValue, null);
        }
        return httpClient.execute(httpRequest, null).get();
    }

    /**
     * This method makes DELETE request for given identifier URL.
     *
     * @param url                       The identifier URL
     * @param auth Authentication handler for the request ( should not be null for deletion)
     * @return HttpResponseHandler that comprises response body as String and status code.
     * @throws IOException
     */
    public SimpleHttpResponse deleteURL(String url, AuthenticationHandler auth) throws ExecutionException, InterruptedException {
        SimpleHttpRequest httpRequest = new SimpleHttpRequest("DELETE", url);
        auth.setAuthorization(httpRequest);
        return httpClient.execute(httpRequest, null).get();
    }

    /**
     * Close the httpclient
     * @throws IOException
     */
    public void close() throws IOException {
        httpClient.close();
    }
}
