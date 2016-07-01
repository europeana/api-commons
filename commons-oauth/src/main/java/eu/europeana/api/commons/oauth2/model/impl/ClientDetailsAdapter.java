package eu.europeana.api.commons.oauth2.model.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import eu.europeana.api.commons.oauth2.model.ApiKey;

public class ClientDetailsAdapter implements ClientDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ApiKey apiKey;

	public ClientDetailsAdapter() {

	}

	public ClientDetailsAdapter(ApiKey apiKey) {
		this.apiKey = apiKey;
	}

	@Override
	public String getClientId() {
		return getApiKey().getApiKey();
	}

	@Override
	public Set<String> getResourceIds() {
		return null;
	}

	@Override
	public boolean isSecretRequired() {
		return false;
	}

	@Override
	public String getClientSecret() {
		return null;
	}

	@Override
	public boolean isScoped() {
		return false;
	}

	@Override
	public Set<String> getScope() {
		return null;
	}

	@Override
	public Set<String> getAuthorizedGrantTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getRegisteredRedirectUri() {
		return null;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public Integer getAccessTokenValiditySeconds() {
		return -1;
	}

	@Override
	public Integer getRefreshTokenValiditySeconds() {
		return -1;
	}

	@Override
	public boolean isAutoApprove(String scope) {
		return false;
	}

	@Override
	public Map<String, Object> getAdditionalInformation() {
		// TODO Auto-generated method stub
		return null;
	}

	public ApiKey getApiKey() {
		return apiKey;
	}

	public void setApiKey(ApiKey apiKey) {
		this.apiKey = apiKey;
	}

}
