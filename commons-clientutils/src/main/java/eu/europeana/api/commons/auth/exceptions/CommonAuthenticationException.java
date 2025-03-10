package eu.europeana.api.commons.auth.exceptions;

public class CommonAuthenticationException extends Exception {

    private static final long serialVersionUID = 8496589899526050812L;

    public CommonAuthenticationException(String message){
        super(message);
    }

    public CommonAuthenticationException(String message, Throwable th){
        super(message, th);
    }

}
