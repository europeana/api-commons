package eu.europeana.api.commons.auth.model;

/**
 * Class to hold client credentials
 * @author srishti singh
 * @since 1 March 2025
 */
public class ClientCredentials {

    private String token;

    private String refreshToken;

    private Long expirationTime;

    private String apikey;


    public ClientCredentials() {

    }
    public ClientCredentials(String token) {
        this.token = token;
    }


    public String getToken() {
        return token;
    }


    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }
}
