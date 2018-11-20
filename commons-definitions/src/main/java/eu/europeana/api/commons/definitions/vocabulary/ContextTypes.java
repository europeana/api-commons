package eu.europeana.api.commons.definitions.vocabulary;

public enum ContextTypes implements JsonKeyword{
	ANNO(CommonLdConstants.WA_CONTEXT), 
	EDM(CommonLdConstants.EDM_CONTEXT),
	ENTITY(CommonLdConstants.ENTITY_CONTEXT);
	
	private String jsonValue;

	ContextTypes(String jsonValue){
		this.jsonValue = jsonValue; 
	}

	@Override
	public String getJsonValue() {
		return jsonValue;
	}	

	public boolean hasName(String name) {
		return this.name().equalsIgnoreCase(name);
	}	
	
	public boolean hasJsonValue(String jsonValue) {
		return this.getJsonValue().equalsIgnoreCase(jsonValue);
	}
	
	public static ContextTypes  valueOfJsonValue(String jsonValue) {

	    for (ContextTypes ct : ContextTypes.values()) {
	        if (ct.getJsonValue().equalsIgnoreCase(jsonValue)) {
	            return ct;
	        }
	    }

	    throw new IllegalArgumentException("Unknown/unsurported keyword. Cannot instantiate the attribute using the keyword: " + jsonValue);
	}	
	
}
