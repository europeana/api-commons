/**
 * 
 */
package eu.europeana.api.commons.auth.service;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Hugo
 * @since 12 Mar 2025
 */
public interface GrantConstants {

    public static final String grant_type         = "grant_type";
    public static final String client_id          = "client_id";
    public static final String client_secret      = "client_secret";
    public static final String username           = "username";
    public static final String password           = "password";
    public static final String scope              = "scope";
    public static final String redirect_uri       = "redirect_uri";

    public static final String response_type      = "response_type";
    public static final String access_token       = "access_token";
    public static final String refresh_token      = "refresh_token";
    public static final String expires_in         = "expires_in";
    public static final String refresh_expires_in = "refresh_expires_in";
    public static final String session_state      = "session_state";

    public static final Collection<String> params 
        = Arrays.asList(grant_type, client_id, client_secret, username, password
                      , scope, redirect_uri);
}
