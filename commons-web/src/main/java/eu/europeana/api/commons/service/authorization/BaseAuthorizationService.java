package eu.europeana.api.commons.service.authorization;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.ClientDetailsService;

import eu.europeana.api.commons.definitions.config.i18n.I18nConstants;
import eu.europeana.api.commons.exception.ApiKeyExtractionException;
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

}
