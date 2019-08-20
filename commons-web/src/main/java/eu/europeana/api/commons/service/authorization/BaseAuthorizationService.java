package eu.europeana.api.commons.service.authorization;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.ClientDetailsService;

import eu.europeana.api.commons.definitions.config.i18n.I18nConstants;
import eu.europeana.api.commons.definitions.vocabulary.Role;
import eu.europeana.api.commons.exception.ApiKeyExtractionException;
import eu.europeana.api.commons.exception.AuthorizationExtractionException;
import eu.europeana.api.commons.oauth2.utils.OAuthUtils;
import eu.europeana.api.commons.web.exception.ApplicationAuthenticationException;

public abstract class BaseAuthorizationService implements AuthorizationService {

    RsaVerifier signatureVerifier;

    protected RsaVerifier getSignatureVerifier() {
	if (signatureVerifier == null)
	    signatureVerifier = new RsaVerifier(getSignatureKey());
	return signatureVerifier;
    }

    @Override
    public void authorizeReadAccess(HttpServletRequest request) throws ApplicationAuthenticationException {

	String wsKey;
	// check and verify jwt token
	try {
	    wsKey = OAuthUtils.extractApiKeyFromJwtToken(request, getSignatureVerifier());
	    if (wsKey != null) {
		return;// apikey is valid if the JWT Token is verified
	    }
	} catch (ApiKeyExtractionException e) {
	    throw new ApplicationAuthenticationException(I18nConstants.INVALID_APIKEY, I18nConstants.INVALID_APIKEY,
		    new String[] { e.getMessage() }, HttpStatus.UNAUTHORIZED, e);
	}

	// extract api key with other methods
	try {
	    wsKey = OAuthUtils.extractApiKey(request);
	} catch (ApiKeyExtractionException e) {
	    throw new ApplicationAuthenticationException(I18nConstants.INVALID_APIKEY, I18nConstants.INVALID_APIKEY,
		    new String[] { e.getMessage() }, HttpStatus.UNAUTHORIZED, e);
	}

	// check if null
	if (wsKey == null)
	    throw new ApplicationAuthenticationException(I18nConstants.MISSING_APIKEY, I18nConstants.MISSING_APIKEY,
		    null, HttpStatus.UNAUTHORIZED, null);
	// check if empty
	if (StringUtils.isEmpty(wsKey))
	    throw new ApplicationAuthenticationException(I18nConstants.EMPTY_APIKEY, I18nConstants.EMPTY_APIKEY, null);
	// validate api key

	try {
	    getClientDetailsService().loadClientByClientId(wsKey);
	} catch (Exception e) {
	    throw new ApplicationAuthenticationException(I18nConstants.INVALID_APIKEY, I18nConstants.INVALID_APIKEY,
		    new String[] { wsKey }, HttpStatus.UNAUTHORIZED, e);
	}
    }

    
    /*
     * (non-Javadoc)
     * 
     * @see eu.europeana.api.commons.service.authorization.AuthorizationService#
     * authorizeWriteAccess(javax.servlet.http.HttpServletRequest, java.lang.String)
     */
    public void authorizeWriteAccess(HttpServletRequest request, String operation)
	    throws ApplicationAuthenticationException, ApiKeyExtractionException, AuthorizationExtractionException {

	List<? extends Authentication> authenticationList = OAuthUtils.processJwtToken(request, getSignatureVerifier());

	checkPermissions(authenticationList, getApiName(), operation);
    }

    /**
     * This method verifies write access rights for particular api and operation
     * 
     * @param authenticationList The list of authentications extracted from the JWT
     *                           token
     * @param api                The name of the called api 
     * @param operation          The name of called api operation
     * @throws ApplicationAuthenticationException if the access to the api operation is not authorized
     */
    @SuppressWarnings("unchecked")
    protected void checkPermissions(List<? extends Authentication> authenticationList, String api, String operation) throws ApplicationAuthenticationException {

	List<GrantedAuthority> authorityList;

	for (Authentication authentication : authenticationList) {

	    authorityList = (List<GrantedAuthority>) authentication.getAuthorities();

	    if (api.equals((String) authentication.getDetails()) && isOperationAuthorized(operation, authorityList)) {
		// access granted
		return;
	    }
	}

	// not authorized
	throw new ApplicationAuthenticationException(I18nConstants.OPERATION_NOT_AUTHORIZED,
		I18nConstants.OPERATION_NOT_AUTHORIZED, null);
    }

    /**
     * This method performs checking, whether an operation is supported for provided
     * authorities
     * 
     * @param operation the called api operation
     * @param authorityList the list of granted authorities (as provided through the JWT token)
     * @return true if operation authorized
     */
    private boolean isOperationAuthorized(String operation, List<GrantedAuthority> authorities) {
	
	Role userRole;
	List<String> allowedOperations;

	for (GrantedAuthority authority : authorities) {
	    userRole = getRoleByName(authority.getAuthority());
	    if(userRole == null) {
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
