package eu.europeana.api.commons.web.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.networknt.schema.ValidationMessage;
import eu.europeana.api.commons.exception.JsonValidationFailedException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.servlet.NoHandlerFoundException;
import eu.europeana.api.commons.config.i18n.I18nService;
import eu.europeana.api.commons.error.EuropeanaApiErrorResponse;
import eu.europeana.api.commons.error.EuropeanaApiException;
import eu.europeana.api.commons.error.EuropeanaI18nApiException;
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
    
    @Value("${server.error.see-also:}")    
    private String seeAlso;

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
     * Default handler for EuropeanaI18nApiException types
     *
     * @param e caught exception
     * @param httpRequest the http request
     * @return api response entity
     */
    @ExceptionHandler(HttpException.class)
    public ResponseEntity<EuropeanaApiErrorResponse> handleCommonHttpException(
        HttpException e, HttpServletRequest httpRequest) {
      final String errorMessage = buildResponseMessage(e, e.getI18nKey(), e.getI18nParams());
      EuropeanaApiErrorResponse response =
          new EuropeanaApiErrorResponse.Builder(httpRequest, e, stackTraceEnabled())
              .setStatus(e.getStatus().value())
              .setError(e.getStatus().getReasonPhrase())
              .setMessage( errorMessage)
              // code only included in JSON if a value is set in exception
              .setCode(getNormalizedErrorCode(e.getI18nKey()))
              .setSeeAlso(getSeeAlso())
              .build();
      
      LOG.error("Error response ({}): {}", e.getStatus().value(), errorMessage, e);
      
      return ResponseEntity.status(e.getStatus()).headers(createHttpHeaders(httpRequest))
          .body(response);
    }

    protected String getNormalizedErrorCode(String i18nKey) {
      if(i18nKey == null) {
        return null;  
      }
      
      //EA-3567 extract the part after last point if it exists in the key 
      final String separator = ".";
      if(i18nKey.contains(separator)) {
        return StringUtils.substringAfterLast(i18nKey, separator);
      }
      
      return i18nKey;       
    }

    /**
     * Default handler for EuropeanaApiException types
     *
     * @param e caught exception
     */
    @ExceptionHandler(EuropeanaApiException.class)
    public ResponseEntity<EuropeanaApiErrorResponse> handleEuropeanaBaseException(EuropeanaApiException e, HttpServletRequest httpRequest) {
        logException(e);
        return buildApiErrorResponse(e, httpRequest, null, null);        
    }

    /**
     * Default handler for EuropeanaI18nApiException types
     *
     * @param e caught exception
     * @param httpRequest the http request
     * @return api response entity
     */
    @ExceptionHandler(EuropeanaI18nApiException.class)
    public ResponseEntity<EuropeanaApiErrorResponse> handleEuropeanaApiException(
        EuropeanaI18nApiException e, HttpServletRequest httpRequest) {
      ResponseEntity<EuropeanaApiErrorResponse> response = buildApiErrorResponse(e, httpRequest, e.getI18nKey(), e.getI18nParams());
      LOG.error("Application Error ({}): {}", response.getStatusCode(), response.getBody().getMessage(), e);
      return response;
    }
   
    protected ResponseEntity<EuropeanaApiErrorResponse> buildApiErrorResponse(EuropeanaApiException e,
        HttpServletRequest httpRequest, String i18nKey, String[] i18NParams) {
      EuropeanaApiErrorResponse response =
          new EuropeanaApiErrorResponse.Builder(httpRequest, e, stackTraceEnabled())
              .setStatus(e.getResponseStatus().value())
              .setError(e.getResponseStatus().getReasonPhrase())
              .setMessage(e.doExposeMessage() ? buildResponseMessage(e, i18nKey, i18NParams) : null)
              // code only included in JSON if a value is set in exception
              .setCode(getNormalizedErrorCode(e.getErrorCode()))
              .setSeeAlso(getSeeAlso())
              .build();
      return ResponseEntity.status(e.getResponseStatus()).headers(createHttpHeaders(httpRequest))
          .body(response);
    }
    
    protected String buildResponseMessage(Exception e, String i18nKey, String[] i18nParams) {
      if (getI18nService() != null && StringUtils.isNotBlank(i18nKey)) {
        return getI18nService().getMessage(i18nKey, i18nParams);
      } else {
        return e.getMessage(); 
      }
    }
    
    
    /**
     * Default handler for all other exception types
     *
     * @param e caught exception
     */
    @ExceptionHandler
    public ResponseEntity<EuropeanaApiErrorResponse> handleOtherExceptionTypes(Exception e, HttpServletRequest httpRequest) {
        HttpStatus responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        EuropeanaApiErrorResponse response = new EuropeanaApiErrorResponse.Builder(httpRequest, e, stackTraceEnabled())
                .setStatus(responseStatus.value())
                .setError(responseStatus.getReasonPhrase())
                .setSeeAlso(seeAlso)
                .build();

        LOG.error("Unexpected Internal Server Error (500): {}", response.getMessage(), e);
        
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
        LOG.error("Method not allowed Error (405):", e);
        HttpStatus responseStatus = HttpStatus.METHOD_NOT_ALLOWED;
        EuropeanaApiErrorResponse response = new EuropeanaApiErrorResponse.Builder(httpRequest, e, stackTraceEnabled())
                .setStatus(responseStatus.value())
                .setError(responseStatus.getReasonPhrase())
                .setMessage(e.getMessage())
                .setSeeAlso(seeAlso)
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
        
        LOG.error("Bad Request error (400): {}", response.getMessage(), e);
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
                .setSeeAlso(seeAlso)
                .build();
        
        LOG.error("Bad Request error (400): {}", response.getMessage(), e);
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
        final String message = "Server could not generate a response that is acceptable by the client";
        LOG.error("Media Format not Acceptable  Error (406): {}", message, e);
        HttpStatus responseStatus = HttpStatus.NOT_ACCEPTABLE;
        EuropeanaApiErrorResponse response = new EuropeanaApiErrorResponse.Builder(httpRequest, e, stackTraceEnabled())
            .setStatus(responseStatus.value())
            .setError(responseStatus.getReasonPhrase())
            .setMessage(message)
            .setSeeAlso(seeAlso)
            .build();

        return ResponseEntity
            .status(responseStatus)
            .headers(createHttpHeaders(httpRequest))
            .body(response);
    }

    /**
     * mapping for resource not found errors
     * @param e exception to handle
     * @param httpRequest the http request
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<EuropeanaApiErrorResponse> handleNoHandlerFoundException(
        NoHandlerFoundException e, HttpServletRequest httpRequest) {
      LOG.error("Not found (404):", e);
      EuropeanaApiErrorResponse response =
          new EuropeanaApiErrorResponse.Builder(httpRequest, e, stackTraceEnabled())
              .setStatus(HttpStatus.NOT_FOUND.value())
              .setError(HttpStatus.NOT_FOUND.getReasonPhrase())
              .setMessage(e.getMessage())
              .setSeeAlso(getSeeAlso())
              .build();
      return ResponseEntity.status(HttpStatus.NOT_FOUND.value())
          .contentType(MediaType.APPLICATION_JSON).body(response);
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
        final String message = "Invalid request body";
        EuropeanaApiErrorResponse response = new EuropeanaApiErrorResponse.Builder(httpRequest, e, stackTraceEnabled())
            .setStatus(responseStatus.value())
            .setMessage(message)
            .setError(error)
            .setSeeAlso(seeAlso)
            .build();
        
        LOG.error("Bad Request Error (400): {}", message, e);
        
        return ResponseEntity
            .status(responseStatus)
            .headers(createHttpHeaders(httpRequest))
            .body(response);
    }
    
    protected HttpHeaders createHttpHeaders(HttpServletRequest httpRequest) {
      HttpHeaders headers = new HttpHeaders();
      //enforce application/json as content type, it is the only serialization supported for exceptions
      headers.setContentType(MediaType.APPLICATION_JSON);
      
      //autogenerate allow header if the service is configured
      if(getRequestPathMethodService()!=null) {
        String allowHeaderValue = getRequestPathMethodService().getMethodsForRequestPattern(httpRequest).orElse(httpRequest.getMethod());
        headers.add(HttpHeaders.ALLOW, allowHeaderValue);
      }
      return headers;
    }

    /**
     * The bean needs to be defined in the individual APIs
     * 
     * @return
     */
    AbstractRequestPathMethodService getRequestPathMethodService() {
      return requestPathMethodService;
    }

    public String getSeeAlso() {
      return seeAlso;
    }
    
    protected I18nService getI18nService() {
      return null;
    }

  @ExceptionHandler(JsonValidationFailedException.class)
  public ResponseEntity<EuropeanaApiErrorResponse> onJsonValidationFailedException(JsonValidationFailedException e,HttpServletRequest httpRequest) {
    List<String> messages = e.getValidationMessages().stream()
        .map(ValidationMessage::getMessage)
        .collect(Collectors.toList());

    HttpStatus responseStatus = HttpStatus.BAD_REQUEST;
    EuropeanaApiErrorResponse response = (new EuropeanaApiErrorResponse.Builder(httpRequest, e, this.stackTraceEnabled())).
        setStatus(responseStatus.value()).
        setMessage(String.join(",", messages)).
        setError(responseStatus.getReasonPhrase()).
        setSeeAlso(getSeeAlso()).build();

    return (ResponseEntity.status(responseStatus).headers(this.createHttpHeaders(httpRequest))).body(response);
  }


}
