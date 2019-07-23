package eu.europeana.api.commons.oauth2.utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;

import eu.europeana.api.commons.definitions.vocabulary.CommonApiConstants;
import eu.europeana.api.commons.exception.ApiKeyExtractionException;

/**
 * This class supports apikey extraction.
 * 
 * @author GrafR
 * 
 */
public class OAuthUtils {

    public static final String HEADER_XAPIKEY = "X-Api-Key";
    public static final String TYPE_APIKEY = "APIKEY";
    public static final String TYPE_BEARER = "Bearer";
    public static final String AZP = "azp";
    public static final String EXP = "exp";

    static JsonParser objectMapper = JsonParserFactory.create();

    /**
     * This method extracts apikey from a HTTP request using one of the following
     * methods: 1. By means of a query parameter 2. By means of the "X-Api-Key"
     * header 3. By means of a specialization of the "Authorization" header
     *
     * @param request API request that is expected to contain an apikey submitted in one of the above mentioned ways
     * @return The extracted apikey, or null if not found in the request object
     * @throws ApiKeyExtractionException if the authorization header doesn't have one of the supported types
     * 
     * @see #extractPayloadFromAuthorizationHeader(HttpServletRequest, String)
     */
    public static String extractApiKey(HttpServletRequest request) throws ApiKeyExtractionException {
	String wskeyParam = request.getParameter(CommonApiConstants.PARAM_WSKEY);
	// use case 1
	if (wskeyParam != null)
	    return wskeyParam;

	String xApiKeyHeader = request.getHeader(HEADER_XAPIKEY);
	if (xApiKeyHeader != null)
	    return xApiKeyHeader;

	return extractPayloadFromAuthorizationHeader(request, TYPE_APIKEY);
    }

    /**
     * Extracts the payload of the Authorization header
     * 
     * @param request
     * @param authorizationType expected type for the authorization header (APIKEY
     *                          or Bearer)
     * @return the payload of authorization header
     * @throws ApiKeyExtractionException if the type of the Authorization header is
     *                                   not supported.
     */
    private static String extractPayloadFromAuthorizationHeader(HttpServletRequest request, String authorizationType)
	    throws ApiKeyExtractionException {
	String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
	// if authorization not present return null
	if (authorization == null)
	    return null;

	// validate header format first
	if (!authorization.startsWith(TYPE_BEARER) && !authorization.startsWith(TYPE_APIKEY))
	    throw new ApiKeyExtractionException("Unsupported type in Auhtorization header: " + authorization);

	if (authorization.startsWith(authorizationType))
	    return authorization.substring(authorizationType.length()).trim();

	return null;
    }

    /**
     * This method extracts apikey from the JWT token if available in the
     * Authorization Header of the HTTP request (4. By means of a JWT token if it
     * exists.)
     * 
     * @param request           the API Request
     * @param signatureVerifier RsaVerifier initialized with the public key used to
     *                          verify the token signature
     * @return value of API key
     * @throws ApiKeyExtractionException if the token cannot be parsed or it is expired 
     */
    public static String extractApiKeyFromJwtToken(HttpServletRequest request, RsaVerifier signatureVerifier)
	    throws ApiKeyExtractionException {
	String jwtToken = extractPayloadFromAuthorizationHeader(request, TYPE_BEARER);
	// if authorization header or JWT token not present in request return null
	if (jwtToken == null)
	    return null;
	// use case 4
	// Obtain the JWT token from the Authorization header
	return processJwtToken(jwtToken, signatureVerifier);
    }

    /**
     * Obtain apikey from JWT token using
     * 
     * @param encodedToken the JWT token as string 
     * @param signatureVerifier RsaVerifier initialized with the public key used to
     *                          verify the token signature
     * @return the extracted apikey 
     * @throws ApiKeyExtractionException if the token is expired or the apikey is not found in the token
     * 
     */
    protected static String processJwtToken(String encodedToken, RsaVerifier signatureVerifier)
	    throws ApiKeyExtractionException {

	try {
	    Jwt token = JwtHelper.decodeAndVerify(encodedToken, signatureVerifier);
	    Map<String, Object> data = objectMapper.parseMap(token.getClaims());
	    verifyTokenExpiration(data);

	    return extractApiKey(data);
	} catch (RuntimeException e) {
	    throw new ApiKeyExtractionException("Unexpected exception occured when processing JWT Token", e);
	}
    }

    private static String extractApiKey(Map<String, Object> data) throws ApiKeyExtractionException {
	String apikey = (String) data.get(AZP);
	if (apikey == null || StringUtils.isEmpty(apikey))
	    throw new ApiKeyExtractionException("API KEY not available in provided JWT token");
	return apikey;
    }

    private static void verifyTokenExpiration(Map<String, Object> data) throws ApiKeyExtractionException {
	int exp = (Integer) data.get(EXP);
	int currentTime = (int) (System.currentTimeMillis() / 1000);
	if (exp < currentTime)
	    throw new ApiKeyExtractionException(
		    "Expired JWT token. Please refresh the token. Expiration time:  " + exp);
    }

}
