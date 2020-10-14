package eu.europeana.api.commons.error;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.WebRequest;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Modifications we make to the default Spring-Boot error response. Make sure this class is loaded by Spring.
 */
@Component
public class EuropeanaApiErrorAttributes extends DefaultErrorAttributes {

    @Value("${server.error.include-stacktrace}")
    private ErrorProperties.IncludeStacktrace includeStacktrace;

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions sbOptions) {
        final Map<String, Object> errorAttributes = new LinkedHashMap<>();
        ErrorAttributeOptions options = includeStacktrace(sbOptions, webRequest);

        errorAttributes.put("success", "false");

        // Spring-Boot uses a LinkedHashMap for error attributes, so to control the order of elements and have our added
        // 'success' field first, we insert the original attributes after that.
        final Map<String, Object> sbAttributes = super.getErrorAttributes(webRequest, options);
        for (Map.Entry<String, Object> attribute : sbAttributes.entrySet()) {
            errorAttributes.put(attribute.getKey(), attribute.getValue());
        }

        addPathRequestParameters(errorAttributes, webRequest);

        addCodeFieldIfAvailable(errorAttributes, webRequest);

        return errorAttributes;
    }

    /**
     * Spring-Boot uses the "trace" parameter to include a field with stacktrace data (see also application.yml)
     * We do the same when a "debug" parameter is provided (if
     */
    private ErrorAttributeOptions includeStacktrace(ErrorAttributeOptions originalOptions, WebRequest request) {
        if (ErrorProperties.IncludeStacktrace.ON_PARAM.equals(includeStacktrace) &&
                request.getParameter("debug") != null) {
            return originalOptions.including(ErrorAttributeOptions.Include.STACK_TRACE);
        }
        return originalOptions;
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
            if (!StringUtils.isEmpty(paramValue)) {
                s.append("=").append(paramValue);
            }
        }

        if (s.length() > 0) {
            errorAttributes.put("path", errorAttributes.get("path") + s.toString());
        }
    }

    /**
     * If the error is an AbstractApiException and contains an error code, we add that to the error
     */
    private void addCodeFieldIfAvailable(Map<String, Object> errorAttributes, WebRequest webRequest) {
        final Throwable throwable = super.getError(webRequest);
        if (throwable instanceof EuropeanaApiException) {
            EuropeanaApiException apiException = (EuropeanaApiException) throwable;
            if (!StringUtils.isEmpty(apiException.getErrorCode())) {
                errorAttributes.put("code", apiException.getErrorCode());
            }
        }
    }

}
