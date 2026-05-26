package eu.europeana.api.commons.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EuropeanaClientRegistrationException extends ClientRegistrationException {

    private int httpStatusCode;
    private String code;
    private String error;

    public EuropeanaClientRegistrationException(String msg){
        super(msg);
    }

    public EuropeanaClientRegistrationException(String message, Throwable th){
        super(message, th);
    }

    @JsonCreator
    public EuropeanaClientRegistrationException(@JsonProperty("error") String error,
                                                @JsonProperty("code") String code
            , @JsonProperty("message") String message) {
        super(message);
        this.error = error;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getError() {
        return error;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }
}