package eu.europeana.api.commons.oauth2.model.impl;

import eu.europeana.api.commons.oauth2.model.ApiCredentials;

public class EuropeanaApiCredentials implements ApiCredentials {

  public static final String USER_ANONYMOUS = "annonymous";

  private String userName;
  private String apiKey;

  /**
   * @deprecated use the constructor {@link #EuropeanaApiCredentials(String, String)} instead
   * @param userName
   */
  @Deprecated
  public EuropeanaApiCredentials(String userName) {
    this(userName, null);
  }

  public EuropeanaApiCredentials(String userName, String apiKey) {
    this.userName = userName;
    this.apiKey = apiKey;
  }

  @Override
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }
}
