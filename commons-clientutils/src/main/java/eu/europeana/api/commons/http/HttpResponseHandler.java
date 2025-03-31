package eu.europeana.api.commons.http;

import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;

public class HttpResponseHandler implements HttpClientResponseHandler<Void> {

  private String response;
  private int status;
  private String locationHeader; // fetched only if status is 301

  @Override
  public Void handleResponse(ClassicHttpResponse response) throws HttpException, IOException {
    this.status = response.getCode();
    HttpEntity entity = response.getEntity();
    if (entity!=null) {
      this.response = EntityUtils.toString(entity);
    }
    if (this.status == HttpStatus.SC_MOVED_PERMANENTLY) {
      this.locationHeader = response.getHeader(HttpHeaders.LOCATION).getValue();
    }
    return null;
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
  
}
