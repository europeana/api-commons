package eu.europeana.api.commons.config;

import static eu.europeana.api.commons.config.ErrorConfig.*;
public enum ErrorMessage {
  TOKEN_INVALID_401("401_token_invalid",  "Token is invalid", TOKEN_INVALID),
  USER_NOT_AUTHORISED_403("403_user_not_authorised",  "User not authorised to access the resource", USER_NOT_AUTHORISED);

  private final String code;
  private final String error;
  private final String i18nKey;

  ErrorMessage(String code, String error, String i18nKey) {
    this.code = code;
    this.error = error;
    this.i18nKey = i18nKey;
  }
  public String getCode() {return code;}
  public String getError() {return error; }
  public String getI18nKey() {return i18nKey;}
}