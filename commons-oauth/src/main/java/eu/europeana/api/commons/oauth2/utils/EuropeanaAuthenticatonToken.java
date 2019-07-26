package eu.europeana.api.commons.oauth2.utils;

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
    
    
    public EuropeanaAuthenticatonToken(Collection<? extends GrantedAuthority> arg0) {
	super(arg0);
	// TODO Auto-generated constructor stub
    }

    @Override
    public Object getCredentials() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Object getPrincipal() {
	// TODO Auto-generated method stub
	return null;
    }

}
