package eu.europeana.api.commons.oauth2.service.impl;import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.stereotype.Service;

import eu.europeana.api.commons.exception.CommonServiceException;
import eu.europeana.api.commons.exception.CommonServiceRuntimeException;
import eu.europeana.api.commons.oauth2.service.OAuth2TokenService;

/**
 * Implementation of oAuth TokenStore. Manages the persistency of access tokens
 */
@Service
public class BridgeApiTokenStoreService extends InMemoryTokenStore implements TokenStore, OAuth2TokenService {

	public BridgeApiTokenStoreService() {
		super();
		setAuthenticationKeyGenerator(new BridgeAuthenticationKeyGenerator());
		initService();
	}
	
	private void initService() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public OAuth2RefreshToken findAccessToken(String token)
			throws CommonServiceException, CommonServiceRuntimeException {
		
		OAuth2AccessToken accessToken = readAccessToken(token);
		return convertToRefreshTocken(accessToken);	
	}

	protected OAuth2RefreshToken convertToRefreshTocken(OAuth2AccessToken accessToken) {
		//Static tokens (refreshtoken is the same as access token in this implementation)
		return new DefaultOAuth2RefreshToken(accessToken.getValue());
	}

	@Override
	public List<OAuth2RefreshToken> findByClientIdAndUserName(String clientId, String userName)
			throws CommonServiceException, CommonServiceRuntimeException {
		
		Collection<OAuth2AccessToken> accessTokens = findTokensByClientIdAndUserName(clientId, userName);
		return convertToRefreshToken(accessTokens);
	}

	@Override
	public List<OAuth2RefreshToken> findByClientId(String clientId)
			throws CommonServiceException, CommonServiceRuntimeException {
		
		return convertToRefreshToken(findTokensByClientId(clientId));
		
	}
	protected List<OAuth2RefreshToken> convertToRefreshToken(Collection<OAuth2AccessToken> accessTokens) {
		List<OAuth2RefreshToken> refreshTokens = new ArrayList<OAuth2RefreshToken>(accessTokens.size());
		for (OAuth2AccessToken accessToken : accessTokens) {
			refreshTokens.add(convertToRefreshTocken(accessToken));	
		}
		return refreshTokens;
	}

	

	



}
