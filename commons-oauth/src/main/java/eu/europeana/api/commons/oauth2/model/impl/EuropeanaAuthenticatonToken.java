package eu.europeana.api.commons.oauth2.model.impl;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * This class maps contents of JwtToken
 * 
 * in collection of Collection<GrantedAuthority> we store roles 
 * in details we store key from resource_access (i.e. api_annotations, api_entity)
 * 
 * @author GrafR
 *
 */
public class EuropeanaAuthenticatonToken extends AbstractAuthenticationToken {

    /**
     * 
     */
    private static final long serialVersionUID = -3956175951431237501L;

    Collection<? extends GrantedAuthority> collection = null;
    
    String details = null;
    
    String principal = null;
    
    
    public EuropeanaAuthenticatonToken(Collection<? extends GrantedAuthority> arg0) {
	super(arg0);
    }

    /**
     * This constructor supports also details
     * @param arg0
     * @param details
     */
    public EuropeanaAuthenticatonToken(Collection<? extends GrantedAuthority> arg0, String details, String principal) {
	super(arg0);
	setDetails(details);
	setPrincipal(principal);
    }

    @Override
    public Object getCredentials() {
	return null;
    }

    public String getPrincipal() {
	// returns the "preferred_username" value from the JWT token
	return principal;
    }

    public String getDetails() {
	// returns the "api" value from the JWT token
	return details;
    }

    public void setDetails(String details) {
	// sets the "api" value from the JWT token
	this.details = details;
    }

    public void setPrincipal(String principal) {
	// sets the "preferred_username" value from the JWT token
	this.principal = principal;
    }

}
