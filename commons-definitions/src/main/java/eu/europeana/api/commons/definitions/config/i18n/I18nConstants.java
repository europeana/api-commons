package eu.europeana.api.commons.definitions.config.i18n;

public interface I18nConstants {
	
	//400
	static final String EMPTY_PARAM_MANDATORY = "error.empty_param_mandatory";
	static final String INVALID_PARAM_VALUE = "error.invalid_param_value";
	
	//401
	static final String INVALID_APIKEY = "error.invalid_apikey";
	static final String INVALID_JWTTOKEN = "error.invalid_jwttoken";
	static final String EMPTY_APIKEY = "error.empty_apikey";
	static final String MISSING_APIKEY = "error.missing_apikey";
	static final String INVALID_API_NAME = "error.invalid_api_name";
	static final String OPERATION_NOT_AUTHORIZED = "error.operation_not_authorized";
	
	//404
	static final String RESOURCE_NOT_FOUND = "error.not_found";
	
	//423
	public static final String LOCKED_MAINTENANCE = "error.userset_lock_maintenance";
	
	//500
	static final String SERVER_ERROR_UNEXPECTED = "error.server_unexpected_error";
	
	static final String UNSUPPORTED_TOKEN_TYPE = "error.entity_unsupported_token_type";
	static final String INVALID_HEADER_FORMAT = "error.entity_invalid_header_format";
	static final String BASE64_DECODING_FAIL = "error.entity_base64_encoding_fail";
	static final String EXPIRATION_TIMESTAMP_NOT_VALID = "error.expiration_timestamp_not_valid";
	static final String JWT_TOKEN_ERROR = "error.jwt_token_error";	
	
	
}
