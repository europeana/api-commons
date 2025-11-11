package eu.europeana.api.commons.exception;

public class ApiKeyValidationException extends Exception {

  private static final long serialVersionUID = 6387090087297106543L;

  public ApiKeyValidationException(String errorMessage) {
    super(errorMessage);
  }

  public ApiKeyValidationException(String errorMessage, Exception cause) {
    super(errorMessage, cause);
  }
}