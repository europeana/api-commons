package eu.europeana.api.commons.oauth2;


import eu.europeana.api.commons.auth.AuthenticationBuilder;
import eu.europeana.api.commons.auth.AuthenticationConfig;
import eu.europeana.api.commons.exception.ApiKeyValidationException;
import eu.europeana.api.commons.exception.EuropeanaClientRegistrationException;
import eu.europeana.api.commons.oauth2.service.impl.EuropeanaClientDetailsService;
import java.io.IOException;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.provider.ClientDetails;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests Auth service to fulfill validation for use case "read access"
 * 
 * @author GrafR
 *
 */
public class AuthServiceIT {
	private static final String WRONG_API_KEY = "wrongapikey";
	public static final String PROP_API_KEY__SERVICE_URL = "test.apikey.service.url";
	public static final String PROP_TEST_API_KEY = "test.apikey";
	public static final String PROP_TOKEN_ENDPOINT = "test.token.endpoint";
	public static final String PROP_GRANT_PARAMS = "test.grant.param";
	Properties configProps;


	@Test
	public void testAuthValidationReadAccess() throws ApiKeyValidationException, IOException {
		EuropeanaClientDetailsService client = getApiKeyClientDetailsService();
		ClientDetails details = client.validateApiKeyKeycloakClient(
				getConfigProperty(PROP_TEST_API_KEY));
		assertNotNull(details);
		assertNotNull(details.getAdditionalInformation());

	}

	@Test
	public void testAuthValidationWrongKey() throws IOException {
		EuropeanaClientDetailsService client = getApiKeyClientDetailsService();
		EuropeanaClientRegistrationException ex = assertThrows(
				EuropeanaClientRegistrationException.class,
				() ->  client.validateApiKeyKeycloakClient(WRONG_API_KEY)
		);

		assertEquals(401, ex.getHttpStatusCode());
		assertEquals("401_key_invalid", ex.getCode());
		assertEquals("Please register for an API key", ex.getMessage());
		assertEquals("API key is invalid", ex.getError());
	}

	protected String getConfigProperty(String key) throws IOException {
		if (configProps == null) {
			configProps = new Properties();
			configProps.load(getClass().getResourceAsStream("/test.properties"));
		}
		return (String) configProps.get(key);
	}

	public EuropeanaClientDetailsService getApiKeyClientDetailsService() throws IOException {
		EuropeanaClientDetailsService clientDetails = new EuropeanaClientDetailsService();
		clientDetails.setApiKeyServiceUrl(getConfigProperty(PROP_API_KEY__SERVICE_URL));
		AuthenticationConfig config = new AuthenticationConfig(getConfigProperty(PROP_TOKEN_ENDPOINT), getConfigProperty(PROP_GRANT_PARAMS));
		clientDetails.setAuthHandler(AuthenticationBuilder.newAuthentication(config));
		return clientDetails;
	}
}