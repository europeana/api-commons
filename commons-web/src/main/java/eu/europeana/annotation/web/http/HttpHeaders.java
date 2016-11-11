package eu.europeana.annotation.web.http;


public interface HttpHeaders extends javax.ws.rs.core.HttpHeaders{

	/**
	 * 
	 * @see  <a href="http://www.w3.org/wiki/LinkHeader">W3C Link Header documentation</a>.
	 * 
	 */
	public static final String LINK = "Link";
	public static final String ALLOW = "Allow";
	public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
	public static final String PREFER = "Prefer";
	public static final String ACCEPT_POST = "Accept-Post";
	
	public static final String ALLOW_POST = "POST";
	public static final String ALLOW_GET = "GET";
	public static final String ALLOW_GPuD = "GET,PUT,DELETE";
	public static final String ALLOW_GOH = "GET,OPTIONS,HEAD";
	public static final String ALLOW_PGDOHP = "GET,OPTIONS,HEAD";
	
	public static final String CONTENT_TYPE_JSON_UTF8 = "application/json; charset=utf-8";
	public static final String CONTENT_TYPE_JSONLD_UTF8 = "application/ld+json; charset=utf-8";
	
	
	public static final String VALUE_LDP_RESOURCE = "<http://www.w3.org/ns/ldp#Resource>; rel=\"type\"";
	public static final String VALUE_LDP_CONTAINER = "<http://www.w3.org/ns/ldp#Resource>; rel=\"type\"\n"+
			"<http://www.w3.org/TR/annotation-protocol/constraints>;\n" +
			"rel=\"http://www.w3.org/ns/ldp#constrainedBy\"";
	public static final String VALUE_LDP_CONTENT_TYPE = CONTENT_TYPE_JSONLD_UTF8 + "; profile=\"http://www.w3.org/ns/anno.jsonld\"";
	public static final String VALUE_CONSTRAINTS = "<http://www.w3.org/TR/annotation-protocol/constraints>; " +
			"rel=\"http://www.w3.org/ns/ldp#constrainedBy\"";
	
	
}

