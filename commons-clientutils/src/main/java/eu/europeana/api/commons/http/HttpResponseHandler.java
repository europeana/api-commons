package eu.europeana.api.commons.http;

import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;

public class HttpResponseHandler implements HttpClientResponseHandler<Void> {

  private String response;
  private int status;

  @Override
  public Void handleResponse(ClassicHttpResponse response) throws HttpException, IOException {
    this.status = response.getCode();
    HttpEntity entity = response.getEntity();
    if(entity!=null) {
      this.response = EntityUtils.toString(entity);
    }
    return null;
  }

  public String getResponse() {
    return response;
  }

  public int getStatus() {
    return status;
  }
  
}
