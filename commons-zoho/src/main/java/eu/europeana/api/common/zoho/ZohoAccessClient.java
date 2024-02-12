package eu.europeana.api.common.zoho;

import com.zoho.api.authenticator.OAuthToken;
import com.zoho.api.authenticator.Token;
import com.zoho.api.authenticator.store.TokenStore;
import com.zoho.crm.api.Initializer;
import com.zoho.crm.api.SDKConfig;
import com.zoho.crm.api.UserSignature;
import com.zoho.crm.api.dc.DataCenter.Environment;
import com.zoho.crm.api.dc.EUDataCenter;
import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ZohoAccessClient {

    public boolean initialize(
        TokenStore tokenStore,
        String username,
        String clientId,
        String clientSecret,
        String refreshToken,
        String redirectUrl
                             )
        throws RuntimeException {
        try {

            Logger logger = LogManager.getLogger(getClass());

            Environment environment = EUDataCenter.PRODUCTION;

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
                .initialize();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
