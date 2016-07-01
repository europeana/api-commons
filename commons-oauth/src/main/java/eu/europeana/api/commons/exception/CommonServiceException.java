package eu.europeana.api.commons.exception;

/**
 * This is the base exception class for common services exceptions  
 * @author Sergiu Gordea 
 *
 */
public class CommonServiceException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6895963160368650224L;
	
	public CommonServiceException(String message){
		super(message);
	}
	
	public CommonServiceException(String message, Throwable th){
		super(message, th);
	}
	
	
}
