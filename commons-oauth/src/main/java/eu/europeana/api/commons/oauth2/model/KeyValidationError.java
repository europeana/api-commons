package eu.europeana.api.commons.oauth2.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KeyValidationError {
  @JsonProperty
  private  String code;
  @JsonProperty
  private String error;
  @JsonProperty
  private  String message;

  public KeyValidationError(){}

  public KeyValidationError(String code, String error, String message) {
    this.code = code;
    this.error = error;
    this.message = message;
  }

  public String getCode() {
    return code;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}