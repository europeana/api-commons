package eu.europeana.api.commons.web.definitions;


/**
 * @author GrafR
 * 
 */
public interface WebFields {

	// Web application
	public static final String AND = "&";
	public static final String EQUALS = "=";
	public static final String JSON_LD_REST = ".jsonld";
	public static final String FORMAT_JSONLD = "jsonld";
	
	//
	// Validation definitions
	//
	public static final String READ_USER_URL_PREFIX = "read";
	public static final String WRITE_USER_URL_PREFIX = "write";
	public static final String DELETE_USER_URL_PREFIX = "delete";	
	public static final String DEFAULT_CREATOR_URL = "http://data.europeana.eu/user/";
}
