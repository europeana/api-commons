package eu.europeana.api.commons.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.europeana.api.commons.config.i18n.I18nService;
import eu.europeana.api.commons.web.exception.HttpException;
import eu.europeana.api.commons.web.model.ApiResponse;
import eu.europeana.api.commons.web.model.ErrorApiResponse;

public abstract class ApiResponseBuilder {

  Logger logger = LogManager.getLogger(getClass());
  private static ObjectMapper objectMapper = new ObjectMapper();

  protected abstract I18nService getI18nService();

  public Logger getLogger() {
    return logger;
  }

  public ErrorApiResponse buildErrorResponse(String errorMessage, String action, String apiKey) {
    return new ErrorApiResponse(apiKey, action, errorMessage);
  }

  protected ErrorApiResponse getErrorReport(String apiKey, String action, Throwable th,
      boolean includeErrorStack) {

    ErrorApiResponse response;

    final String blank = " ";
    StringBuilder messageBuilder = new StringBuilder();

    if (!(th instanceof HttpException)) {
      messageBuilder.append(blank).append(th.getMessage()).append(". ");
    } else {
      HttpException ex = (HttpException) th;
      String message = getI18nService().getMessage(ex.getI18nKey(), ex.getI18nParams());
      messageBuilder.append(message);
    }


    if (th != null && th.getCause() != null && th != th.getCause())
      messageBuilder.append(blank).append(th.getCause().getMessage());

    response = buildErrorResponse(messageBuilder.toString(), action, apiKey);

    if (includeErrorStack && th != null)
      response.setStackTrace(getStackTraceAsString(th));

    return response;
  }

  String getStackTraceAsString(Throwable th) {
    StringWriter out = new StringWriter();
    th.printStackTrace(new PrintWriter(out));
    return out.toString();
  }

  protected String serializeResponse(ApiResponse res) {
    String errorMessage;
    try {
      // correct serialization
      return objectMapper.writeValueAsString(res);
    } catch (JsonGenerationException e) {
      getLogger().error("Json Generation Exception: " + e.getMessage(), e);
      errorMessage = "Json Generation Exception: " + e.getMessage() + " See error logs!";
    } catch (JsonMappingException e) {
      getLogger().error("Json Mapping Exception: " + e.getMessage(), e);
      errorMessage = "Json Generation Exception: " + e.getMessage() + " See error logs!";
    } catch (IOException e) {
      getLogger().error("I/O Exception: " + e.getMessage(), e);
      errorMessage = "I/O Exception: " + e.getMessage() + " See error logs!";
    }
    return errorMessage;
  }
}
