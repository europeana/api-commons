/*
 * HttpConnector.java - europeana4j
 * (C) 2011 Digibis S.L.
 */
package eu.europeana.api.commons.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;


/**
 * The class encapsulating simple HTTP access.
 * @author Srishti Singh
 *
 */
public class HttpConnection {

    private final CloseableHttpClient httpClient;

    public HttpConnection() {
        httpClient = HttpClientBuilder.create().build();
    }

    /**
     * This method makes POST request for given URL and JSON body parameter.
     *
     * @param url
     * @param requestBody
     * @param contentType
     * @return HttpResponseHandler that comprises response body as String and status code.
     * @throws IOException
     */
    public HttpResponseHandler post(String url, String requestBody, String contentType) throws IOException {
        HttpPost post = new HttpPost(url);
        if(StringUtils.isNotBlank(contentType)) {
          addHeaders(post, HttpHeaders.CONTENT_TYPE, contentType);
        }
        if(requestBody!=null) {
          post.setEntity(new StringEntity(requestBody));
        }
		return executeHttpClient(post);
	}


    private <T extends HttpUriRequestBase> HttpResponseHandler executeHttpClient(T url) throws IOException {
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
