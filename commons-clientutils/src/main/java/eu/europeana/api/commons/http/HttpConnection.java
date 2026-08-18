package eu.europeana.api.commons.http;

import java.io.IOException;
import java.util.Map;

import eu.europeana.api.commons.auth.AuthenticationHandler;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultRedirectStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
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
	 * Creates the client with the desired connection pool settings
	 * This client will follow the redirects by default
	 * @param cm connectionPool param
	 */
	public HttpConnection(PoolingHttpClientConnectionManager cm) {
		RequestConfig requestConfig = RequestConfig.custom()
				.build();

		this.httpClient =  HttpClients.custom()
				.setDefaultRequestConfig(requestConfig)
				.setConnectionManager(cm)
				.setRedirectStrategy(new DefaultRedirectStrategy())
				.build();
	}

	/**
	 *This method makes GET request for given URL.
	 * @param url
	 * @param auth Authentication handler for the request
	 * @return HttpResponseHandler that comprises response body as String and status code.
	 * @throws IOException
	 */

	public HttpGet getHttpRequest(String url, String acceptHeaderValue
			, AuthenticationHandler auth) {
		HttpGet get = new HttpGet(url);
		if (acceptHeaderValue != null) {
			get.addHeader(HttpHeaders.ACCEPT, acceptHeaderValue);
		}
		if (auth != null) auth.setAuthorization(get);
		return get;
	}

	/**
	 *This method makes GET request for given URL.
	 * @param url
	 * @param headers map of header name and value that needs to be added in the Url
	 * @param auth Authentication handler for the request
	 * @return HttpResponseHandler that comprises response body as String and status code.
	 * @throws IOException
	 */
	public CloseableHttpResponse get(String url, Map<String, String> headers
			, AuthenticationHandler auth) throws IOException {
		HttpGet get = new HttpGet(url);
		addHeaders(get, headers);
		if (auth != null) auth.setAuthorization(get);
		return executeHttpClient(get);
	}

	/**
	 * Makes POST request with given url,request, headers and authHandler
	 * @param url request URL
	 * @param requestBody body
	 * @param headers Request headers
	 * @param auth Authentication handler for the request
	 * @return CloseableHttpResponse that comprises response body as String and status code.
	 * @throws IOException
	 */
	public CloseableHttpResponse post(String url, String requestBody, Map<String, String> headers
			, AuthenticationHandler auth) throws IOException {
		HttpPost post = new HttpPost(url);
		addHeaders(post,headers);
		if (auth != null) auth.setAuthorization(post);
		if (requestBody != null) {
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
     * @return CloseableHttpResponse that comprises response body as String and status code.
     * @throws IOException
     */
    public CloseableHttpResponse put(String url, String jsonParamValue
                                 , AuthenticationHandler auth) throws IOException {
		HttpPut put = new HttpPut(url);
		if (auth != null) auth.setAuthorization(put);
		put.setEntity(new StringEntity(jsonParamValue));

		return executeHttpClient(put);

	}

    /**
     * This method makes DELETE request for given identifier URL.
     *
     * @param url                       The identifier URL
	 * @param auth Authentication handler for the request
	 * @return CloseableHttpResponse that comprises response body as String and status code.
     * @throws IOException
     */
    public CloseableHttpResponse deleteURL(String url, AuthenticationHandler auth) throws IOException {
		HttpDelete delete = new HttpDelete(url);
		auth.setAuthorization(delete);
		return executeHttpClient(delete);
	}

	/**
	 * Execute the http client for the url passed
	 * @param url url to be execeuted
	 * @param <T> class extending HttpUriRequestBase
	 * @return response handler of the executed request
	 * @throws IOException
	 */
	public <T extends HttpUriRequestBase> CloseableHttpResponse executeHttpClient(T url) throws IOException {
		return httpClient.execute(url);
	}

	private <T extends HttpUriRequestBase> void addHeaders(T url, Map<String, String> headers) {
		if ( headers == null ) { return; }
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			url.setHeader(entry.getKey(), entry.getValue());
		}
	}

	public void close() throws IOException {
		httpClient.close();
	}
}