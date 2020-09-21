package eu.europeana.api.commons.logs;

import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
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
import java.net.InetAddress;
import java.net.UnknownHostException;
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
        long startTime = System.currentTimeMillis();
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
            long processTime = System.currentTimeMillis() - startTime;
            logRequest(request, response, handler, processTime, serverTime);
            updateResponse(response);
        }
    }

    /**
     * Forms the LogMessage
     *
     * @param requestToCache
     * @param responseToCache
     * @param handler
     */
    private void logRequest(HttpServletRequest requestToCache, HttpServletResponse responseToCache, HandlerExecutionChain handler,
                            long processTime, String serverTime) {
        LogMessage logMessage = new LogMessage();

        // TODO Have to decide if we will still set this values here
        logMessage.setApp_guid(""); // app_id

        logMessage.setApp_name(StringUtils.substringBefore(requestToCache.getHeader(LogConstants.HOST), LogConstants.HOST_SEPERATOR));
        logMessage.setBytes(getResponsePayload(responseToCache).getBytes().length);
        logMessage.setWskey(requestToCache.getParameter(LogConstants.WSKEY));
        logMessage.setPort(requestToCache.getServerPort());
        logMessage.setJavaMethod(handler.getHandler().toString());

        // get the client's Ip and Geo Location
        String clientIP = getClientIP(requestToCache);
        // TODO Verify which Ip is this exactly; for now getting IPV4
        String ipV4 = getIPV4();

        logMessage.setClientIPv4(clientIP);
        logMessage.setIpV4(ipV4);
        logMessage.setClientLocation(getGeoLocation(clientIP));
        logMessage.setLocation(getGeoLocation(ipV4));

        // TODO figure out how we will calculate this
        // Gorouter Time : is the total time it takes for the request to go through the
        // Gorouter initially plus the time it takes for the response to travel back through the Gorouter. This does not include
        // the time the request spends traversing the network to the app. This also does not include the time the
        // app spends forming a response.
        //logMessage.setGorouterTime(goRouterTime);

        logMessage.setHttpVersion(StringUtils.substringAfter(requestToCache.getProtocol(), "/"));
        logMessage.setReferer(requestToCache.getHeader(LogConstants.REFERER));
        logMessage.setHttpStatus(responseToCache.getStatus());
        logMessage.setProcessTime(processTime);
        // server date and time
        logMessage.setServerDate(StringUtils.substringBefore(serverTime, "T"));
        logMessage.setServerTime(StringUtils.substringBetween(serverTime, "T", "Z"));
        logMessage.setServerTimeZoneOffset(LogConstants.SERVER_TIMEZONE_OFFSET);

        logMessage.setUrlQuery(requestToCache.getQueryString());
        logMessage.setUrlPath(requestToCache.getPathInfo());
        logMessage.setUserAgent(requestToCache.getHeader(LogConstants.USER_AGENT));
        logMessage.setVcapRequestId(requestToCache.getHeader(LogConstants.X_VCAP_REQUEST_ID));
        logMessage.setMethod(requestToCache.getMethod());
        logMessage.setxB3parentSpanId(requestToCache.getHeader(LogConstants.X_B3_PARENT_SPAN_ID));
        logMessage.setxB3SpanId(requestToCache.getHeader(LogConstants.X_B3_SPAN_ID));
        logMessage.setxB3TraceId(requestToCache.getHeader(LogConstants.X_B3_TRACE_ID));
        logMessage.setxGlobalTransId(requestToCache.getHeader(LogConstants.X_GLOBAL_TRANSACTION_ID));
        logMessage.setCfConnectingIp(requestToCache.getHeader(LogConstants.CF_CONNECTING_IP));
        logMessage.setCfIpCountry(requestToCache.getHeader(LogConstants.CF_IP_COUNTRY));

        LOG.info(logMessage);
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
     * Gets the client's Ip first from X-FORWARDED-FOR (format : client Ip, proxy, proxy)
     * if null get the remote Address
     *
     * @param request
     * @return client's IP
     */
    private static String getClientIP(HttpServletRequest request) {
        String ipAddress = request.getHeader(LogConstants.X_FORWARDED_FOR);
        if (StringUtils.isNotEmpty(ipAddress)) {
            return ipAddress.contains(",") ? ipAddress.split(",")[0] : ipAddress;
        } else {
            return request.getRemoteAddr();
        }
    }

    /**
     * Gets the IpV4 address
     *
     * @return IPV4 address
     */
    private static String getIPV4() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            LOG.error("Unknown Host ", e);
        }
        return "";
    }

    /**
     * Gets the GeoIp Location
     * @param ipAddress the ip address to get the location
     *
     * @return GeoIp
     */
    private GeoIP getGeoLocation(String ipAddress) {
        try {
            return new RawDBGeoIPLocationService().getLocation(ipAddress);
        } catch (IOException e) {
            LOG.error("Could not load the GeoLite2-City mapping file ", e);
        } catch (AddressNotFoundException e) {
                LOG.error("Could not find the address {} in the database ", ipAddress);
            } catch (GeoIp2Exception e) {
                LOG.error("Address not found for IP {} ", ipAddress, e);
            }
        return null;
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

    private void updateResponse(HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper responseWrapper =
                WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (responseWrapper != null) {
            responseWrapper.copyBodyToResponse();
        }
    }
}
