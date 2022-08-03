package eu.europeana.api.commons.oauth2.model.impl;

import eu.europeana.api.commons.oauth2.model.ApiCredentials;

public class EuropeanaApiCredentials implements ApiCredentials {

  public static final String USER_ANONYMOUS = "annonymous";
  public static final String CLIENT_UNKNOWN = "unknown";

  private String userName;
  private String clientId;

  /**
   * @deprecated use the constructor {@link #EuropeanaApiCredentials(String, String)} instead
   * @param userName
   */
  @Deprecated
  public EuropeanaApiCredentials(String userName) {
    this(userName, null);
  }

  public EuropeanaApiCredentials(String userName, String clientId) {
    this.userName = userName;
    this.clientId = clientId;
  }

  @Override
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }
}
