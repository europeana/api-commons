package eu.europeana.api.commons.oauth2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.http.HttpHeaders;
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
	
	private String TEST_API_KEY = "tromssusequ";
	private String WRONG_API_KEY = "wrongapikey";

	@Test
	public void testAuthValidationReadAccess() throws ApiKeyValidationException {

    	    Client client = new Client();
    	    ApiKeyValidationResult validationResult = client.validateApiKey(
    		TEST_API_KEY);
    	    assertNotNull(validationResult);
    	    assertTrue(validationResult.isValidApiKey());
	}
	
	@Test
	public void testAuthValidationWrongKey() throws ApiKeyValidationException {

    	    Client client = new Client();
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

}
