package eu.europeana.api.commons.error;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class ResponseUtils {

    private ResponseUtils() {
        // hide implicit public constructor
    }

    /**
     * Gets a String representation of an Exception's stacktrace
     *
     * @param throwable Throwable instance
     * @return String representation of stacktrace
     */
    public static String getExceptionStackTrace(Throwable throwable) {
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        throwable.printStackTrace(printWriter);
        return result.toString();
    }


    /**
     * Gets the URI path in the request, appending  any query parameters
     *
     * @param httpRequest Http request
     * @param attributeErrorPath  This is to fetch the actual path of the request from the Spring "/error"
     *                            auto configurations responses
     * @return String containing request URI and query parameterss
     */
    public static String getRequestPath(HttpServletRequest httpRequest, boolean attributeErrorPath) {
        if (attributeErrorPath && httpRequest.getAttribute("javax.servlet.error.request_uri") != null) {
            return StringUtils.substringBefore(httpRequest.getRequestURL().toString(), httpRequest.getRequestURI()) +
                    httpRequest.getAttribute("javax.servlet.error.request_uri");
        }
        return
                httpRequest.getQueryString() == null ? String.valueOf(httpRequest.getRequestURL()) :
                        String.valueOf(httpRequest.getRequestURL().append("?").append(httpRequest.getQueryString()));
    }
}
