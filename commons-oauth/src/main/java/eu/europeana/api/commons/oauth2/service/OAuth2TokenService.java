/*
 * Copyright 2007-2012 The Europeana Foundation
 *
 *  Licenced under the EUPL, Version 1.1 (the "Licence") and subsequent versions as approved 
 *  by the European Commission;
 *  You may not use this work except in compliance with the Licence.
 *  
 *  You may obtain a copy of the Licence at:
 *  http://joinup.ec.europa.eu/software/page/eupl
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under 
 *  the Licence is distributed on an "AS IS" basis, without warranties or conditions of 
 *  any kind, either express or implied.
 *  See the Licence for the specific language governing permissions and limitations under 
 *  the Licence.
 */
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