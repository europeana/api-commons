package eu.europeana.api.commons.error;

import io.micrometer.core.instrument.util.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;

/**
 * Global exception handler that catches all errors and logs the interesting ones
 * To use this create a new class in your application that extends this class and add the @ControllerAdvice annotation
 * to it.
 */
public class EuropeanaGlobalExceptionHandler {

    private static final Logger LOG = LogManager.getLogger(EuropeanaGlobalExceptionHandler.class);

    /**
     * Checks if we should log an error and rethrows it
     * @param e caught exception
     * @throws EuropeanaApiException rethrown exception
     */
    @ExceptionHandler(EuropeanaApiException.class)
    public void handleBaseException(EuropeanaApiException e) throws EuropeanaApiException {
        if (e.doLog()) {
            if (e.doLogStacktrace()) {
                LOG.error("Caught exception", e);
            } else {
                LOG.error("Caught exception: {}", e.getMessage());
            }
        }

        // We simply rethrow so Spring & Jackson will automatically return a json error. Note that this requires all exceptions
        // to have a ResponseStatus annotation, otherwise the exception will default to 500 status
        throw e;
    }

    /**
     * Make sure we return 400 instead of 500 response when input validation fails
     * @param e exception that is thrown
     * @param response the response that is sent back
     * @throws IOException when there's an exception sending back the response
     */
    @SuppressWarnings("findsecbugs:XSS_SERVLET")
    // Safest would be to use Owasp's Encode.forJavasScript(e.getMessage) but that translates characters such as ' to \\x27
    // Since we are in control (mostly) of the generated error messages we'll settle for now for simple escaping
    @ExceptionHandler
    public void handleInputValidationError(ConstraintViolationException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), StringEscapeUtils.escapeJson(e.getMessage()));
    }
}
