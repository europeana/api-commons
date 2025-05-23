package eu.europeana.api.commons.http;

import java.io.IOException;

import eu.europeana.api.commons.auth.AuthenticationHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultRedirectStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.entity.StringEntity;

/**
 * The class encapsulating simple HTTP access.
 * Handles the authentication for the clients as well via AuthenticationHandler
 *
 * @author Srishti Singh
 * @since 26 November 2024
 */
public class HttpConnection {

    private final CloseableHttpClient httpClient;

	/**
	 * Creates a HttpConnection without redirect strategy
	 */
	public HttpConnection() {
		this(false);
	}

	/**
	 * By default - DefaultRedirectStrategy is initialised if nothing is set.
	 * To create a connection without following redirects set 'redirectHandlingDisabled' to false
	 * @param withRedirect
	 */
	public HttpConnection(boolean withRedirect) {
		if (withRedirect) {
			RequestConfig requestConfig = RequestConfig.custom()
					.setCircularRedirectsAllowed(true)
					.build();

			this.httpClient =  HttpClients.custom()
					.setDefaultRequestConfig(requestConfig)
					.setRedirectStrategy(new DefaultRedirectStrategy())
					.build();
		} else {
			HttpClientBuilder clientBuilder = HttpClientBuilder.create();
			clientBuilder.disableRedirectHandling();
			this.httpClient = clientBuilder.build();
		}
	}

	/**
	 *This method makes GET request for given URL.
	 * @param url
	 * @param acceptHeaderValue
	 * @param auth Authentication handler for the request
	 * @return HttpResponseHandler that comprises response body as String and status code.
	 * @throws IOException
	 */

	public HttpGet getHttpRequest(String url, String acceptHeaderValue
			, AuthenticationHandler auth) throws IOException {
		HttpGet get = new HttpGet(url);
		if(StringUtils.isNotBlank(acceptHeaderValue)) {
			addHeaders(get, HttpHeaders.ACCEPT, acceptHeaderValue);
		}
		auth.setAuthorization(get);
		return get;
	}

	/**
	 *This method makes GET request for given URL.
	 * @param url
	 * @param acceptHeaderValue
	 * @param auth Authentication handler for the request
	 * @return HttpResponseHandler that comprises response body as String and status code.
	 * @throws IOException
	 */

	public HttpResponseHandler get(String url, String acceptHeaderValue
	                             , AuthenticationHandler auth) throws IOException {
		HttpGet get = getHttpRequest(url, acceptHeaderValue, auth);
		return executeHttpClient(get);
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
    public HttpResponseHandler post(String url, String requestBody, String contentType
                                  , AuthenticationHandler auth) throws IOException {
        HttpPost post = new HttpPost(url);
        if(StringUtils.isNotBlank(contentType)) {
            addHeaders(post, HttpHeaders.CONTENT_TYPE, contentType);
        }
        auth.setAuthorization(post);
        if(requestBody != null) {
            post.setEntity(new StringEntity(requestBody));
        }
		return executeHttpClient(post);
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
    public HttpResponseHandler put(String url, String jsonParamValue
                                 , AuthenticationHandler auth) throws IOException {
		HttpPut put = new HttpPut(url);
        auth.setAuthorization(put);
		put.setEntity(new StringEntity(jsonParamValue));

		return executeHttpClient(put);

	}

    /**
     * This method makes DELETE request for given identifier URL.
     *
     * @param url                       The identifier URL
	 * @param auth Authentication handler for the request
	 * @return HttpResponseHandler that comprises response body as String and status code.
     * @throws IOException
     */
    public HttpResponseHandler deleteURL(String url, AuthenticationHandler auth) throws IOException {
		HttpDelete delete = new HttpDelete(url);
		auth.setAuthorization(delete);
		return executeHttpClient(delete);
	}


    public <T extends HttpUriRequestBase> HttpResponseHandler executeHttpClient(T url) throws IOException {
      HttpResponseHandler responseHandler = new HttpResponseHandler();
      httpClient.execute(url, responseHandler); 
      return responseHandler;
	}

	private <T extends HttpUriRequestBase> void addHeaders(T url, String headerName, String headerValue) {
		if (StringUtils.isNotBlank(headerValue)) {
			url.setHeader(headerName, headerValue);
		}
	}
}
