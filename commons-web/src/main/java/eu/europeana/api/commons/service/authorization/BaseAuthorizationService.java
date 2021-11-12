package eu.europeana.api.commons.service.authorization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

import eu.europeana.api.commons.definitions.config.i18n.I18nConstants;
import eu.europeana.api.commons.definitions.vocabulary.Role;
import eu.europeana.api.commons.exception.ApiKeyExtractionException;
import eu.europeana.api.commons.exception.AuthorizationExtractionException;
import eu.europeana.api.commons.oauth2.model.impl.EuropeanaApiCredentials;
import eu.europeana.api.commons.oauth2.model.impl.EuropeanaAuthenticationToken;
import eu.europeana.api.commons.oauth2.utils.OAuthUtils;
import eu.europeana.api.commons.web.exception.ApplicationAuthenticationException;

public abstract class BaseAuthorizationService implements AuthorizationService {

    RsaVerifier signatureVerifier;
    private Logger log = LogManager.getLogger(getClass());

    public Logger getLog() {
	return log;
    }

    protected RsaVerifier getSignatureVerifier() {
	if (signatureVerifier == null)
	    signatureVerifier = new RsaVerifier(getSignatureKey());
	return signatureVerifier;
    }

    @Override
    /**
     * 
     */
    public Authentication authorizeReadAccess(HttpServletRequest request) throws ApplicationAuthenticationException {
	Authentication authentication = null;
	// check and verify jwt token
	String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
	if (authorization != null && authorization.startsWith(OAuthUtils.TYPE_BEARER)) {
	    //if jwt token submitted
	    authentication = authorizeReadByJwtToken(request);
	}else {
	    //user id not available, verify apiKey only
	    authentication = authorizeReadByApiKey(request);    
	}
	return authentication;
    }

    private Authentication authorizeReadByApiKey(HttpServletRequest request) throws ApplicationAuthenticationException {
	String wsKey;
	// extract api key with other methods
	try{
	    wsKey = OAuthUtils.extractApiKey(request);
	} catch (ApiKeyExtractionException | AuthorizationExtractionException e) {
	    throw new ApplicationAuthenticationException(I18nConstants.INVALID_APIKEY, I18nConstants.INVALID_APIKEY,
		    new String[] { e.getMessage() }, HttpStatus.UNAUTHORIZED, e);
	}

	// check if empty
	if (StringUtils.isEmpty(wsKey))
	    throw new ApplicationAuthenticationException(I18nConstants.EMPTY_APIKEY, I18nConstants.EMPTY_APIKEY, null);
	
	// validate api key
	try {
	    getClientDetailsService().loadClientByClientId(wsKey);
	} catch (ClientRegistrationException e) {
	    // invalid api key
	    throw new ApplicationAuthenticationException(I18nConstants.INVALID_APIKEY, I18nConstants.INVALID_APIKEY,
		    new String[] { wsKey }, HttpStatus.UNAUTHORIZED, e);
	} catch (OAuth2Exception e) {
	    // validation failed through API Key service issues
	    // silently approve request
	    getLog().info("Invocation of API Key Service failed. Silently approve apikey: " + wsKey, e);
	}
	
	return new EuropeanaAuthenticationToken(null, getApiName(), wsKey, 
		new EuropeanaApiCredentials(EuropeanaApiCredentials.USER_ANONYMOUS));
    }

    private Authentication authorizeReadByJwtToken(HttpServletRequest request)
	    throws ApplicationAuthenticationException {
	Authentication authentication = null;
	try {
//		String jwtToken = OAuthUtils.extractPayloadFromAuthorizationHeader(request, OAuthUtils.);
	    Map<String, Object> data = OAuthUtils.extractCustomData(request, getSignatureVerifier(), getApiName());
	    String wsKey = OAuthUtils.extractApiKey(data);

	    // check if null
	    if (wsKey == null)
		throw new ApplicationAuthenticationException(I18nConstants.MISSING_APIKEY, I18nConstants.MISSING_APIKEY,
			null, HttpStatus.UNAUTHORIZED, null);

	    if (data.containsKey(OAuthUtils.USER_ID)) {
		List<Authentication> authList = new ArrayList<Authentication>(); 
		OAuthUtils.processResourceAccessClaims(getApiName(), data, authList);
		if(!authList.isEmpty()) {
		    authentication = authList.get(0);
		}else {
		    ////return Authentication object for read only user
		    String userName = (String) data.get(OAuthUtils.PREFERRED_USERNAME);
		    if(userName == null) {
			userName = EuropeanaApiCredentials.USER_ANONYMOUS;
		    }
		    authentication = new EuropeanaAuthenticationToken(null, getApiName(),
				(String) data.get(OAuthUtils.USER_ID),
				new EuropeanaApiCredentials(userName));
		    
		}
	    }
	} catch (Exception e) {
	    throw new ApplicationAuthenticationException(I18nConstants.JWT_TOKEN_ERROR, I18nConstants.JWT_TOKEN_ERROR,
			    new String[] { e.getMessage() }, HttpStatus.UNAUTHORIZED, e);
	}

	return authentication;
    }

