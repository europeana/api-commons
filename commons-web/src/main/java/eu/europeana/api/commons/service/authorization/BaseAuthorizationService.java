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
        if(signatureVerifier == null)
            signatureVerifier = new RsaVerifier(getSignatureKey()); 
	return signatureVerifier;
    }


    @Override
    public void authorizeReadAccess(HttpServletRequest request) throws ApplicationAuthenticationException {
    
	String wsKey; 
	//check and verify jwt token
	try {
	    wsKey = OAuthUtils.extractApiKeyFromJwtToken(request, getSignatureVerifier());
	    if(wsKey != null) {
        	return;//apikey is valid if the JWT Token is verified
	    }
	} catch (ApiKeyExtractionException e) {
	    throw new ApplicationAuthenticationException(I18nConstants.INVALID_APIKEY, I18nConstants.INVALID_APIKEY,
		    new String[] { e.getMessage() }, HttpStatus.UNAUTHORIZED, e);
	}
	
	//extract api key with other methods
	try {
		wsKey = OAuthUtils.extractApiKey(request);
	} catch (ApiKeyExtractionException e) {
	    throw new ApplicationAuthenticationException(I18nConstants.INVALID_APIKEY, I18nConstants.INVALID_APIKEY,
		    new String[] { e.getMessage() }, HttpStatus.UNAUTHORIZED, e);
	}
	
	//check if null
	if (wsKey == null)
	    throw new ApplicationAuthenticationException(I18nConstants.MISSING_APIKEY, I18nConstants.MISSING_APIKEY,
		    null, HttpStatus.UNAUTHORIZED, null);
	//check if empty
	if (StringUtils.isEmpty(wsKey))
	    throw new ApplicationAuthenticationException(I18nConstants.EMPTY_APIKEY, I18nConstants.EMPTY_APIKEY, null);
	//validate api key

	try {
	    getClientDetailsService().loadClientByClientId(wsKey);
	} catch (Exception e) {
	    throw new ApplicationAuthenticationException(I18nConstants.INVALID_APIKEY, I18nConstants.INVALID_APIKEY,
		    new String[] { wsKey }, HttpStatus.UNAUTHORIZED, e);
	}	
    }
    
    
    protected abstract String getSignatureKey();

    protected abstract ClientDetailsService getClientDetailsService();
    
    protected abstract String getAuthorizationApiName();

    
    /* (non-Javadoc)
     * @see eu.europeana.api.commons.service.authorization.AuthorizationService#authorizeWriteAccess(javax.servlet.http.HttpServletRequest, java.lang.String)
     */
    public void authorizeWriteAccess(HttpServletRequest request, String operation, Role[] userRoles) 
	    throws ApplicationAuthenticationException, ApiKeyExtractionException, AuthorizationExtractionException {
	List<? extends Authentication> authenticationList = OAuthUtils.processJwtToken(request, getSignatureVerifier());
	    
	checkPermissions(authenticationList, getAuthorizationApiName(), operation, userRoles);
    }
    
    /**
     * This method verifies write access rights for particular api and operation
     * @param authenticationList The list of authentications extracted from the JWT token
     * @param api The current api name
     * @param operation The name of current operation
     * @param userRoles
     * @return true if authenticated, false otherwise
     * @throws ApplicationAuthenticationException
     */
    @SuppressWarnings("unchecked")
    protected void checkPermissions(List<? extends Authentication> authenticationList, 
	    String api, String operation, Role[] userRoles) 
	    throws ApplicationAuthenticationException {
	
	List<GrantedAuthority> authorityList;
	
	for (Authentication authentication : authenticationList) {
	    
	    authorityList = (List<GrantedAuthority>) authentication.getAuthorities();
	    
	    if (api.equals((String) authentication.getDetails()) 
		    && isOperationAuthorized(operation, authorityList, userRoles)) {
		// access granted
		return;
	    }
	}

	// not authorized
	throw new ApplicationAuthenticationException(I18nConstants.OPERATION_NOT_AUTHORIZED,
		I18nConstants.OPERATION_NOT_AUTHORIZED, null);    
    }


    /**
     * This method performs checking, whether an operation is supported for provided authorities
     * @param operation
     * @param authorityList
     * @param userRoles
     * @return true if operation supported
     */
    private boolean isOperationAuthorized(String operation, List<GrantedAuthority> authorityList, Role[] userRoles) {
	String user;
	Role userRole;
	String[] supportedOperations;
	for(GrantedAuthority authority : authorityList) {
	     user = authority.getAuthority();
	     userRole = getRoleByName(userRoles, user.toUpperCase());
	     supportedOperations = userRole.getPermissions();
	     if(Arrays.asList(supportedOperations).contains(operation)) {
		 return true;
	     }
	 }
	return false;
    }    
    
    /**
     * This method selects user role from all roles by provided role name
     * @param userRoles The list of roles
     * @param name The name of required role
     * @return the user role
     */
    private Role getRoleByName(Role[] userRoles, String name) {
	for(Role role : userRoles) {
	    if(role.getName().equals(name)) {
		return role;
	    }
	}
	return null;
    }
    
}
