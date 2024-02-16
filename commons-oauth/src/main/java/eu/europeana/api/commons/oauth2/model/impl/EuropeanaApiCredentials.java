package eu.europeana.api.commons.oauth2.model.impl;

import eu.europeana.api.commons.oauth2.model.ApiCredentials;

public class EuropeanaApiCredentials implements ApiCredentials {

  public static final String USER_ANONYMOUS = "annonymous";
  public static final String CLIENT_UNKNOWN = "unknown";

  private String userName;
  private String clientId;
  private String apiKey;
  private String affiliation;

  /**
   * @deprecated use the constructor {@link #EuropeanaApiCredentials(String, String,String)} instead
   * @param userName User name for credentials
   */
  @Deprecated
  public EuropeanaApiCredentials(String userName) {
    this(userName, null);
  }
  @Deprecated(since = "DEC-2023" )
  public EuropeanaApiCredentials(String userName, String clientId) {
    this.userName = userName;
    this.clientId = clientId;

  }
  public EuropeanaApiCredentials(String userName, String clientId,String apikey, String affiliation) {
    this.userName = userName;
    this.clientId = clientId;
    this.apiKey = apikey;
    this.affiliation = affiliation;
  }

  @Override
  public String getUserName() {
    return userName;
  }
  
  @Override
  public String getAffiliation() {
    return affiliation;
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

  public String getApiKey() { return apiKey; }

  public void setApiKey(String apiKey) { this.apiKey = apiKey;  }
}
