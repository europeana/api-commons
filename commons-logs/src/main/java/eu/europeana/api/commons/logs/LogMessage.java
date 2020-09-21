package eu.europeana.api.commons.logs;

/**
 * Created by Srishti on 14 September 2020
 */
public class LogMessage {

    private String app_guid;
    private String app_name;
    private long bytes;
    private GeoIP clientLocation;
    private String clientIPv4;
    private GeoIP location;
    private String wskey;
    private long gorouterTime;
    private String httpVersion;
    private int port;
    private String ipV4;
    private long processTime;
    private String referer;
    private int httpStatus;
    private String serverDate;
    private String serverTime;
    private String serverTimeZoneOffset;
    private String type;
    private String urlQuery;
    private String urlPath;
    private String userAgent;
    private String vcapRequestId;
    private String method;
    private String xB3parentSpanId;
    private String xB3SpanId;
    private String xB3TraceId;
    private String xGlobalTransId;

    public void setApp_guid(String app_guid) {
        this.app_guid = app_guid;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    public void setClientLocation(GeoIP clientLocation) {
        this.clientLocation = clientLocation;
    }

    public void setClientIPv4(String clientIPv4) {
        this.clientIPv4 = clientIPv4;
    }

    public void setLocation(GeoIP location) {
        this.location = location;
    }

    public void setGorouterTime(long gorouterTime) {
        this.gorouterTime = gorouterTime;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public void setIpV4(String ipV4) {
        this.ipV4 = ipV4;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setProcessTime(long processTime) {
        this.processTime = processTime;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void setServerDate(String serverDate) {
        this.serverDate = serverDate;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public void setServerTimeZoneOffset(String serverTimeZoneOffset) {
        this.serverTimeZoneOffset = serverTimeZoneOffset;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrlQuery(String urlQuery) {
        this.urlQuery = urlQuery;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setVcapRequestId(String vcapRequestId) {
        this.vcapRequestId = vcapRequestId;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setxB3parentSpanId(String xB3parentSpanId) {
        this.xB3parentSpanId = xB3parentSpanId;
    }

    public void setxB3SpanId(String xB3SpanId) {
        this.xB3SpanId = xB3SpanId;
    }

    public void setxB3TraceId(String xB3TraceId) {
        this.xB3TraceId = xB3TraceId;
    }

    public void setxGlobalTransId(String xGlobalTransId) {
        this.xGlobalTransId = xGlobalTransId;
    }

    public void setWskey(String wskey) {
        this.wskey = wskey;
    }

    @Override
    public String toString() {
        return "LogMessage{" + "\n" +
                "app_guid='" + app_guid + '\'' + "\n" +
                ", app_name='" + app_name + '\'' +"\n" +
                ", bytes='" + bytes + '\'' +"\n" +
                ", clientLocation=" + clientLocation +"\n" +
                ", clientIPv4='" + clientIPv4 + '\'' +"\n" +
                ", location=" + location +"\n" +
                ", gorouterTime='" + gorouterTime + '\'' +"\n" +
                ", httpVersion='" + httpVersion + '\'' +"\n" +
                ", port=" + port +"\n" +
                ", ipV4='" + ipV4 + '\'' +"\n" +
                ", processTime='" + processTime + '\'' +"\n" +
                ", referer='" + referer + '\'' +"\n" +
                ", httpStatus=" + httpStatus +"\n" +
                ", serverDate=" + serverDate +"\n" +
                ", serverTime='" + serverTime + '\'' +"\n" +
                ", serverTimeZoneOffset='" + serverTimeZoneOffset + '\'' +"\n" +
                ", type='" + type + '\'' +"\n" +
                ", urlQuery='" + urlQuery + '\'' +"\n" +
                ", urlPath='" + urlPath + '\'' +"\n" +
                ", userAgent='" + userAgent + '\'' +"\n" +
                ", vcapRequestId='" + vcapRequestId + '\'' +"\n" +
                ", method='" + method + '\'' +"\n" +
                ", xB3parentSpanId='" + xB3parentSpanId + '\'' +"\n" +
                ", xB3SpanId='" + xB3SpanId + '\'' +"\n" +
                ", xB3TraceId='" + xB3TraceId + '\'' +"\n" +
                ", global trabs id ='" + xGlobalTransId + '\'' +"\n" +
                ", wskey ='" + wskey + '\'' +"\n" +

                '}';
    }
}

