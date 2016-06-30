package eu.europeana.api.commons.oauth2.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;

public class BridgeAuthenticationManager extends OAuth2AuthenticationManager{

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

}
