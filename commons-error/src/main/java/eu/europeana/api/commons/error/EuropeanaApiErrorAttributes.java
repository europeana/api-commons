package eu.europeana.api.commons.error;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Modifications we make to the default Spring-Boot error response. Make sure this class is loaded by Spring.
 */
@Component
public class EuropeanaApiErrorAttributes extends DefaultErrorAttributes {

    /**
     * Used by Spring to display errors with no custom handler.
     * Since we explicitly return {@link EuropeanaApiErrorResponse} on errors within controllers, this method is only invoked when
     * a request isn't handled by any controller.
     */
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions sbOptions) {
        final Map<String, Object> defaultErrorAttributes = super.getErrorAttributes(webRequest, sbOptions);

        // use LinkedHashMap to guarantee display order
        LinkedHashMap<String, Object> europeanaErrorAttributes = new LinkedHashMap<>();
        europeanaErrorAttributes.put("success", false);
        europeanaErrorAttributes.put("status", defaultErrorAttributes.get("status"));
        europeanaErrorAttributes.put("error", defaultErrorAttributes.get("error"));
        // message not shown
        europeanaErrorAttributes.put("timestamp", OffsetDateTime.now());
        // casting is safe here as webRequest will always be a HttpServletRequest
        europeanaErrorAttributes.put("path", ResponseUtils.getRequestPath((HttpServletRequest) webRequest));

        return europeanaErrorAttributes;
    }
}
