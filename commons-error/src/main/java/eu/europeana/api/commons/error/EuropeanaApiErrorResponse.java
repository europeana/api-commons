package eu.europeana.api.commons.error;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;

/**
 * This class contains fields to be returned by APIs when an error occurs within the application.
 *
 */
@JsonPropertyOrder({"success", "status", "error", "message", "timestamp", "path"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EuropeanaApiErrorResponse {
    private final boolean success = false;

    private final int status;
    private final String error;

    private final String message;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
    private final OffsetDateTime timestamp = OffsetDateTime.now();

    private final String trace;

    private final String path;

    private final String code;

    private EuropeanaApiErrorResponse(int status, String error, String message, String trace, String path, String code) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.trace = trace;
        this.path = path;
        this.code = code;
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

        public Builder(HttpServletRequest httpRequest, Exception e, boolean stacktraceEnabled) {
            this.path = ResponseUtils.getRequestPath(httpRequest);
            if (stacktraceEnabled && httpRequest.getParameter("debug") != null ) {
                this.trace = ResponseUtils.getExceptionStackTrace(e);
            }
        }

        public Builder setStatus(int status) {
            this.status = status;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setError(String error) {
            this.error = error;
            return this;
        }

        public Builder setCode(String code) {
            this.code = code;
            return this;
        }

        public EuropeanaApiErrorResponse build() {
            return new EuropeanaApiErrorResponse(status, error, message, trace, path, code);
        }
    }
}
