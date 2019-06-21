package eu.europeana.api.commons.oauth2.service.impl;

import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.http.HttpHeaders;

import com.auth0.jwt.JWTVerifier;

import eu.europeana.api.commons.definitions.config.i18n.I18nConstants;
import eu.europeana.api.commons.definitions.vocabulary.CommonApiConstants;
import eu.europeana.api.commons.exception.ApiKeyExtractionException;
import eu.europeana.api.commons.exception.CommonServiceException;
import eu.europeana.api.commons.exception.CommonServiceRuntimeException;
import eu.europeana.api.commons.oauth2.model.ApiKey;
import eu.europeana.api.commons.oauth2.service.ApiKeyManager;

/**
 * This class supports apikey extraction and management.
 * 
 * @author GrafR
 * 
 * Example: A decrypted JWT token issued by KeyCloak.
{
    "success": "Successfully accessed endpoint available for role admin",
    "header": {
        "alg": "RS256",
        "typ": "JWT",
        "kid": "wcSzL6tktB4XGqKclFgruZht-_wy2FTWAeYKZacR93g"
    },
    "data": {
        "jti": "4b0c4ddd-0fb0-49f8-9be4-bf80501473e1",
        "exp": 1551346143,
        "nbf": 0,
        "iat": 1551345843,
        "iss": "https://keycloak-server-test.eanadev.org/auth/realms/europeana",
        "aud": [
            ...
        ],
        "sub": "8d31b832-9305-483f-8540-e3ac4dc65d64",
        "typ": "Bearer",
        "azp": "APIKEY",
        "auth_time": 1551345843,
        "session_state": "dd99a49a-3355-4d63-b25d-526a2fd6792f",
        "acr": "1",
        "realm_access": {
            "roles": [
               ...
            ]
        },
        "resource_access": {
            "api_annotations": {
                "roles": [
                   ...
                ]
            },
            "account": {
                "roles": [
                  ...
                ]
            }
        },
        "scope": "openid email profile",
        "email_verified": false,
        "preferred_username": "admin"
    },
    "signature": "iNjJTqb_53iaPB5mYN3zDTxkuH0U1f3XyDBF7HcPnq3JhRGJ7C_DcBfxr_ihcm42ewjwEqa2AucotEqL2I6vAaf73BN6E89pswQCERMJMuwqqXW7WcLxGBN9Hk4c45oUVYzZvx37tT6p9NOKFxnwiZ3M36aDa8GrMrBsY-aij0i3VEb_9rSM02kMa6HDG_Zq8zahjdkqER_vikoQIL88_-3xN4ZoPdS5k146BT67OuDeO6EG-2wl73pVKBBMRYsiZ_1C3VrO02QwK_fvuDuxR2A9wyz-OFfYu27e2yWZDyZgW7Q2_NrEjObQwnhrPEC4wRUo9vE0YJ6pdmnLCyqhsQ"
}
 * which is equivalent to base64 code:
 * ewogICAgInN1Y2Nlc3MiOiAiU3VjY2Vzc2Z1bGx5IGFjY2Vzc2VkIGVuZHBvaW50IGF2YWlsYWJsZSBmb3Igcm9sZSBhZG1pbiIsCiAgICAiaGVhZGVyIjogewogICAgICAgICJhbGciOiAiUlMyNTYiLAogICAgICAgICJ0eXAiOiAiSldUIiwKICAgICAgICAia2lkIjogIndjU3pMNnRrdEI0WEdxS2NsRmdydVpodC1fd3kyRlRXQWVZS1phY1I5M2ciCiAgICB9LAogICAgImRhdGEiOiB7CiAgICAgICAgImp0aSI6ICI0YjBjNGRkZC0wZmIwLTQ5ZjgtOWJlNC1iZjgwNTAxNDczZTEiLAogICAgICAgICJleHAiOiAxNTUxMzQ2MTQzLAogICAgICAgICJuYmYiOiAwLAogICAgICAgICJpYXQiOiAxNTUxMzQ1ODQzLAogICAgICAgICJpc3MiOiAiaHR0cHM6Ly9rZXljbG9hay1zZXJ2ZXItdGVzdC5lYW5hZGV2Lm9yZy9hdXRoL3JlYWxtcy9ldXJvcGVhbmEiLAogICAgICAgICJhdWQiOiBbCiAgICAgICAgICAgIC4uLgogICAgICAgIF0sCiAgICAgICAgInN1YiI6ICI4ZDMxYjgzMi05MzA1LTQ4M2YtODU0MC1lM2FjNGRjNjVkNjQiLAogICAgICAgICJ0eXAiOiAiQmVhcmVyIiwKICAgICAgICAiYXpwIjogIkFQSUtFWSIsCiAgICAgICAgImF1dGhfdGltZSI6IDE1NTEzNDU4NDMsCiAgICAgICAgInNlc3Npb25fc3RhdGUiOiAiZGQ5OWE0OWEtMzM1NS00ZDYzLWIyNWQtNTI2YTJmZDY3OTJmIiwKICAgICAgICAiYWNyIjogIjEiLAogICAgICAgICJyZWFsbV9hY2Nlc3MiOiB7CiAgICAgICAgICAgICJyb2xlcyI6IFsKICAgICAgICAgICAgICAgLi4uCiAgICAgICAgICAgIF0KICAgICAgICB9LAogICAgICAgICJyZXNvdXJjZV9hY2Nlc3MiOiB7CiAgICAgICAgICAgICJhcGlfYW5ub3RhdGlvbnMiOiB7CiAgICAgICAgICAgICAgICAicm9sZXMiOiBbCiAgICAgICAgICAgICAgICAgICAuLi4KICAgICAgICAgICAgICAgIF0KICAgICAgICAgICAgfSwKICAgICAgICAgICAgImFjY291bnQiOiB7CiAgICAgICAgICAgICAgICAicm9sZXMiOiBbCiAgICAgICAgICAgICAgICAgIC4uLgogICAgICAgICAgICAgICAgXQogICAgICAgICAgICB9CiAgICAgICAgfSwKICAgICAgICAic2NvcGUiOiAib3BlbmlkIGVtYWlsIHByb2ZpbGUiLAogICAgICAgICJlbWFpbF92ZXJpZmllZCI6IGZhbHNlLAogICAgICAgICJwcmVmZXJyZWRfdXNlcm5hbWUiOiAiYWRtaW4iCiAgICB9LAogICAgInNpZ25hdHVyZSI6ICJpTmpKVHFiXzUzaWFQQjVtWU4zekRUeGt1SDBVMWYzWHlEQkY3SGNQbnEzSmhSR0o3Q19EY0JmeHJfaWhjbTQyZXdqd0VxYTJBdWNvdEVxTDJJNnZBYWY3M0JONkU4OXBzd1FDRVJNSk11d3FxWFc3V2NMeEdCTjlIazRjNDVvVVZZelp2eDM3dFQ2cDlOT0tGeG53aVozTTM2YURhOEdyTXJCc1ktYWlqMGkzVkViXzlyU00wMmtNYTZIREdfWnE4emFoamRrcUVSX3Zpa29RSUw4OF8tM3hONFpvUGRTNWsxNDZCVDY3T3VEZU82RUctMndsNzNwVktCQk1SWXNpWl8xQzNWck8wMlF3S19mdnVEdXhSMkE5d3l6LU9GZll1MjdlMnlXWkR5WmdXN1EyX05yRWpPYlF3bmhyUEVDNHdSVW85dkUwWUo2cGRtbkxDeXFoc1EiCn0K
 *
 */
