package eu.europeana.api.commons.logs;

/**
 * Created by Srishti on 14 September 2020
 */
public class LogConstants {

    private LogConstants() {
        // constructor to hide the implicit one
    }

    protected static final String HOST                   = "host";
    protected static final String DATE_FORMAT            = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    protected static final String TIME_ZONE              = "CET";
    protected static final String USER_AGENT             = "User-Agent";
    protected static final String B3                     = "b3";
    protected static final String X_VCAP_REQUEST_ID      = "x-vcap_request_id";
    protected static final String X_B3_PARENT_SPAN_ID    = "x-b3-parentspanid";
    protected static final String X_B3_SPAN_ID           = "x-b3-spanid";
    protected static final String X_B3_TRACE_ID          = "x-b3-traceid";
    protected static final String X_GLOBAL_TRANSACTION_ID = "x_global_transaction_id";
    protected static final String X_FORWARDED_FOR        = "x-forwarded-for";
    protected static final String X_FORWARDED_PROTO      = "x-forwarded-proto";
    protected static final String X_CF_ROUTE_ERROR       = "x-cf-route-error";
    protected static final String X_CF_APPLICATIONID     = "x-cf-applicationid";
    protected static final String X_CF_INSTANCEINDEX     = "x-cf-instanceindex";
    protected static final String X_REQUEST_START        = "x-request-start";
    protected static final String X_CLIENT_IP            = "x-client-ip";

}

