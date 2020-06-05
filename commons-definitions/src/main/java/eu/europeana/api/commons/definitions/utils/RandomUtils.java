package eu.europeana.api.commons.definitions.utils;

import java.security.SecureRandom;

/**
 * Same as corelib RandomSeed
 * @author GordeaS
 *
 */
public class RandomUtils {

    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static SecureRandom rnd = new SecureRandom();

    public static String randomString(int len) {
	StringBuilder sb = new StringBuilder(len);
	for (int i = 0; i < len; i++) {
	    sb.append(AB.charAt(rnd.nextInt(AB.length())));
	}
	return sb.toString();
    }

}
