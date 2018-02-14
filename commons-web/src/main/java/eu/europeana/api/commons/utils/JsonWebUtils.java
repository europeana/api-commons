package eu.europeana.api.commons.utils;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author GordeaS
 *
 */
@Deprecated
public class JsonWebUtils {
	
	private static final Logger log = Logger.getLogger(JsonWebUtils.class);
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	public static String toJson(Object object) {
		return toJson(object, null, false, -1);
	}
	
	public static String toJson(Object object, String callback) {
		return toJson(object, callback, false, -1);
	}
		
	public static String toJson(Object object, String callback, boolean shortObject, int objectId) {
			
		objectMapper.setSerializationInclusion(Include.NON_EMPTY);
		String errorMessage = null;
		try {
			String jsonStr = objectMapper.writeValueAsString(object);	
			if (shortObject) {
				String idBeginStr = "id\":{";
				int startIdPos = jsonStr.indexOf(idBeginStr);
				int endIdPos = jsonStr.indexOf("}", startIdPos);
				jsonStr = jsonStr.substring(0, startIdPos) + idBeginStr.substring(0, idBeginStr.length() - 1) 
				    + Integer.valueOf(objectId) + jsonStr.substring(endIdPos + 1);
			}
			return jsonStr;
		} catch (JsonGenerationException e) {
			log.error("Json Generation Exception: " + e.getMessage(),e);
			errorMessage = "Json Generation Exception: " + e.getMessage() + " See error logs!";
		} catch (JsonMappingException e) {
			log.error("Json Mapping Exception: " + e.getMessage(),e);
			errorMessage = "Json Generation Exception: " + e.getMessage() + " See error logs!";
		} catch (IOException e) {
			log.error("I/O Exception: " + e.getMessage(),e);
			errorMessage = "I/O Exception: " + e.getMessage() + " See error logs!";
		}
		//Report technical errors...
		return errorMessage;
	}
		
}
