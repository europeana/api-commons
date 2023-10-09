package eu.europeana.api.commons.error;


import static eu.europeana.api.commons.definitions.vocabulary.CommonApiConstants.QUERY_PARAM_PROFILE_SEPARATOR;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import eu.europeana.api.commons.definitions.vocabulary.CommonApiConstants;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import org.springframework.util.StringUtils;

/**
 * This class contains fields to be returned by APIs when an error occurs within the application.
 *
 */
@JsonPropertyOrder({"@context", "type", "success", "status", "code", "error", "message", "seeAlso", "timestamp", "path", "trace"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EuropeanaApiErrorResponse {
  
   @JsonIgnore
   public static final String CONTEXT = "http://www.europeana.eu/schemas/context/api.jsonld";
  
  
    @JsonProperty("@context")
    private final String context = CONTEXT;
    private final String type = "ErrorResponse";
    private final boolean success = false;

    private final int status;
    private final String error;

    private final String message;
    private final String seeAlso;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
    private final OffsetDateTime timestamp = OffsetDateTime.now();

    private final String trace;

    private final String path;

    private final String code;

    private EuropeanaApiErrorResponse(int status, String code, String error, String message, String seeAlso, String path, String trace) {
        this.status = status;
        this.code = code;
        this.error = error;
        this.message = message;
        this.seeAlso = seeAlso;
        this.path = path;
        this.trace = trace;
    }

    public String getError() {
        return error;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public String getTrace() {
        return trace;
    }

    public String getPath() {
        return path;
    }

    public String getCode() {
        return code;
    }


    public static class Builder {
        private int status;
        private String message;
        private String error;
        private String trace;
        private final String path;
        private String code;
        private String seeAlso;
        
        public Builder(HttpServletRequest httpRequest, Exception e, boolean stacktraceEnabled) {
            this.path = ResponseUtils.getRequestPath(httpRequest);
            boolean includeErrorStack = false;
            String profileParamString = httpRequest.getParameter(CommonApiConstants.QUERY_PARAM_PROFILE);
            // check if profile contains debug
            if (StringUtils.hasLength(profileParamString)) {
                includeErrorStack = List.of(profileParamString.split(QUERY_PARAM_PROFILE_SEPARATOR))
                    .contains(CommonApiConstants.PROFILE_DEBUG);
            }
            if (stacktraceEnabled && includeErrorStack) {
                this.trace = ResponseUtils.getExceptionStackTrace(e);
            }
        }

        public Builder setStatus(int status) {
            this.status = status;
            return this;
        }
        
        public Builder setCode(String code) {
          this.code = code;
          return this;
        }

        public Builder setError(String error) {
            this.error = error;
            return this;
        }
        
        public Builder setMessage(String message) {
          this.message = message;
          return this;
        }

        public Builder setSeeAlso(String seeAlso) {
          this.seeAlso = seeAlso;
          return this;
        }
       

        public EuropeanaApiErrorResponse build() {
            return new EuropeanaApiErrorResponse(status, code, error, message, seeAlso, path, trace);
        }
    }
   
    public String getType() {
      return type;
    }

    public String getSeeAlso() {
      return seeAlso;
    }
}
