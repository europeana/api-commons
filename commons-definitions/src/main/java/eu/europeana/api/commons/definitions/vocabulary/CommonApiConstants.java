package eu.europeana.api.commons.definitions.vocabulary;

public interface CommonApiConstants {

    /**
     * PARAMS
     */
    public static final String PARAM_INCLUDE_ERROR_STACK = "includeErrorStack";
    public static final String PARAM_WSKEY = "wskey";

    /**
     * Search PARAMS
     */
    public static final String QUERY_PARAM_TEXT = "text";
    public static final String QUERY_PARAM_LANGUAGE = "language";
    public static final String QUERY_PARAM_LANG = "lang";

    public static final String QUERY_PARAM_QUERY = "query";
    public static final String QUERY_PARAM_QF = "qf";

    public static final String QUERY_PARAM_SORT = "sort";
    public static final String QUERY_PARAM_PAGE = "page";
    public static final String QUERY_PARAM_PAGE_SIZE = "pageSize";
    
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_PAGE_SIZE = 10;
    

    // row based pagination used in search&record API
    public static final String QUERY_PARAM_ROWS = "rows";
    public static final String RESULT_LIMIT = "limit";

    //views
    public static final String QUERY_PARAM_PROFILE = "profile";
    public static final String QUERY_PARAM_FACET = "facet";
    public static final String FL = "fl";

    public static final String PROFILE_MINIMAL = "minimal";
    public static final String PROFILE_STANDARD = "standard";

    /**
     * Search API response
     */
    public static final String SEARCH_RESP_FACETS = "facets";
    public static final String SEARCH_RESP_FACETS_FIELD = "field";
    public static final String SEARCH_RESP_FACETS_VALUES = "values";
    public static final String SEARCH_RESP_FACETS_LABEL = "label";
    public static final String SEARCH_RESP_FACETS_COUNT = "count";

}
