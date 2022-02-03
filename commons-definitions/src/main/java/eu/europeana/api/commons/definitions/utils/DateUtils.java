package eu.europeana.api.commons.definitions.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import eu.europeana.api.commons.definitions.exception.DateParsingException;

public class DateUtils {

  /**
   * @deprecated use DateTimeFormatter.ISO_INSTANT instead
   */
  @Deprecated
  public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

  public static String convertDateToStr(Date date) {

    //TODO: change to following and check if Z is included (yyyy-MM-dd'T'HH:mm:ss'Z')
    //    return DateTimeFormatter.ISO_INSTANT.format(date.toInstant());
    String res = "";
    DateFormat df = new SimpleDateFormat(DATE_FORMAT);
    res = df.format(date);
    return res;
  }

  @Deprecated
  /**
   * use {@link #parseToDate(String)} instead
   * @param str
   * @return
   * @throws DateParsingException
   */
  public static Date convertStrToDate(String str) throws DateParsingException {
    return parseToDate(str);
  }
  
  /**
   * 
   * @param isoDateTime in ISO DateTime format;"yyyy-MM-dd'T'HH:mm:ss'Z'"
   * @return
   * @throws DateParsingException
   */
  public static Date parseToDate(String isoDateTime) throws DateParsingException {
    try {
      TemporalAccessor timeAccessor = DateTimeFormatter.ISO_INSTANT.parse(isoDateTime);
      return Date.from(Instant.from(timeAccessor));
    } catch (RuntimeException e) {
      throw new DateParsingException(e);
    }
  }
  
  /**
   * 
   * @param isoDateTime in ISO DateTime format;"yyyy-MM-dd'T'HH:mm:ss'Z'"
   * @return
   * @throws DateParsingException
   */
  public static OffsetDateTime parseToOffsetDateTime(String isoDateTime) throws DateParsingException {
    try {
      TemporalAccessor timeAccessor = DateTimeFormatter.ISO_INSTANT.parse(isoDateTime);
      return OffsetDateTime.from(Instant.from(timeAccessor).atOffset(ZoneOffset.UTC));
    } catch (RuntimeException e) {
      throw new DateParsingException(e);
    }
  }
  
}
