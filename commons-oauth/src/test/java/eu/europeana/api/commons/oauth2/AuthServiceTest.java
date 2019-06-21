package eu.europeana.api.commons.oauth2;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import eu.europeana.apikey.client.Connector;
import eu.europeana.apikey.client.ValidationResult;
import eu.europeana.apikey.client.exception.ApiKeyValidationException;

/**
 * This class tests Auth service to fulfill validation for use case "read access"
 * 
 * @author GrafR
 *
 */
public class AuthServiceTest {

	private final Logger log = LogManager.getLogger(getClass());
	
	private String ADMINAPIKEY = "ApiKey1";
	private String ADMINSECRETKEY = "PrivateKey1";
	private String API_KEY = "ApiKey2";
	private String TEST_API = "entity";

	@Test
	public void testAuthValidationReadAccess() throws ApiKeyValidationException {

    	    Connector connector = new Connector();
    	    ValidationResult validationResult = connector.validateApiKey(
    		    ADMINAPIKEY, ADMINSECRETKEY, API_KEY, TEST_API);
    	    assertNotNull(validationResult);
    	    assertTrue(validationResult.isValidKey());
	}
	
	/**
	 * @return
	 */
	public Logger getLog() {
		return log;
	}

}
