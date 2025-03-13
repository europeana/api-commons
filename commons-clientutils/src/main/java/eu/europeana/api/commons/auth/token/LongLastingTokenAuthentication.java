package eu.europeana.api.commons.auth.token;

import eu.europeana.api.commons.auth.service.AuthGrant;
import eu.europeana.api.commons.auth.service.AuthenticationService;
import eu.europeana.api.commons.auth.service.TokenResponse;

/**
 * @author Hugo
 * @since 11 Mar 2025
 */
public class LongLastingTokenAuthentication extends TokenBasedAuthentication {

    private boolean offline;

    private AuthenticationService authService;

    private AuthGrant grant;

    private TokenResponse tokenResponse;


    public LongLastingTokenAuthentication(AuthenticationService authService
                                        , AuthGrant grant, boolean offline) {
        this.authService = authService;
        this.grant = grant;
        this.offline = offline;
    }

    public LongLastingTokenAuthentication(AuthenticationService authService
                                        , AuthGrant grant) {
        this(authService, grant, true);
    }

    /**
     * Set Authorisation method - Handles the issue of token if necessary and add it to the HttpRequest of the client
     * if the token provided is still valid - set the 'AUTHORIZATION' header in the request
     * if token is NOT valid - issue the new/refresh token and attach that to the request
     *
     * Also update the client credentials if new/refresh token is generated
     *
     * @param httpRequest htt request of the client
     * @throws CommonAuthenticationException
     */
    @Override
    protected String getAccessToken() {
        
        AuthGrant grant = this.grant;
        if ( this.tokenResponse == null || !offline ) {
            this.tokenResponse = authService.newToken(grant);
            return this.tokenResponse.accessToken();
        }
        
        if ( !this.tokenResponse.hasTokenExpired() ) {
            return this.tokenResponse.accessToken();
        }

        if ( this.tokenResponse.canRefresh() ) {
            grant = this.grant.newRefreshGrant(this.tokenResponse.refreshToken());
        }

        this.tokenResponse = authService.newToken(grant);
        return this.tokenResponse.accessToken();
    }
}
