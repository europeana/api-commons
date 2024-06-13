/**
 * 
 */
package eu.europeana.api.commons.web.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Willem-Jan Boogerd www.eledge.net/contact
 */
// @JsonInclude(content=Include.NON_EMPTY, value=Include.NON_EMPTY)
@JsonInclude(Include.NON_NULL)
public abstract class ApiResponse {

  public String apikey;

  public String action;

  public boolean success = true;

  public String error;

  public Date statsStartTime;

  public Long statsDuration;

  public Long requestNumber;

  String status;

  String stackTrace;

  public ApiResponse(String apikey, String action) {
    this.apikey = apikey;
    this.action = action;
  }

  public ApiResponse() {
    // used by Jackson
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getStackTrace() {
    return stackTrace;
  }

  public void setStackTrace(String stackTrace) {
    this.stackTrace = stackTrace;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }
}
