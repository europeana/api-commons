package eu.europeana.api.commons.logs;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Srishti on 14 September 2020
 */
public class LoggableDispatcherServlet extends DispatcherServlet {

    private static final Logger LOG = LogManager.getLogger(LoggableDispatcherServlet.class);
    private static final int BUFFERLENGTH = 5120;

    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long appRequestTime = System.currentTimeMillis();
        String serverTime = getCurrentDateTime();

        if (!(request instanceof ContentCachingRequestWrapper)) {
            request = new ContentCachingRequestWrapper(request);
        }
        if (!(response instanceof ContentCachingResponseWrapper)) {
            response = new ContentCachingResponseWrapper(response);
        }
        HandlerExecutionChain handler = null;
        try {
            handler = getHandler(request);
        } catch (Exception e) {
            LOG.error("Exception occurred while retrieving HandlerExecutionChain from HttpServletRequest", e);
        }

        try {
            super.doDispatch(request, response);
        } catch (Exception e) {
            LOG.error("Exception occurred while invoking DispatcherServlet", e);
        } finally {
            long processTime = System.currentTimeMillis() - appRequestTime;
            long bytes = getResponsePayload(response).getBytes().length;
            // time for the response to travel back through the router
            long start = System.currentTimeMillis();
            updateResponse(response);
            long goRouterTime = (System.currentTimeMillis() - start) + getGoRouterInitialTime(request, appRequestTime);
            logRequest(request, response, processTime, goRouterTime, serverTime, bytes);

        }
    }

    /**
     * Forms the LogMessage
     *
     * @param request
     * @param response
     */
    private void logRequest(HttpServletRequest request, HttpServletResponse response, long processTime,
                            long goRouterTime, String serverTime, long bytes) {
        LogMessage logMessage = new LogMessage();

        logMessage.setAppName(request.getHeader(LogConstants.HOST));
        logMessage.setServerDate(serverTime);
        logMessage.setMethod(request.getMethod());
        logMessage.setUrlPath(getFullURL(request));
        logMessage.setHttpVersion(request.getProtocol());
        logMessage.setHttpStatus(response.getStatus());
        logMessage.setBytes(bytes);
        logMessage.setUserAgent(request.getHeader(LogConstants.USER_AGENT));

        String ip = getXForwardedForIp(request,true);
        ip.concat(":"+ request.getHeader(LogConstants.X_FORWARDED_PORT));
        logMessage.setIpPort(ip);

        // TODO : how to get IBM CF IP and port
        logMessage.setCfIpPort("-");
        logMessage.setxForwardedFor(request.getHeader(LogConstants.X_FORWARDED_FOR));
        logMessage.setxForwardedProto(request.getHeader(LogConstants.X_FORWARDED_PROTO));
        logMessage.setVcapRequestId(request.getHeader(LogConstants.X_VCAP_REQUEST_ID));
        logMessage.setResponseTime(processTime);
        logMessage.setGorouterTime(goRouterTime);
        logMessage.setAppId(request.getHeader(LogConstants.X_CF_APPLICATIONID));
        logMessage.setAppIndex(request.getHeader(LogConstants.X_CF_INSTANCEINDEX));
        logMessage.setxCfRoutererror(request.getHeader(LogConstants.X_CF_ROUTE_ERROR));
        logMessage.setxGlobalTransactionId(request.getHeader(LogConstants.X_GLOBAL_TRANSACTION_ID));
        logMessage.setTrueClientIp(getClientIP(request));
        logMessage.setxB3Traceid(request.getHeader(LogConstants.X_B3_TRACE_ID));
        logMessage.setxB3Spanid(request.getHeader(LogConstants.X_B3_SPAN_ID));
        logMessage.setxB3Parentspanid(request.getHeader(LogConstants.X_B3_PARENT_SPAN_ID));
        logMessage.setB3(request.getHeader(LogConstants.B3));
        LOG.info(logMessage);
    }

    /**
     * Calculates the total time it takes for the request to go through
     * the Gorouter initially
     * Difference of x_request_start : the time when router receives the request
     * and the startTime ( the time when application receives the request)
     * <p>
     * x_request_start : When a request comes in to the router, the routing layer
     * adds a header called X-Request-Start that is a timestamp of when the request was first received.
     * For Nginx 1.2.6 or higher : the time is in milliseconds
     *
     * @param request
     * @param appRequestTime the time application receives the request
     * @return long value
     */
    private static long getGoRouterInitialTime(HttpServletRequest request, long appRequestTime) {
        String requestStartTime = request.getHeader(LogConstants.X_REQUEST_START);
        if (StringUtils.isNotEmpty(requestStartTime)) {
            return appRequestTime - parseLong(requestStartTime);
        }
        return 0L;
    }

    /**
     * Converts String value to long
     *
     * @param value String value to be converted
     * @return long value
     */
    private static long parseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            LOG.error("NumberFormat Exception while parsing {} to long ", value, e);
        }
        return 0L;
    }

    /**
     * Gets the complete url
     *
     * @return full url
     */
    private static String getFullURL(HttpServletRequest request) {
        StringBuilder requestURL = new StringBuilder(request.getRequestURI());
        String queryString = request.getQueryString();
        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }

    /**
     * Gets the current date and time in yyyy-MM-dd'T'HH:mm:ss.SSS'Z' format
     *
     * @return date in yyyy-MM-dd'T'HH:mm:ss.SSS'Z' format
     */
    private String getCurrentDateTime() {
        DateFormat df = new SimpleDateFormat(LogConstants.DATE_FORMAT, Locale.US);
        df.setTimeZone(TimeZone.getTimeZone(LogConstants.TIME_ZONE));
        return df.format(new Date());
    }

    /**
     * Gets the client's Ip first from x-client-ip,
     * if empty fetch from x-forwarded-for (format : client Ip, proxy, proxy)
     * if null get the remote Address
     *
     * @param request
     * @return client's IP
     */
    private static String getClientIP(HttpServletRequest request) {
        String ipAddress = request.getHeader(LogConstants.X_CLIENT_IP);
        if (StringUtils.isNotEmpty(ipAddress)) {
            return ipAddress;
        } else {
            return getXForwardedForIp(request,false);
        }
    }

    /**
     * Gets the Ip address from x-forwarded-for (format : client Ip, proxy, proxy)
     * if proxy is true will fetch the last proxy
     *
     * @param request
     * @return IP or proxy
     */
    private static String getXForwardedForIp(HttpServletRequest request, boolean proxy) {
        String ipAddress = request.getHeader(LogConstants.X_FORWARDED_FOR);
        if (StringUtils.isNotEmpty(ipAddress) && ipAddress.contains(",")) {
            String [] addresses = ipAddress.split(",");
            if (proxy) {
                return addresses[addresses.length-1];
            } else {
                return addresses[0];
            }
        }
        return  "-";
    }



    /**
     * Gets the response
     *
     * @param response
     * @return the response
     */
    private String getResponsePayload(HttpServletResponse response) {
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                int length = Math.min(buf.length, BUFFERLENGTH);
                try {
                    return new String(buf, 0, length, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException ex) {
                    LOG.error("Unsupported encoding encountered while retrieving response payload from HttpServletResponse", ex);
                }
            }
        }
        return "[unknown]";
    }

    private void updateResponse(HttpServletResponse response) {
        ContentCachingResponseWrapper responseWrapper =
                WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (responseWrapper != null) {
            try {
                responseWrapper.copyBodyToResponse();
            } catch (IOException e) {
                LOG.error("Error occurred while updating the response ", e);
            }
        }
    }
}
