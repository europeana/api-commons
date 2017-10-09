package eu.europeana.api.commons.web.model;

public class ErrorApiResponse extends ApiResponse {

	
	public ErrorApiResponse(String apikey, String action, String errorMessage){
		super(apikey, action);
		//mark as error
		this.success = false;
		//set error message
		this.error = errorMessage;
	}
}