    /*
     * (non-Javadoc)
     * 
     * @see eu.europeana.api.commons.service.authorization.AuthorizationService#
     * authorizeWriteAccess(javax.servlet.http.HttpServletRequest, java.lang.String)
     */
    public Authentication authorizeWriteAccess(HttpServletRequest request, String operation)
	    throws ApplicationAuthenticationException {

	return authorizeOperation(request, operation);
    }

    private Authentication authorizeOperation(HttpServletRequest request, String operation)
	    throws ApplicationAuthenticationException {
	if (getSignatureVerifier() == null) {
	    throw new ApplicationAuthenticationException(I18nConstants.OPERATION_NOT_AUTHORIZED,
		    I18nConstants.OPERATION_NOT_AUTHORIZED,
		    new String[] { "No signature key configured for verification of JWT Token" },
		    HttpStatus.INTERNAL_SERVER_ERROR);
	}

	List<? extends Authentication> authenticationList;
	try {
	    authenticationList = OAuthUtils.processJwtToken(request, getSignatureVerifier(), getApiName());
	} catch (ApiKeyExtractionException | AuthorizationExtractionException e) {
	    throw new ApplicationAuthenticationException(I18nConstants.OPERATION_NOT_AUTHORIZED,
		    I18nConstants.OPERATION_NOT_AUTHORIZED, new String[] { "Invalid token or ApiKey" },
		    HttpStatus.UNAUTHORIZED, e);
	}

	return checkPermissions(authenticationList, getApiName(), operation);
    }

    /**
     * This method verifies write access rights for particular api and operation
     * 
     * @param authenticationList The list of authentications extracted from the JWT
     *                           token
     * @param api                The name of the called api
     * @param operation          The name of called api operation
     * @throws ApplicationAuthenticationException if the access to the api operation
     *                                            is not authorized
     */
    @SuppressWarnings("unchecked")
    protected Authentication checkPermissions(List<? extends Authentication> authenticationList, String api,
	    String operation) throws ApplicationAuthenticationException {

	if (authenticationList == null || authenticationList.isEmpty()) {
	    throw new ApplicationAuthenticationException(I18nConstants.OPERATION_NOT_AUTHORIZED,
		    I18nConstants.OPERATION_NOT_AUTHORIZED, new String[] { "No or invalid authorization provided" },
		    HttpStatus.FORBIDDEN);
	}

	List<GrantedAuthority> authorityList;

	for (Authentication authentication : authenticationList) {

	    authorityList = (List<GrantedAuthority>) authentication.getAuthorities();

	    if (api.equals(authentication.getDetails()) && isOperationAuthorized(operation, authorityList)) {
		// access granted
		return authentication;
	    }
	}

	// not authorized
	throw new ApplicationAuthenticationException(I18nConstants.OPERATION_NOT_AUTHORIZED,
		I18nConstants.OPERATION_NOT_AUTHORIZED,
		new String[] { "Operation not permitted or not GrantedAuthority found for operation:" + operation },
		HttpStatus.FORBIDDEN);
    }

    /**
     * This method performs checking, whether an operation is supported for provided
     * authorities
     * 
     * @param operation     the called api operation
     * @param authorityList the list of granted authorities (as provided through the
     *                      JWT token)
     * @return true if operation authorized
     */
    private boolean isOperationAuthorized(String operation, List<GrantedAuthority> authorities) {

	Role userRole;
	List<String> allowedOperations;

	for (GrantedAuthority authority : authorities) {
	    userRole = getRoleByName(authority.getAuthority());
	    if (userRole == null) {
		continue;
	    }
	    allowedOperations = Arrays.asList(userRole.getPermissions());
	    if (allowedOperations.contains(operation)) {
		return true;
	    }
	}

	return false;
    }

    /**
     * This method returns the api specific Role for the given role name
     * 
     * @param name the name of user role
     * @return the user role
     */
    protected abstract Role getRoleByName(String name);

    /**
     * 
     * @return key used to verify the JWT token signature
     */
    protected abstract String getSignatureKey();

    /**
     * 
     * @return the service providing the client details
     */
    protected abstract ClientDetailsService getClientDetailsService();

    /**
     * 
     * @return the name of the API calling the authorization service
     */
    protected abstract String getApiName();

}
