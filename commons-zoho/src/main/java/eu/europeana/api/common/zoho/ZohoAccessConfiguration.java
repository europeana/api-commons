package eu.europeana.api.common.zoho;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Shweta Nazare, Sergiu Gordea, Luthien Dulk
 * Created on 12 feb 2024
 */
@Configuration
@PropertySource(value = "classpath:zoho.user.properties", ignoreResourceNotFound = true)
public class ZohoAccessConfiguration {

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