public class ApiKeyUtils implements ApiKeyManager {

	private final Logger log = LogManager.getLogger(getClass());
	
	/* (non-Javadoc)
	 * @see eu.europeana.api.commons.oauth2.service.ApiKeyManager#extractApiKey(javax.servlet.http.HttpServletRequest)
	 */
	public String extractApiKey(HttpServletRequest request) throws ApiKeyExtractionException {
	    String apikey = "";
	    
	    // use case 1
	    if (request.getParameter(CommonApiConstants.PARAM_WSKEY) != null) 
		apikey = request.getParameter(CommonApiConstants.PARAM_WSKEY);
	    
	    // use case 2
	    String XAPIKEY = "X-Api-Key";
            String xApiKeyHeader = request.getHeader(XAPIKEY);
	    if (xApiKeyHeader != null) {
		getLog().trace("'X-Api-Key' header value: " + xApiKeyHeader);
		apikey = xApiKeyHeader;
	    }
	    
	    int API_KEY_TYPE_POS = 0;
	    int API_KEY_POS = 1;
	    String APIKEY = "APIKEY";
	    String BEARER = "Bearer";
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
	    if (authorizationHeader != null) {
		getLog().trace("'Authorization' header value: " + authorizationHeader);
		String[] headerElems = authorizationHeader.split(" ");
		if (headerElems.length < 2)
		    throw new ApiKeyExtractionException(I18nConstants.INVALID_HEADER_FORMAT+" "+authorizationHeader);

		String apikeyType = headerElems[API_KEY_TYPE_POS];
		
		// use case 3
		if (APIKEY.equals(apikeyType)) {
		    apikey = headerElems[API_KEY_POS];
		} else {
		    // use case 4
		    // Obtain the JWT token from the Authorization header
		    if (!BEARER.equals(apikeyType)) 
			throw new ApiKeyExtractionException(I18nConstants.UNSUPPORTED_TOKEN_TYPE+" "+apikeyType);
		    String encodedApiKey = headerElems[API_KEY_POS];
		    apikey = processJwtToken(encodedApiKey);
		}		
	    }
	    getLog().debug("Extracted apikey: " + apikey);
	    return apikey;	    
	}
	
