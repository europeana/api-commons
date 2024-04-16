package eu.europeana.api.commons.definitions.utils;

/**
 * Utilities for logging 
 * @author GordeaS
 *
 */
public class LoggingUtils {

  //hide default contructor
  private LoggingUtils() {}
  
  /**
   * Sanitize user input to prevent malicious code
   * @param input user input
   * @return input in which newlines are replaces by underscore
   */
  public static String sanitizeUserInput(String input) {
    return input.replaceAll("[\n\r]", " ");
  }
}
