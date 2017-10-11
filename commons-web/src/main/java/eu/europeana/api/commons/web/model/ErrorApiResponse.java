package eu.europeana.api.commons.web.model;

public class ErrorApiResponse extends ApiResponse {

	
	public ErrorApiResponse(String apiKey, String action, String errorMessage){
		super(apiKey, action);
		//mark as error
		this.success = false;
		//set error message
		this.error = errorMessage;
	}
}
