package eu.europeana.api.commons.error;

import static eu.europeana.api.commons.error.EuropeanaApiErrorResponse.CONTEXT;
import java.time.OffsetDateTime;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.WebRequest;

/**
 * Modifications we make to the default Spring-Boot error response. Make sure this class is loaded by Spring.
 */
@Component
public class EuropeanaApiErrorAttributes extends DefaultErrorAttributes {

    @Value("${server.error.see-also:}")    
    private String seeAlso;
  
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
        europeanaErrorAttributes.put("@context", CONTEXT);
        europeanaErrorAttributes.put("type", "ErrorResponse");
        europeanaErrorAttributes.put("success", false);
        europeanaErrorAttributes.put("status", defaultErrorAttributes.get("status"));
        //code only available for internal API errors 
        europeanaErrorAttributes.put("error", defaultErrorAttributes.get("error"));
        europeanaErrorAttributes.put("message", defaultErrorAttributes.get("message"));
        //to be enabled when the URLs are available, eventually through appllication configuration
        if(StringUtils.hasLength(seeAlso)) {
          europeanaErrorAttributes.put("seeAlso", seeAlso);   
        }
        // message not shown
        europeanaErrorAttributes.put("timestamp", OffsetDateTime.now());
        addPathRequestParameters(europeanaErrorAttributes, webRequest);
        return europeanaErrorAttributes;
    }


    /**
     * Spring errors only return the error path and not the parameters, so we add those ourselves.
     * The original parameter string is not available in WebRequest so we rebuild it.
     */
    private void addPathRequestParameters(Map<String, Object> errorAttributes, WebRequest webRequest) {
        Iterator<String> it = webRequest.getParameterNames();
        StringBuilder s = new StringBuilder();
        while (it.hasNext()) {
            if (s.length() == 0) {
                s.append('?');
            } else {
                s.append("&");
            }
            String paramName = it.next();
            s.append(paramName);
            String paramValue = webRequest.getParameter(paramName);
            if (StringUtils.hasText(paramValue)) {
                s.append("=").append(paramValue);
            }
        }

        if (s.length() > 0) {
            errorAttributes.put("path", errorAttributes.get("path") + s.toString());
        }
    }
}
