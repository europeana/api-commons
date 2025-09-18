package eu.europeana.api.commons.oauth2.model;

public class KeyValidationResult {

  private int httpStatusCode;
  private KeyValidationError validationError;

  public KeyValidationResult(int httpStatusCode, KeyValidationError validationError) {
    this.httpStatusCode = httpStatusCode;
    this.validationError = validationError;
  }

  public int getHttpStatusCode() {
    return httpStatusCode;
  }

  public void setHttpStatusCode(int httpStatusCode) {
    this.httpStatusCode = httpStatusCode;
  }

  public KeyValidationError getValidationError() {
    return validationError;
  }

  public void setValidationError(KeyValidationError validationError) {
    this.validationError = validationError;
  }
}