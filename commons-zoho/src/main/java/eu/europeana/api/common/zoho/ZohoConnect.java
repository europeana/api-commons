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
 * Created on 14 feb 2024
 */
public class ZohoConnect {


	Logger LOG = LogManager.getLogger(ZohoConnect.class);
	private final ZohoAccessConfiguration config;

	// not clear if this is actually used. Maybe delete if not.
	private final ZohoInMemoryTokenStore  tokenStore;

	public ZohoConnect(){
		this.config = new ZohoAccessConfiguration();
		this.tokenStore  = new ZohoInMemoryTokenStore();
	}


	public boolean ConnectToZoho() {
		try {
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

			// example usage, taken from Zoho's samples 
			String moduleAPIName = "Leads";
			getRecords(moduleAPIName);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
