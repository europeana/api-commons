package eu.europeana.api.commons.web.exception;

import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import eu.europeana.api.commons.error.EuropeanaApiErrorResponse;
import eu.europeana.api.commons.error.EuropeanaApiException;
import eu.europeana.api.commons.web.service.AbstractRequestPathMethodService;

/**
 * Global exception handler that catches all errors and logs the interesting ones
 * To use this, create a new class in your application that extends this class and add the @ControllerAdvice annotation
 * to it.
 */
@Component
public class EuropeanaGlobalExceptionHandler {

    @Value("${server.error.include-stacktrace:ON_PARAM}")
    private ErrorProperties.IncludeStacktrace includeStacktraceConfig;

    private static final Logger LOG = LogManager.getLogger(EuropeanaGlobalExceptionHandler.class);

    protected AbstractRequestPathMethodService requestPathMethodService;
    
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
     * Checks whether stacktrace for exceptions should be included in responses
     * @return true if IncludeStacktrace config is not disabled on server
     */
    protected boolean stackTraceEnabled(){
        return includeStacktraceConfig != ErrorProperties.IncludeStacktrace.NEVER;
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

        return ResponseEntity
                .status(e.getResponseStatus())
                .headers(createHttpHeaders(httpRequest))
                .body(response);
    }

    /**
     * Default handler for all other exception types
     *
     * @param e caught exception
     */
    @ExceptionHandler
    public ResponseEntity<EuropeanaApiErrorResponse> handleOtherExceptionTypes(Exception e, HttpServletRequest httpRequest) {
        LOG.error("Error: ", e);
        HttpStatus responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        EuropeanaApiErrorResponse response = new EuropeanaApiErrorResponse.Builder(httpRequest, e, stackTraceEnabled())
                .setStatus(responseStatus.value())
                .setError(responseStatus.getReasonPhrase())
                .build();

        return ResponseEntity
                .status(responseStatus)
                .headers(createHttpHeaders(httpRequest))
                .body(response);
    }

    /**
     * Handler for HttpRequestMethodNotSupportedException errors
     * Make sure we return 405 instead of 500 response when http method is not supported; also include error message
     */
    @ExceptionHandler
    public ResponseEntity<EuropeanaApiErrorResponse> handleHttpMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest httpRequest) {
        HttpStatus responseStatus = HttpStatus.METHOD_NOT_ALLOWED;
        EuropeanaApiErrorResponse response = new EuropeanaApiErrorResponse.Builder(httpRequest, e, stackTraceEnabled())
                .setStatus(responseStatus.value())
                .setError(responseStatus.getReasonPhrase())
                .setMessage(e.getMessage())
                .build();

        Set<HttpMethod> supportedMethods = e.getSupportedHttpMethods();

        // set Allow header in error response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (supportedMethods != null) {
            headers.setAllow(supportedMethods);
        }
        return new ResponseEntity<>(response, headers, responseStatus);
    }


    /**
     * Handler for ConstraintValidation errors
     * Make sure we return 400 instead of 500 response when input validation fails; also include error message
     */
    @ExceptionHandler
    public ResponseEntity<EuropeanaApiErrorResponse> handleInputValidationError(ConstraintViolationException e, HttpServletRequest httpRequest) {
        HttpStatus responseStatus = HttpStatus.BAD_REQUEST;
        EuropeanaApiErrorResponse response = new EuropeanaApiErrorResponse.Builder(httpRequest, e, stackTraceEnabled())
                .setStatus(responseStatus.value())
                .setError(responseStatus.getReasonPhrase())
                .setMessage(e.getMessage())
                .build();

        return ResponseEntity
                .status(responseStatus)
                .headers(createHttpHeaders(httpRequest))
                .body(response);
    }

    /**
     * MissingServletRequestParameterException thrown when a required parameter is not included in a request.
     */
    @ExceptionHandler
    public ResponseEntity<EuropeanaApiErrorResponse> handleInputValidationError(MissingServletRequestParameterException e, HttpServletRequest httpRequest) {
        HttpStatus responseStatus = HttpStatus.BAD_REQUEST;
        EuropeanaApiErrorResponse response = (new EuropeanaApiErrorResponse.Builder(httpRequest, e, stackTraceEnabled()))
                .setStatus(responseStatus.value())
                .setError(responseStatus.getReasonPhrase())
                .setMessage(e.getMessage())
                .build();

        return ResponseEntity
                .status(responseStatus)
                .headers(createHttpHeaders(httpRequest))
                .body(response);
    }

    /**
     * Customise the response for {@link org.springframework.web.HttpMediaTypeNotAcceptableException}
     */
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<EuropeanaApiErrorResponse> handleMediaTypeNotAcceptableException(
        HttpMediaTypeNotAcceptableException e, HttpServletRequest httpRequest) {

        HttpStatus responseStatus = HttpStatus.NOT_ACCEPTABLE;
        EuropeanaApiErrorResponse response = new EuropeanaApiErrorResponse.Builder(httpRequest, e, stackTraceEnabled())
            .setStatus(responseStatus.value())
            .setError(responseStatus.getReasonPhrase())
            .setMessage("Server could not generate a response that is acceptable by the client")
            .build();

        return ResponseEntity
            .status(responseStatus)
            .headers(createHttpHeaders(httpRequest))
            .body(response);
    }


    /**
     * Exception thrown by Spring when RequestBody validation fails.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<EuropeanaApiErrorResponse> handleMethodArgNotValidException(MethodArgumentNotValidException e, HttpServletRequest httpRequest) {
        BindingResult result = e.getBindingResult();
        String error ="";
        List<FieldError> fieldErrors = result.getFieldErrors();
        if(!fieldErrors.isEmpty()) {
            // just return the first error
            error = fieldErrors.get(0).getField() + " " + fieldErrors.get(0).getDefaultMessage();
        }
        HttpStatus responseStatus = HttpStatus.BAD_REQUEST;
        EuropeanaApiErrorResponse response = new EuropeanaApiErrorResponse.Builder(httpRequest, e, stackTraceEnabled())
            .setStatus(responseStatus.value())
            .setMessage("Invalid request body")
            .setError(error)
            .build();

        return ResponseEntity
            .status(responseStatus)
            .headers(createHttpHeaders(httpRequest))
            .body(response);
    }
    
    protected HttpHeaders createHttpHeaders(HttpServletRequest httpRequest) {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      if(requestPathMethodService!=null) {
        String allowHeaderValue = requestPathMethodService.getMethodsForRequestPattern(httpRequest).orElse(httpRequest.getMethod());
        headers.add(HttpHeaders.ALLOW, allowHeaderValue);
      }
      return headers;
    }
}
