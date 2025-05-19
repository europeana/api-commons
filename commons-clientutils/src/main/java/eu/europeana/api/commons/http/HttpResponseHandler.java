package eu.europeana.api.commons.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HttpResponseHandler implements HttpClientResponseHandler<Void> {

  private String response;
  private int status;
  private String locationHeader;
  private List<Header> cachingHeaders;

  @Override
  public Void handleResponse(ClassicHttpResponse response) throws HttpException, IOException {
    this.status = response.getCode();
    HttpEntity entity = response.getEntity();
    if (entity!=null) {
      this.response = EntityUtils.toString(entity);
    }
    cachingHeaders = new ArrayList<>(3);
    getHeaders(response);

    return null;
  }

  private void getHeaders(ClassicHttpResponse response) {
    cachingHeaders = new ArrayList<>(3);

    Iterator<Header> headers = response.headerIterator();
    while (headers.hasNext()) {
      Header header = headers.next();
      if (StringUtils.equalsAny(header.getName(), HttpHeaders.ETAG, HttpHeaders.LAST_MODIFIED, HttpHeaders.CACHE_CONTROL)) {
        cachingHeaders.add(header);
      }
      if (StringUtils.equals(header.getName(), HttpHeaders.LOCATION)) {
        this.locationHeader = header.getValue();
      }
    }
  }

  public String getResponse() {
    return response;
  }

  public int getStatus() {
    return status;
  }

  public String getLocationHeader() {
    return locationHeader;
  }

  public List<Header> getCachingHeaders() {
    return cachingHeaders;
  }
}
