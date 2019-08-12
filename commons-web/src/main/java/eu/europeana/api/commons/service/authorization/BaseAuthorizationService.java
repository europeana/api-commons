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
import eu.europeana.api.commons.exception.ApiKeyExtractionException;
import eu.europeana.api.commons.exception.AuthorizationExtractionException;
import eu.europeana.api.commons.oauth2.utils.OAuthUtils;
import eu.europeana.api.commons.web.exception.ApplicationAuthenticationException;
import eu.europeana.api.commons.web.model.vocabulary.UserRoles;

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
     * @see eu.europeana.api.commons.service.authorization.AuthorizationService#authorizeWriteAccess(java.util.List, java.lang.String, java.lang.String)
     */
    public boolean authorizeWriteAccess(List<? extends Authentication> authenticationList, String operation) 
	    throws ApplicationAuthenticationException {
	    
	return checkPermissions(authenticationList, getAuthorizationApiName(), operation);
    }
    
    /**
     * This method verifies write access rights for particular api and operation
     * @param authenticationList The list of authentications extracted from the JWT token
     * @param api The current api name
     * @param operation The name of current operation
     * @return true if authenticated, false otherwise
     * @throws ApplicationAuthenticationException
     */
    public static boolean checkPermissions(List<? extends Authentication> authenticationList, String api, String operation) 
	    throws ApplicationAuthenticationException {
	 boolean res = false;
	 
	 for(Authentication authentication : authenticationList) {
	     String authenticationApi = (String) authentication.getDetails();
	     @SuppressWarnings("unchecked")
	     List<GrantedAuthority> authorityList = (List<GrantedAuthority>) authentication.getAuthorities();
	     if(authenticationApi.equals(api)) {
		 for(GrantedAuthority authority : authorityList) {
		     String user = authority.getAuthority();
		     UserRoles userRole = UserRoles.valueOf(user.toUpperCase());
		     String[] supportedOperations = userRole.getOperations();
		     if(Arrays.asList(supportedOperations).contains(operation)) {
			 res = true;
			 break;
		     }
		 }
//	     } else {
//	         throw new ApplicationAuthenticationException(I18nConstants.INVALID_API_NAME, I18nConstants.INVALID_API_NAME, null);
	     }
	     
	     if(res) {
		 break;
	     }
	 }
	 
	 return res;
    }    
    
    /* (non-Javadoc)
     * @see eu.europeana.api.commons.service.authorization.AuthorizationService#processJwtToken(javax.servlet.http.HttpServletRequest)
     */
    public List<? extends Authentication> processJwtToken(HttpServletRequest request) throws ApplicationAuthenticationException, ApiKeyExtractionException, AuthorizationExtractionException {
	return OAuthUtils.processJwtToken(request, getSignatureVerifier());
    }    
}
