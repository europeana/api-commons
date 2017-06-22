package eu.europeana.api.commons.config.i18n;

public interface I18nConstants {
	
	// shared error messages
	static final String INVALID_APIKEY = "error.entity_invalid_apikey";
	
	// entity-api
	//401
	static final String NO_APPLICATION_FOR_APIKEY = "error.entity_no_application_for_apikey";
	static final String EMPTY_APIKEY = "error.entity_empty_apikey";
	static final String APIKEY_FILE_NOT_FOUND = "error.entity_apikey_file_not_found";
	
	//404
	static final String URI_NOT_FOUND = "error.entity_uri_not_found";
	static final String CANT_FIND_BY_SAME_AS_URI = "error.entity_same_as_not_found";
	static final String UNSUPPORTED_ENTITY_TYPE = "erorr.entity_unsupported_type";
	
	//500
	static final String SERVER_ERROR_CANT_RETRIEVE_URI = "error.entity_server_cannot_retrieve_uri";
	static final String SERVER_ERROR_CANT_RESOLVE_SAME_AS_URI = "error.entity_server_cannot_resolve_uri";
	static final String SERVER_ERROR_UNEXPECTED = "error.entity_server_unexpected_error";
	
	// annotation-api
	static final String ANNOTATION_NOT_FOUND = "error.annotation_not_found";
	static final String OPERATION_NOT_AUTHORIZED = "error.annotation_operation_not_authorized";
	static final String CLIENT_NOT_AUTHORIZED = "error.annotation_client_not_authorized";
	static final String INVALID_TOKEN = "error.annotation_invalid_token";
	static final String USER_NOT_AUTHORIZED = "error.annotation_user_not_authorized";
	static final String ANNOTATION_NOT_ACCESSIBLE = "error.annotation_not_accessible";
	static final String ANNOTATION_INVALID_BODY = "error.annotation_invalid_body";
	static final String ANNOTATION_CANT_PARSE_BODY = "error.annotation_cant_parse_body";
	static final String API_WRITE_LOCK = "error.annotation_write_lock";
	static final String ANNOTATION_VALIDATION_ID = "error.annotation_validation_id";
	static final String ANNOTATION_VALIDATION_RESOURCE = "error.annotation_validation_resource";
	
}
