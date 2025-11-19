package eu.europeana.api.commons.oauth2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


import eu.europeana.api.commons.auth.AuthenticationBuilder;
import eu.europeana.api.commons.auth.AuthenticationConfig;
import eu.europeana.api.commons.exception.ApiKeyValidationException;
import eu.europeana.api.commons.oauth2.model.KeyValidationResult;
import eu.europeana.api.commons.oauth2.service.impl.EuropeanaClientDetailsService;
import java.io.IOException;
import java.util.Properties;

import org.apache.hc.core5.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

/**
 * This class tests Auth service to fulfill validation for use case "read access"
 * 
 * @author GrafR
 *
 */
public class AuthServiceIT {
	private final Logger log = LogManager.getLogger(getClass());
	private static final String WRONG_API_KEY = "wrongapikey";
	public static final String PROP_API_KEY__SERVICE_URL = "test.apikey.service.url";
	public static final String PROP_TEST_API_KEY = "test.apikey";
	public static final String PROP_TOKEN_ENDPOINT = "test.token.endpoint";
	public static final String PROP_GRANT_PARAMS = "test.grant.param";
	Properties configProps;
	Properties dataProps;

	@Test
	public void testAuthValidationReadAccess() throws ApiKeyValidationException, IOException {
		EuropeanaClientDetailsService client = getApiKeyClientDetailsService();
		KeyValidationResult validationResult = client.validateApiKeyKeycloakClient(
				getDataProperty(PROP_TEST_API_KEY));
		assertNull(validationResult);
	}

	@Test
	public void testAuthValidationWrongKey() throws ApiKeyValidationException, IOException {
		EuropeanaClientDetailsService client = getApiKeyClientDetailsService();
		KeyValidationResult validationResult = client.validateApiKeyKeycloakClient(WRONG_API_KEY);
		assertNotNull(validationResult);
		assertNotNull(validationResult.getValidationError());
		assertEquals(HttpStatus.SC_BAD_REQUEST, validationResult.getHttpStatusCode());
	}
	
	/**
	 * @return log
	 */
	public Logger getLog() {
		return log;
	}

	protected String getConfigProperty(String key) throws IOException {
		if (configProps == null) {
			configProps = new Properties();
			configProps.load(getClass().getResourceAsStream("/test.properties"));
		}
		return (String) configProps.get(key);
	}

	protected String getDataProperty(String key) throws IOException {
		if (dataProps == null) {
			dataProps = new Properties();
			dataProps.load(getClass().getResourceAsStream("/test_data.properties"));
		}
		return (String) dataProps.get(key);
	}
	public EuropeanaClientDetailsService getApiKeyClientDetailsService() throws IOException {
		EuropeanaClientDetailsService clientDetails = new EuropeanaClientDetailsService();
		clientDetails.setApiKeyServiceUrl(getConfigProperty(PROP_API_KEY__SERVICE_URL));
		AuthenticationConfig config = new AuthenticationConfig(loadProperties());
		clientDetails.setAuthHandler(AuthenticationBuilder.newAuthentication(config));
		return clientDetails;
	}
	private Properties loadProperties() throws IOException {
		Properties properties = new Properties();
		properties.setProperty(AuthenticationConfig.CONFIG_TOKEN_ENDPOINT,getConfigProperty(PROP_TOKEN_ENDPOINT));
		properties.setProperty(AuthenticationConfig.CONFIG_GRANT_PARAMS,getConfigProperty(PROP_GRANT_PARAMS));
		return properties;
	}
}