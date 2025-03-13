package eu.europeana.api.commons.auth.token;

/**
 * @author Hugo
 * @since 11 Mar 2025
 */
public class StaticTokenAuthentication extends TokenBasedAuthentication {

    protected String token;

    public StaticTokenAuthentication(String token) {
        this.token = token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    protected String getAccessToken() {
        return this.token;
    }
}
