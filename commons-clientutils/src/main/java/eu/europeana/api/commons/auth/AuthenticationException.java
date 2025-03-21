package eu.europeana.api.commons.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Hugo
 * @since 11 Mar 2025
 */
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AuthenticationException extends RuntimeException {

    public static final String TOKEN_MISSING      = "Handler returned no token";
    public static final String AUTH_SERVICE_ERROR = "Error connecting to auth endpoint";
    public static final String CONFIG_MISSING     = "Missing configuration for authentication";

    private static final long serialVersionUID = 8496589899526050812L;

    private String error;

    public AuthenticationException(String message) {
        super(message);
    }

    @JsonCreator
    public AuthenticationException(@JsonProperty("error") String error
                                 , @JsonProperty("error_description") String message) {
        super(message);
        this.error = error;
    }

    public AuthenticationException(String message, Throwable th){
        super(message, th);
    }

    public String getError() {
        return this.error;
    }
}
