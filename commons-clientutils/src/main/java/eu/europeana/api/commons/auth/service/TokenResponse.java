/**
 * 
 */
package eu.europeana.api.commons.auth.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import static eu.europeana.api.commons.auth.AuthenticationConstants.*;
import static eu.europeana.api.commons.auth.service.GrantConstants.*;

import java.util.concurrent.TimeUnit;

/**
 * @author Hugo
 * @since 11 Mar 2025
 */
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown=true)
public record TokenResponse(String accessToken, Long expirationTime
                          , String refreshToken, Long refreshExpirationTime
                          , String tokenScope, String sessionState) {
    
    public TokenResponse(
            @JsonProperty(access_token) String accessToken
          , @JsonProperty(expires_in) Long expirationTime
          , @JsonProperty(refresh_token) String refreshToken
          , @JsonProperty(refresh_expires_in) Long refreshExpirationTime
          , @JsonProperty(scope) String tokenScope
          , @JsonProperty(session_state) String sessionState) {
        long now = now();
        this.accessToken           = accessToken;
        this.refreshToken          = refreshToken;
        this.expirationTime        = getExpirationTime(now, expirationTime);
        this.refreshExpirationTime = getExpirationTime(now, refreshExpirationTime);
        this.tokenScope            = tokenScope;
        this.sessionState          = sessionState;
    }

    /**
     * expires = current time + expires_in (from jwt token request) - adjustment
     * @param now
     * @param expiresIn
     * @return
     */
    private static long getExpirationTime(long now, long expiresIn) {
        return (now + expiresIn - BUFFER_TIME_IN_SECONDS);
    }

    // present time in seconds
    private static long now() {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
    }

    /**
     * Checks if the token has expired or not
     * @param tokenExpirationTime
     * @return
     */
    public boolean hasTokenExpired() {
        return (expirationTime() < now());
    }

    public boolean hasRefreshTokenExpired() {
        return (refreshExpirationTime() < now());
    }

    public boolean canRefresh() {
        return ( refreshToken() != null && !hasRefreshTokenExpired() );
    }
}