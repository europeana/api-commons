package eu.europeana.api.commons.oauth2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Properties;

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import eu.europeana.apikey.client.ApiKeyValidationResult;
import eu.europeana.apikey.client.Client;
import eu.europeana.apikey.client.exception.ApiKeyValidationException;

/**
 * This class tests Auth service to fulfill validation for use case "read access"
 * 
 * @author GrafR
 *
 */
public class AuthServiceTest {

	private final Logger log = LogManager.getLogger(getClass());
	
	private String WRONG_API_KEY = "wrongapikey";
	
	public static final String PROP_API_KEY__SERVICE_URL = "test.apikey.service.url";
	public static final String PROP_TEST_API_KEY = "test.apikey";
	
	Properties configProps;
	Properties dataProps;
	

	@Test
	public void testAuthValidationReadAccess() throws ApiKeyValidationException, IOException {

    	    Client client = new Client(getConfigProperty(PROP_API_KEY__SERVICE_URL));
    	    String TEST_API_KEY = getDataProperty(PROP_TEST_API_KEY);
	    ApiKeyValidationResult validationResult = client.validateApiKey(
    		TEST_API_KEY);
    	    assertNotNull(validationResult);
    	    assertTrue(validationResult.isValidApiKey());
	}
	
	@Test
	public void testAuthValidationWrongKey() throws ApiKeyValidationException, IOException {

    	    Client client = new Client(getConfigProperty(PROP_API_KEY__SERVICE_URL));
    	    ApiKeyValidationResult validationResult = client.validateApiKey(
    		WRONG_API_KEY);
    	    assertNotNull(validationResult);
    	    assertFalse(validationResult.isValidApiKey());
    	    assertEquals(HttpStatus.SC_UNAUTHORIZED, validationResult.getHttpStatus());
	}
	
	/**
	 * @return
	 */
	public Logger getLog() {
		return log;
	}

	protected String getConfigProperty(String key) throws IOException {
	    if(configProps == null) {
		configProps = new Properties();
		configProps.load(getClass().getResourceAsStream("/test.properties"));
	    }
	    
	    return (String) configProps.get(key);
	}
	
	protected String getDataProperty(String key) throws IOException {
	    if(dataProps == null) {
		dataProps = new Properties();
		dataProps.load(getClass().getResourceAsStream("/test_data.properties"));
	    }
	    
	    return (String) dataProps.get(key);
	}
}
