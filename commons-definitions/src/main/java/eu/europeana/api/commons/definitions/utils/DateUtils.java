package eu.europeana.api.commons.definitions.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static String convertDateToStr(Date date) {
    	String res = "";    	
    	DateFormat df = new SimpleDateFormat(DATE_FORMAT);
    	res = df.format(date);    	
    	return res;
    }

    public static Date convertStrToDate(String str) throws ParseException {
    	Date res = null; 
    	DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
    	res = formatter.parse(str);
    	return res;
    }
}
