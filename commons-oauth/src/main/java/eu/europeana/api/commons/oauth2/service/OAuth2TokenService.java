package eu.europeana.api.commons.oauth2.service;

import java.util.List;

import org.springframework.security.oauth2.common.OAuth2RefreshToken;

import eu.europeana.api.commons.exception.CommonServiceException;
import eu.europeana.api.commons.exception.CommonServiceRuntimeException;

public interface OAuth2TokenService {
	
	/**
	 * Find an access token by its string value
	 * @param token The identifier to search on
	 * @return An access token
	 * @throws CommonServiceException catched exception if a logical problem occurs during the execution 
	 * @throws CommonServiceRuntimeException runtime exception if a configuration related exception occur
	 */
	OAuth2RefreshToken findAccessToken(String token) throws CommonServiceException, CommonServiceRuntimeException;
	
	/**
	 * Find the list of access tokens by username
	 * @param clientId The client id to search on
	 * @param userName The username to search on
	 * @return A list of access tokens
	 * @throws CommonServiceException catched exception if a logical problem occurs during the execution 
	 * @throws CommonServiceRuntimeException runtime exception if a configuration related exception occur
	 */
	List<OAuth2RefreshToken> findByClientIdAndUserName(String clientId, String userName) throws CommonServiceException, CommonServiceRuntimeException;

	/**
	 * Find the list of access tokens associated to a client id
	 * @param clientId The client id to search on
	 * @return A list of access tokens
	 * @throws CommonServiceException catched exception if a logical problem occurs during the execution 
	 * @throws CommonServiceRuntimeException runtime exception if a configuration related exception occur
	 */
	List<OAuth2RefreshToken> findByClientId(String clientId) throws CommonServiceException, CommonServiceRuntimeException;
		
}