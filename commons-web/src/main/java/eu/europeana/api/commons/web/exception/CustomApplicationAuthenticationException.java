package eu.europeana.api.commons.web.exception;

import eu.europeana.api.commons.oauth2.model.KeyValidationResult;
import org.springframework.http.HttpStatus;

public class CustomApplicationAuthenticationException extends ApplicationAuthenticationException{
  KeyValidationResult result;
  public KeyValidationResult getResult() {
    return result;
  }
  public CustomApplicationAuthenticationException(String message, String i18nKey,
      String[] i18nParams,HttpStatus status, Throwable th,KeyValidationResult result) {
    super(message, i18nKey, i18nParams, status, th);
    this.result =result;
  }

}