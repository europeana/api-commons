package eu.europeana.api.commons.logs;

/**
 * Created by Srishti on 14 September 2020
 */
public class LogMessage {

    private String appName;
    private String serverDate;
    private String method;
    private String urlPath;
    private String httpVersion;
    private int httpStatus;
    private long bytes;
    private String userAgent;
    private String ipPort;
    private String cfIpPort;
    private String xForwardedFor;
    private String xForwardedProto;
    private String vcapRequestId;
    private long responseTime;
    private long gorouterTime;
    private String appId;
    private String appIndex;
    private String xCfRoutererror;
    private String xGlobalTransactionId;
    private String trueClientIp;
    private String xB3Traceid;
    private String xB3Spanid;
    private String xB3Parentspanid;
    private String b3;

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setServerDate(String serverDate) {
        this.serverDate = serverDate;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setxForwardedFor(String xForwardedFor) {
        this.xForwardedFor = xForwardedFor;
    }

    public void setxForwardedProto(String xForwardedProto) {
        this.xForwardedProto = xForwardedProto;
    }

    public void setVcapRequestId(String vcapRequestId) {
        this.vcapRequestId = vcapRequestId;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public void setGorouterTime(long gorouterTime) {
        this.gorouterTime = gorouterTime;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setAppIndex(String appIndex) {
        this.appIndex = appIndex;
    }

    public void setxCfRoutererror(String xCfRoutererror) {
        this.xCfRoutererror = xCfRoutererror;
    }

    public void setxGlobalTransactionId(String xGlobalTransactionId) {
        this.xGlobalTransactionId = xGlobalTransactionId;
    }

    public void setTrueClientIp(String trueClientIp) {
        this.trueClientIp = trueClientIp;
    }

    public void setxB3Traceid(String xB3Traceid) {
        this.xB3Traceid = xB3Traceid;
    }

    public void setxB3Spanid(String xB3Spanid) {
        this.xB3Spanid = xB3Spanid;
    }

    public void setxB3Parentspanid(String xB3Parentspanid) {
        this.xB3Parentspanid = xB3Parentspanid;
    }

    public void setB3(String b3) {
        this.b3 = b3;
    }

    public void setIpPort(String ipPort) {
        this.ipPort = ipPort;
    }

    public void setCfIpPort(String cfIpPort) {
        this.cfIpPort = cfIpPort;
    }

    @Override
    public String toString() {
        return "OUT " +
                appName + " - " +
                "[" + serverDate + "] " +
                "\"" + method + " " +
                urlPath  +
                " " + httpVersion + "\" " +
                + httpStatus + " 0 " +
                + bytes + " \'-\' " +
                "\"" + userAgent + "\" " +
                "\'" + ipPort + "\'" +
                "\'" + cfIpPort + "\'" +
                " x_forwarded_for:'" + xForwardedFor + '\'' +
                " x_forwarded_proto:'" + xForwardedProto + '\'' +
                " vcap_request_id:'" + vcapRequestId + '\'' +
                " response_time:" + responseTime +
                " gorouter_time:" + gorouterTime +
                " app_id:'" + appId + '\'' +
                " app_index:'" + appIndex + '\'' +
                " x_cf_routererror:'" + xCfRoutererror + '\'' +
                " x_global_transaction_id:'" + xGlobalTransactionId + '\'' +
                " true_client_ip:'" + trueClientIp + '\'' +
                " x_b3_traceid:'" + xB3Traceid + '\'' +
                " x_b3_spanid:'" + xB3Spanid + '\'' +
                " x_b3_parentspanid:'" + xB3Parentspanid + '\'' +
                " b3:'" + b3 + '\'';
    }
}

