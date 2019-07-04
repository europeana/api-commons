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
	private String wsKey;

	
	public ClientDetailsAdapter() {

	}

	public ClientDetailsAdapter(ApiKey apiKey) {
		this.apiKey = apiKey;
		this.wsKey = apiKey.getApiKey(); 
	}

	public ClientDetailsAdapter(String wsKey) {
		this.wsKey = wsKey; 
	}
	
	@Override
	public String getClientId() {
		return getWsKey();
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
		return true;
	}

	@Override
	public Map<String, Object> getAdditionalInformation() {
		return null;
	}

	public ApiKey getApiKey() {
		return apiKey;
	}

	public void setApiKey(ApiKey apiKey) {
		this.apiKey = apiKey;
	}

	public String getWsKey() {
	    return wsKey;
	}

	public void setWsKey(String wsKey) {
	    this.wsKey = wsKey;
	}

}
