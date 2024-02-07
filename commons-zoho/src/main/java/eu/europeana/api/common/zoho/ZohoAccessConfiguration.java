package eu.europeana.api.common.zoho;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:zoho-access.properties", ignoreResourceNotFound = true)
public class ZohoAccessConfiguration {

  /*For Zoho US*/



  @Value("${zoho.us.username:#{null}}")
  private String zohoUSUserName;

  @Value("${zoho.us.client.id:#{null}}")
  private String zohoUSClientId;

  @Value("${zoho.us.client.secret:#{null}}")
  private String zohoUSClientSecret;

  @Value("${zoho.us.refresh.token:#{null}}")
  private String zohoUSRefreshToken;

  @Value("${zoho.us.redirect.url:#{null}}")
  private String zohoUSRedirectUrl;


  /*For Zoho EU*/

  @Value("${zoho.eu.username:#{null}}")
  private String zohoEUUserName;

  @Value("${zoho.eu.client.id:#{null}}")
  private String zohoEUClientId;

  @Value("${zoho.eu.client.secret:#{null}}")
  private String zohoEUClientSecret;

  @Value("${zoho.eu.refresh.token:#{null}}")
  private String zohoEURefreshToken;

  @Value("${zoho.eu.redirect.url:#{null}}")
  private String zohoEURedirectUrl;

  public String getZohoUSUserName() {
    return zohoUSUserName;
  }

  public String getZohoUSClientId() {
    return zohoUSClientId;
  }

  public String getZohoUSClientSecret() {
    return zohoUSClientSecret;
  }

  public String getZohoUSRefreshToken() {
    return zohoUSRefreshToken;
  }

  public String getZohoUSRedirectUrl() {
    return zohoUSRedirectUrl;
  }

  public String getZohoEUUserName() {
    return zohoEUUserName;
  }

  public String getZohoEUClientId() {
    return zohoEUClientId;
  }

  public String getZohoEUClientSecret() {
    return zohoEUClientSecret;
  }

  public String getZohoEURefreshToken() {
    return zohoEURefreshToken;
  }

  public String getZohoEURedirectUrl() {
    return zohoEURedirectUrl;
  }
}
