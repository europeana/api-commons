package eu.europeana.api.common.zoho;

import com.zoho.api.authenticator.OAuthToken;
import com.zoho.api.authenticator.Token;
import com.zoho.api.authenticator.store.TokenStore;
import com.zoho.api.logger.Logger;
import com.zoho.api.logger.Logger.Levels$;
import com.zoho.crm.api.Initializer;
import com.zoho.crm.api.SDKConfig;
import com.zoho.crm.api.UserSignature;
import com.zoho.crm.api.dc.DataCenter.Environment;
import com.zoho.crm.api.dc.EUDataCenter;
import com.zoho.crm.api.dc.USDataCenter;
import org.apache.commons.lang3.SystemUtils;


public class ZohoAccessClient {
  public boolean initialize(
      TokenStore tokenStore,
      String username,
      String clientId,
      String clientSecret,
      String refreshToken,
      String redirectUrl,
      DataCenterVal dataCenter
      )
      throws RuntimeException {
    try {

      Logger logger = new Logger.Builder()
          .filePath("/Users//user//Documents//java_sdk_log.log")
          .level(Levels$.MODULE$.INFO())
          .build();
      
      Environment environment =  getEnvironment(dataCenter);

      UserSignature userSignature = new UserSignature(username);

      Token token = new OAuthToken.Builder()
          .userSignature(userSignature)
          .clientID(clientId)
          .clientSecret(clientSecret)
          .refreshToken(refreshToken)
          .redirectURL(redirectUrl)
          .build();

      SDKConfig sdkConfig = new SDKConfig.Builder()
          .autoRefreshFields(false)
          .pickListValidation(true)
          .build();

      String resourcePath = SystemUtils.getUserHome().getAbsolutePath();
      // Does not generate any tokens, we'll need to execute a command to do so

      new Initializer.Builder()
          .environment(environment)
          .token(token)
          .store(tokenStore)
          .SDKConfig(sdkConfig)
          .resourcePath(resourcePath)
          .logger(logger)
          .initialize();

      return true;

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  private static Environment getEnvironment(DataCenterVal dataCenter) {
    Environment environment= USDataCenter.PRODUCTION();

    switch (dataCenter) {
      case US:
        environment = USDataCenter.PRODUCTION();
        break;
      case EU:
        environment = EUDataCenter.PRODUCTION();
        break;
    }
    return environment;
  }
}
