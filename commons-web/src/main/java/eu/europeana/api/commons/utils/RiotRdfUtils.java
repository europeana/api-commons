package eu.europeana.api.commons.utils;

import org.apache.jena.riot.lang.ReaderRIOTRDFXML;
import java.lang.reflect.Field;

public class RiotRdfUtils {

    /** set the field 'errorForSpaceInURI' to false.
     * This to overcome th error of spaces in URLs in the record data. See: EA-2066
     * using reflection to disable the validation of the field.
     */
    public static void disableErrorForSpaceURI() throws NoSuchFieldException, IllegalAccessException {
     Field f = ReaderRIOTRDFXML.class.getDeclaredField("errorForSpaceInURI");
     f.setAccessible(true);
     f.set(null, false);
    }
}
