package eu.europeana.api.commons.exception;

/**
 * This is the base exception class for (uncaught) common services runtime exceptions
 * @author Sergiu Gordea 
 *
 */
public class CommonServiceRuntimeException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6895963160368650224L;
	
	public CommonServiceRuntimeException(String message){
		super(message);
	}
	
	public CommonServiceRuntimeException(String message, Throwable th){
		super(message, th);
	}
	
	
}
