package eu.europeana.api.commons.oauth2.model.impl;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import eu.europeana.api.commons.oauth2.model.ApiCredentials;

/**
 * This class maps contents of JwtToken
 * 
 * in granted authorities we store roles 
 * in details we store the api for which the resource_access is requested (i.e. api_annotations, api_entity)
 * 
 * @author GrafR
 *
 */
public class EuropeanaAuthenticationToken extends AbstractAuthenticationToken {

    /**
     * 
     */
    private static final long serialVersionUID = -3956175951431237501L;
    String principal = null;
    ApiCredentials credentials;
    
    public static final String DEFAULT_ROLE_USER = "user";
    
//    public EuropeanaAuthenticationToken(Collection<? extends GrantedAuthority> grantedAuthorities) {
//	super(grantedAuthorities);
//    }

    /**
     * This constructor supports also details
     * @param grantedAuthorities the authorities holding granted access permissions
     * @param api the API for which access is requested
     * @param principal the username 
     */
    public EuropeanaAuthenticationToken(Collection<? extends GrantedAuthority> grantedAuthorities, String api, String principal, ApiCredentials credentials) {
	super(grantedAuthorities);
	setDetails(api);
	this.principal = principal;
	this.credentials = credentials;
    }

    @Override
    public Object getCredentials() {
	return credentials;
    }

    @Override
    public Object getPrincipal() {
	return principal;
    }

}
