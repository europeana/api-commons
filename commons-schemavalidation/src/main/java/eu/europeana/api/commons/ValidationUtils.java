package eu.europeana.api.commons;



import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion.VersionFlag;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonValidator;
import com.networknt.schema.OneOfValidator;
import com.networknt.schema.RefValidator;

/**
 * @author Hugo
 * @since 18 Dec 2023
 */
public class ValidationUtils
{
  public static JsonSchema getSubSchema(JsonSchema schema, String ref) {
    if ( schema.getSchemaPath().equals(ref) ) { return schema; }
    return getSchemaFromValidators(schema.getValidators().values(), ref);
  }

  private static JsonSchema getSchemaFromValidators(Collection<JsonValidator> validators, String ref) {
    for (JsonValidator v : validators) {
      JsonSchema ret = getSchemaFromValidator(v, ref);
      if ( ret != null ) { return ret; }
    }
    return null;
  }

  private static JsonSchema getSubSchema(Collection<JsonSchema> schemas, String ref) {
    for (JsonSchema schema : schemas ) {
      JsonSchema ret = getSubSchema(schema, ref);
      if ( ret != null ) { return ret; }
    }
    return null;
  }

  private static JsonSchema getSchemaFromValidator(JsonValidator v, String ref) {
    if ( v instanceof OneOfValidator ) {
      return getSubSchema(((OneOfValidator)v).getChildSchemas(), ref);
    }
    if ( v instanceof RefValidator ) {
      return getSubSchema(((RefValidator)v).getSchemaRef().getSchema(), ref);
    }
    return null;
  }

  public static JsonSchema getJsonSchemaFromUrl(String uri) throws URISyntaxException {
    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(VersionFlag.V201909);
    return factory.getSchema(new URI(uri));
  }
}