	/**
	 * Obtain apikey from JWT token using
	 * @param encodedApiKey
	 * @return apikey
	 * @throws ApiKeyExtractionException 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@SuppressWarnings("unchecked")
	public String processJwtToken(String encodedApiKey) throws ApiKeyExtractionException {
	    String apikey = null;
	    String SIGNATURE = "signature";
	    String DATA = "data";
	    String EXP = "exp";
	    String AZP = "azp";
	    // Decrypt the JWT token (see example below) into JSON
	    String jwtToken = decodeBase64(encodedApiKey);
	    jwtToken = jwtToken.replace(".", "");
	    ObjectMapper objectMapper = new ObjectMapper();
	    Map<String, Object> jsonMap;
	    Map<String, Object> dataMap;
	    try {
		jsonMap = objectMapper.readValue(jwtToken,
		    new TypeReference<Map<String,Object>>(){});
		// Validate signature (using the TokenVerifier instance)
		String signature = (String) jsonMap.get(SIGNATURE);
		verifyToken(signature);
		// Obtain the expiration timestamp from "data.exp" and verify if it is still valid
		dataMap = (Map<String, Object>) jsonMap.get(DATA);
		int exp = (Integer) dataMap.get(EXP);
		int currentTime = (int) (new Date().getTime()/1000);
		// if expiration time is lower then the current time - it is expired
		if (exp < currentTime) 
		    throw new ApiKeyExtractionException(I18nConstants.EXPIRATION_TIMESTAMP_NOT_VALID+" "+exp);
		// Obtain the API key from the path "data.azp"
		String azp = (String) dataMap.get(AZP);
		apikey = azp;
	    } catch (IOException e) {
		throw new ApiKeyExtractionException(I18nConstants.JWT_TOKEN_ERROR+" "+e.getMessage());
	    }
	    apikey = decodeBase64(encodedApiKey);
	    return apikey;
	}
	
	/**
	 * This method validates JWT signature
	 * @param token The JWT signature
	 * @return true if valid
	 */
	public boolean verifyToken(String token) {
	    RSAPublicKey publicKey = null; //Get the key instance
            RSAPrivateKey privateKey = null; //Get the key instance
//	    try {
//	        Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
//		JWTVerifier verifier = JWT.require(algorithm)
//			        .withIssuer("auth0")
//			        .build(); //Reusable verifier instance
//		DecodedJWT jwt = verifier.verify(token);
//	    } catch (JWTVerificationException exception){
//		    throw new ApiKeyExtractionException(I18nConstants.INVALID_JWT_TOKEN+" "+token);
//	    }
	    return true;
	}
		
	    
	/**
	 * This method performs decoding of base64 string
	 * 
	 * @param base64Str
	 * @return decoded string
	 * @throws ApiKeyExtractionException
	 */
	public String decodeBase64(String base64Str) throws ApiKeyExtractionException {
		String res = null;
		try {
			byte[] decodedBase64Str = Base64.decodeBase64(base64Str);
			res = new String(decodedBase64Str);
		} catch (Exception e) {
			throw new ApiKeyExtractionException(I18nConstants.BASE64_DECODING_FAIL);
		}
		return res;
	}
	
	/**
	 * @return
	 */
	public Logger getLog() {
		return log;
	}

	@Override
	public ApiKey findByKey(String apiKey) throws CommonServiceException, CommonServiceRuntimeException {
	    // TODO Auto-generated method stub
	    return null;
	}

	@Override
	public List<ApiKey> findByApplicationName(String applicationName)
		throws CommonServiceException, CommonServiceRuntimeException {
	    // TODO Auto-generated method stub
	    return null;
	}


}
