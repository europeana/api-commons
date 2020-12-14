package eu.europeana.api.commons.error;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

/**
 * Global exception handler that catches all errors and logs the interesting ones
 * To use this create a new class in your application that extends this class and add the @ControllerAdvice annotation
 * to it.
 */
@Component
public class EuropeanaGlobalExceptionHandler {

    @Value("${server.error.include-stacktrace:ON_PARAM}")
    private ErrorProperties.IncludeStacktrace includeStacktraceConfig;

    private static final Logger LOG = LogManager.getLogger(EuropeanaGlobalExceptionHandler.class);

    /**
     * Checks if {@link EuropeanaApiException} instances should be logged or not
     *
     * @param e exception
     */
    protected void logException(EuropeanaApiException e) {
        if (e.doLog()) {
            if (e.doLogStacktrace()) {
                LOG.error("Caught exception", e);
            } else {
                LOG.error("Caught exception: {}", e.getMessage());
            }
        }
    }

    /**
     * Default handler for EuropeanaApiException types
     *
     * @param e caught exception
     */
    @ExceptionHandler
    public ResponseEntity<EuropeanaApiErrorResponse> handleEuropeanaBaseException(EuropeanaApiException e, HttpServletRequest httpRequest) {
        logException(e);
        EuropeanaApiErrorResponse response = new EuropeanaApiErrorResponse.Builder(httpRequest, e, stackTraceEnabled())
                .setStatus(e.getResponseStatus().value())
                .setError(e.getResponseStatus().getReasonPhrase())
                .setMessage(e.doExposeMessage() ? e.getMessage() : null)
                // code only included in JSON if a value is set in exception
                .setCode(e.getErrorCode())
                .build();

        return new ResponseEntity<>(response, e.getResponseStatus());
    }

    /**
     * Default handler for all other exception types
     *
     * @param e caught exception
     */
    @ExceptionHandler
    public ResponseEntity<EuropeanaApiErrorResponse> handleOtherExceptionTypes(Exception e, HttpServletRequest httpRequest) {
        LOG.error(e);
        EuropeanaApiErrorResponse response = new EuropeanaApiErrorResponse.Builder(httpRequest, e, stackTraceEnabled())
                .setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handler for ConstraintValidation errors
     * Make sure we return 400 instead of 500 response when input validation fails; also include error message
     */
    @ExceptionHandler
    public ResponseEntity<EuropeanaApiErrorResponse> handleInputValidationError(ConstraintViolationException e, HttpServletRequest httpRequest) {
        EuropeanaApiErrorResponse response = new EuropeanaApiErrorResponse.Builder(httpRequest, e, stackTraceEnabled())
                .setStatus(HttpStatus.BAD_REQUEST.value())
                .setError(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .setMessage(e.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Checks whether stacktrace for exceptions should be included in responses
     * @return true if IncludeStacktrace config is not disabled on server
     */
    protected boolean stackTraceEnabled(){
        return includeStacktraceConfig != ErrorProperties.IncludeStacktrace.NEVER;
    }
}
