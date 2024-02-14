package eu.europeana.api.common.zoho;

import static eu.europeana.api.common.zoho.GetRecords.getRecords;

import com.zoho.api.authenticator.OAuthToken;
import com.zoho.api.authenticator.Token;
import com.zoho.api.authenticator.store.TokenStore;
import com.zoho.crm.api.Initializer;
import com.zoho.crm.api.dc.DataCenter.Environment;
import com.zoho.crm.api.dc.EUDataCenter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main application
 *
 * @author Luthien Dulk
 * Created on 12 feb 2024
 */
//@SpringBootApplication
public class ZohoExampleApplication {


	Logger LOG = LogManager.getLogger(ZohoExampleApplication.class);
	private static ZohoAccessConfiguration config = new ZohoAccessConfiguration();
	private static ZohoInMemoryTokenStore  tokenStore;

	public static void main(String[] args) {
		try
		{
			Environment environment = EUDataCenter.PRODUCTION;
			TokenStore  tokenStore  = new ZohoInMemoryTokenStore();

			Token token = new OAuthToken
				.Builder()
				.clientID(config.getZohoClientId())
				.clientSecret(config.getZohoClientSecret())
				.refreshToken(config.getZohoRefreshToken())
				.redirectURL(config.getZohoRedirectUrl())
				.build();

			new Initializer.Builder()
				.environment(environment)
				.token(token)
				.store(tokenStore)
				.initialize();

			String moduleAPIName = "Leads";
			getRecords(moduleAPIName);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
