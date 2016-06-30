package eu.europeana.api.commons.oauth2.service.impl;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;

public class BridgeAuthenticationKeyGenerator implements AuthenticationKeyGenerator {

	@Override
	public String extractKey(OAuth2Authentication authentication) {
		//principal = user token
		return authentication.getUserAuthentication().getPrincipal().toString();
	}

}
