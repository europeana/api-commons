package eu.europeana.api.commons.oauth2.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;

import eu.europeana.api.commons.definitions.vocabulary.CommonApiConstants;
import eu.europeana.api.commons.exception.ApiKeyExtractionException;
import eu.europeana.api.commons.exception.AuthorizationExtractionException;
import eu.europeana.api.commons.oauth2.model.impl.EuropeanaApiCredentials;
import eu.europeana.api.commons.oauth2.model.impl.EuropeanaAuthenticationToken;

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
    // apikey
    public static final String AZP = "azp";
    public static final String EXP = "exp";
    // api
    public static final String AUD = "aud";
    // api
    public static final String SCOPE = "scope";
    public static final String RESOURCE_ACCESS = "resource_access";
    public static final String ROLES = "roles";
    // user name
    public static final String PREFERRED_USERNAME = "preferred_username";
    // user id
    public static final String USER_ID = "sub";

    static JsonParser objectMapper = JsonParserFactory.create();

    /**
     * This method extracts apikey from a HTTP request using one of the following
     * methods: 1. By means of a query parameter 2. By means of the "X-Api-Key"
     * header 3. By means of a specialization of the "Authorization" header
     *
     * @param request API request that is expected to contain an apikey submitted in
     *                one of the above mentioned ways
     * @return The extracted apikey, or null if not found in the request object
     * @throws ApiKeyExtractionException        if the authorization header doesn't
     *                                          have one of the supported types
     * @throws AuthorizationExtractionException
     * 
     * @see #extractPayloadFromAuthorizationHeader(HttpServletRequest, String)
     */
    public static String extractApiKey(HttpServletRequest request)
	    throws ApiKeyExtractionException, AuthorizationExtractionException {
	String wskeyParam = request.getParameter(CommonApiConstants.PARAM_WSKEY);
	// use case 1
	if (wskeyParam != null)
	    return wskeyParam;

	String xApiKeyHeader = request.getHeader(HEADER_XAPIKEY);
	if (xApiKeyHeader != null)
	    return xApiKeyHeader;

	String apikey = extractPayloadFromAuthorizationHeader(request, TYPE_APIKEY);
	if (apikey == null) {
	    throw new ApiKeyExtractionException("No APIKey provided within the request or authorization header!");
	}

	return apikey;
    }

    /**
     * This method adopts KeyCloack token from HTTP request
     * 
     * @param request           The HTTP request
     * @param signatureVerifier the signature verifier for JWT token
     * @return a list of Authentication objects
     * @throws ApiKeyExtractionException        if the API key cannot be
     *                                          successfully extracted from re quest
     * @throws AuthorizationExtractionException if the Authorization header cannot
     *                                          be successfully extracted from
     *                                          request
     */
    public static List<? extends Authentication> processJwtToken(HttpServletRequest request,
	    RsaVerifier signatureVerifier, String api)
	    throws ApiKeyExtractionException, AuthorizationExtractionException {

	String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

	return extractAuthenticationList(authorization, signatureVerifier, api);
    }

    public static List<? extends Authentication> extractAuthenticationList(String authorization,
	    RsaVerifier signatureVerifier, String api)
	    throws ApiKeyExtractionException, AuthorizationExtractionException {
	List<Authentication> authenticationList = new ArrayList<Authentication>();

	String encodedToken = extractPayloadFromHeaderValue(TYPE_BEARER, authorization);

	// if authorization header or JWT token not present in request return null
	if (encodedToken == null)
	    return null;

	try {
	    Map<String, Object> data = extractCustomData(encodedToken, signatureVerifier, api);
	    processResourceAccessClaims(api, data, authenticationList);

	} catch (RuntimeException e) {
	    throw new AuthorizationExtractionException(
		    "Unexpected exception occured when processing JWT Token for authorization", e);
	}

	return authenticationList;
    }

    @SuppressWarnings("unchecked")
    public static void processResourceAccessClaims(String api, Map<String, Object> data,
	    List<Authentication> authenticationList) throws ApiKeyExtractionException {
	// verify scope, aud and resource access
	if (!verifyScope(api, data)) {
	    // token not intended to have write access to current api
	    return;
	}

	if (!verifyAudience(api, data)) {
	    // token not intended to have write access to current api
	    return;
	}

	if (!data.containsKey(RESOURCE_ACCESS)) {
	    // token for read only access
	    return;
	}

	Map<String, Object> resourceAccessMap = (Map<String, Object>) data.get(RESOURCE_ACCESS);
	String principal = (String) data.get(USER_ID);
	String userName = (String) data.get(PREFERRED_USERNAME);

	// each API in resource_access should be processed and
	// EuropeanaAuthenticationToken will be created for the current API
	EuropeanaAuthenticationToken authenticationToken;
	Collection<GrantedAuthority> authorities;
	String details;
	Map<String, Object> rolesMap;
	List<String> roles;

	for (Map.Entry<String, Object> entry : resourceAccessMap.entrySet()) {
	    details = entry.getKey();
	    if (!api.equals(details)) {
		// process only authentication for this API
		continue;
	    }

	    authorities = new ArrayList<GrantedAuthority>();

	    rolesMap = (Map<String, Object>) entry.getValue();
	    roles = (List<String>) rolesMap.get(ROLES);
	    for (String role : roles) {
		authorities.add(new SimpleGrantedAuthority(role));
	    }

	    authenticationToken = new EuropeanaAuthenticationToken(authorities, details, principal,
		    new EuropeanaApiCredentials(userName));
	    authenticationList.add(authenticationToken);
	}
    }

    /**
     * Extracts the payload of the Authorization header
     * 
     * @param request
     * @param authorizationType expected type for the authorization header (APIKEY
     *                          or Bearer)
     * @return the payload of authorization header
     * @throws ApiKeyExtractionException        if the type of the Authorization
     *                                          header is not supported.
     * @throws AuthorizationExtractionException
     */
    private static String extractPayloadFromAuthorizationHeader(HttpServletRequest request, String authorizationType)
	    throws ApiKeyExtractionException, AuthorizationExtractionException {
	String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
	return extractPayloadFromHeaderValue(authorizationType, authorization);
    }

    private static String extractPayloadFromHeaderValue(String authorizationType, String authorization)
	    throws AuthorizationExtractionException, ApiKeyExtractionException {
	// if authorization not present return null
	if (authorization == null) {
	    throw new AuthorizationExtractionException(
		    "No authentication information provided, Authorization header not submitted with the request! ");
	}

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
     * @param api               the api for which access is requested
     * @return value of API key
     * @throws ApiKeyExtractionException        if the token cannot be parsed or it
     *                                          is expired
     * @throws AuthorizationExtractionException if the subject is not defined in the
     *                                          token
     */
    public static Map<String, Object> extractCustomData(HttpServletRequest request, RsaVerifier signatureVerifier,
	    String api) throws ApiKeyExtractionException, AuthorizationExtractionException {
	String encodedToken = extractPayloadFromAuthorizationHeader(request, TYPE_BEARER);
	// if authorization header or JWT token not present in request return null
	if (encodedToken == null)
	    return null;
	// use case 4
	// Obtain the JWT token from the Authorization header
	return extractCustomData(encodedToken, signatureVerifier, api);
    }

    public static String extractApiKeyFromJwtToken(HttpServletRequest request, RsaVerifier signatureVerifier,
	    String api) throws ApiKeyExtractionException, AuthorizationExtractionException {
	Map<String, Object> data = extractCustomData(request, signatureVerifier, api);
	if (data == null)
	    return null;
	// use case 4
	// Obtain the JWT token from the Authorization header
	return extractApiKey(data);
    }

    /**
     * This method extracts custom data from encoded JWT token, verifying it with
     * the provided key
     * 
     * @param encodedToken      The JWT token
     * @param signatureVerifier The key to verify token
     * @param api               the api for which access is requested
     * @return map of the custom data
     * @throws ApiKeyExtractionException
     */
    private static Map<String, Object> extractCustomData(String encodedToken, RsaVerifier signatureVerifier, String api)
	    throws ApiKeyExtractionException, AuthorizationExtractionException {
	Map<String, Object> data = null;
	try {
	    Jwt token = JwtHelper.decodeAndVerify(encodedToken, signatureVerifier);
	    data = objectMapper.parseMap(token.getClaims());
	    verifyTokenExpiration(data);
	    verifySubject(data);
	    verifyUserName(data);
	} catch (RuntimeException e) {
	    throw new ApiKeyExtractionException("Unexpected exception occured when processing JWT Token", e);
	}
	return data;
    }

    /**
     * @param data
     * @return
     * @throws ApiKeyExtractionException
     */
    public static String extractApiKey(Map<String, Object> data) throws ApiKeyExtractionException {
	String apikey = (String) data.get(AZP);
	if (apikey == null || StringUtils.isEmpty(apikey))
	    throw new ApiKeyExtractionException("API KEY not available in provided JWT token");
	return apikey;
    }

    /**
     * @param api  the api for which access is requested
     * @param data custom data in the JWT token
     * @throws ApiKeyExtractionException if the audience is not correct or not the
     *                                   intended one for the given api
     */
    @SuppressWarnings("rawtypes")
    private static boolean verifyAudience(String api, Map<String, Object> data) throws ApiKeyExtractionException {

	if (!data.containsKey(AUD)) {
	    // read only token
	    return false;
	}

	Object aud = data.get(AUD);
	if (aud instanceof String) {
	    // single value audience
	    return aud.equals(api);
	} else if (aud instanceof String[]) {
	    // multiple values audience
	    Stream<String> values = Arrays.stream((String[]) aud);
	    return values.anyMatch(api::equals);
	} else if (aud instanceof List) {
	    // multiple values audience
	    return ((List) aud).contains(api);
	} else {
	    // should not happen, but who knows
	    throw new ApiKeyExtractionException(
		    "Invalid JWT token. Audience is not propertly formated. It must be a string or a string array: "
			    + aud);
	}
    }

    /**
     * @param api  the name of the api to which read access is requested
     * @param data custom data in the JWT token
     * @return
     * @throws ApiKeyExtractionException if the scope field is not present in the
     *                                   token
     */
    private static boolean verifyScope(String api, Map<String, Object> data) throws ApiKeyExtractionException {

	if (!data.containsKey(SCOPE)) {
	    // read only token
	    return false;
	}

	String scope = (String) data.get(SCOPE);
	String[] scopes = StringUtils.splitByWholeSeparator(scope, null);
	Stream<String> values = Arrays.stream(scopes);
	return values.anyMatch(api::equals);
    }

    /**
     * @param data
     * @throws ApiKeyExtractionException
     */
    private static void verifyTokenExpiration(Map<String, Object> data) throws ApiKeyExtractionException {
	int exp = (Integer) data.get(EXP);
	int currentTime = (int) (System.currentTimeMillis() / 1000);
	if (exp < currentTime)
	    throw new ApiKeyExtractionException(
		    "Expired JWT token. Please refresh the token. Expiration time:  " + exp);
    }

    private static void verifySubject(Map<String, Object> data) throws AuthorizationExtractionException {
	// verify subject (user id)
	if (!data.containsKey(USER_ID)) {
	    throw new AuthorizationExtractionException("User id not available in provided JWT token");
	}
    }

    private static void verifyUserName(Map<String, Object> data) throws AuthorizationExtractionException {
	// verify subject (user id)
	if (!data.containsKey(PREFERRED_USERNAME)) {
	    throw new AuthorizationExtractionException("Preffered User Name not available in provided JWT token");
	}
    }

}
