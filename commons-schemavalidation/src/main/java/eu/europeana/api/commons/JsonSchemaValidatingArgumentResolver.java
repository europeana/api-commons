package eu.europeana.api.commons;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion.VersionFlag;
import com.networknt.schema.ValidationMessage;
import eu.europeana.api.commons.exception.JsonSchemaLoadingFailedException;
import eu.europeana.api.commons.exception.JsonValidationFailedException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class JsonSchemaValidatingArgumentResolver implements
    HandlerMethodArgumentResolver  {
  private final ObjectMapper objectMapper;
  private final ResourcePatternResolver resourcePatternResolver;
  private final Map<String, JsonSchema> schemaCache;

  public JsonSchemaValidatingArgumentResolver(ObjectMapper objectMapper, ResourcePatternResolver resourcePatternResolver) {
    this.objectMapper = objectMapper;
    this.resourcePatternResolver = resourcePatternResolver;
    this.schemaCache = new ConcurrentHashMap<>();
  }

  @Override
  public boolean supportsParameter(MethodParameter methodParameter) {
    return methodParameter.getParameterAnnotation(ValidJson.class) != null;
  }

  @Override
  public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory)
      throws URISyntaxException, IOException {
    // get schema path from ValidJson annotation
  if (supportsParameter(methodParameter)) {

    ValidJson parameterAnnotation = methodParameter.getParameterAnnotation(ValidJson.class);
    JsonSchema schema = parameterAnnotation!=null ? getJsonSchemaBasedOnInput( parameterAnnotation.path(), parameterAnnotation.uri()) :null;
    if (schema != null) {
      String schemaRef = parameterAnnotation.nested();
      JsonSchema reqSchema = ValidationUtils.getSubSchema(schema, schemaRef);
      // parse json payload
      JsonNode json = getParsedJson(nativeWebRequest);
      // Do actual validation
      Set<ValidationMessage> validationResult = reqSchema.validate(json, json, schemaRef);
      if (!validationResult.isEmpty()) {
        // throw exception if validation failed
        throw new JsonValidationFailedException(validationResult);
      }
      // No validation errors, convert JsonNode to method parameter type and return it
      return objectMapper.treeToValue(json, methodParameter.getParameterType());
    }
  }
  return null;
  }

  private JsonNode getParsedJson(NativeWebRequest nativeWebRequest) throws IOException   {
        HttpServletRequest httpServletRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String jsonStream = httpServletRequest!=null ? StreamUtils.copyToString(httpServletRequest.getInputStream(),StandardCharsets.UTF_8) :null;
        return objectMapper.readTree(jsonStream);
  }

  private JsonSchema getJsonSchemaBasedOnInput(String schemaPath, String schemaUri) throws URISyntaxException {
    JsonSchema  schema = null;
    // get JsonSchema from schemaPath the URI path is prioritized if provided.
    if(StringUtils.isNotBlank(schemaPath)) {
      schema = getJsonSchema(schemaPath);
    }
    if(StringUtils.isNotBlank(schemaUri)){
      schema = ValidationUtils.getJsonSchemaFromUrl(schemaUri);
    }
    return schema;
  }

  private JsonSchema getJsonSchema(String schemaPath) {
    return schemaCache.computeIfAbsent(schemaPath, path -> {
      Resource resource = resourcePatternResolver.getResource(path);
      if (!resource.exists()) {
        throw new JsonSchemaLoadingFailedException("Schema file does not exist, path: " + path);
      }
      JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(VersionFlag.V201909);
      try (InputStream schemaStream = resource.getInputStream()) {
        return schemaFactory.getSchema(schemaStream);
      } catch (Exception e) {
        throw new JsonSchemaLoadingFailedException("An error occurred while loading JSON Schema, path: " + path, e);
      }
    });
  }
}

