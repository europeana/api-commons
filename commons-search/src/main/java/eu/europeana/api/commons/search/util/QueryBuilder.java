package eu.europeana.api.commons.search.util;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import eu.europeana.api.commons.definitions.search.Query;
import eu.europeana.api.commons.definitions.search.impl.QueryImpl;
import eu.europeana.api.commons.definitions.utils.RandomUtils;

public class QueryBuilder {

    private static final String COLON = ":";
    //
    public static final String PARAM_QUERY_OPERATOR = "q.op";
    private static final String SORT_RANDOM = "random";

    /**
     * 
     * @param queryString  The query string e.g. "drama"
     * @param filters      query filters (qf)
     * @param facets       requested facet fields
     * @param retFields    the fields to be returned as response for the Query
     * @param sortCriteria list of sorting criteria (e.g. ["type+asc",
     *                     "derived_score+desc"]
     * @param pageNr       the requested results page
     * @param pageSize     the requested pageSize
     * @param maxPageSize  the maximum allowed page size for the given query
     * @param profile      the search profile
     * @return the completed search query
     */
    public Query buildSearchQuery(String queryString, String[] filters, String[] facets, String[] retFields,
	    String[] sortCriteria, int pageNr, int pageSize, int maxPageSize, String profile) {

	// TODO: check if needed
	// String[] normalizedFacets =
	// StringArrayUtils.splitWebParameter(facets);
	boolean isFacetsRequested = isFacetsRequest(facets);

	Query searchQuery = new QueryImpl();
	searchQuery.setQuery(queryString);
	if (pageNr < 0)
	    searchQuery.setPageNr(getDefaultStarPage());
	else
	    searchQuery.setPageNr(pageNr);

	int rows = computeValidPageSize(pageSize, maxPageSize);
	searchQuery.setPageSize(rows);

	if (isFacetsRequested)
	    searchQuery.setFacetFields(facets);

	// translateSearchFilters(filters);
	searchQuery.setFilters(filters);
	if (retFields != null) {
	    searchQuery.setViewFields(retFields);
	}
	searchQuery.setSearchProfile(profile);

	searchQuery.setSortCriteria(sortCriteria);

	return searchQuery;
    }

    protected int computeValidPageSize(int pageSize, int maxPageSize) {
	if (pageSize < 0)
	    return Query.DEFAULT_PAGE_SIZE;

	if (pageSize > maxPageSize)
	    return maxPageSize;

	return pageSize;
    }

    protected boolean isFacetsRequest(String[] facets) {
	return facets != null && facets.length > 0;
    }

    /**
     * 
     * @param filters            The array of filter values
     * @param mappedFilterFields the mapping of model (request) fields (map key) to
     *                           solr fields (map value)
     * @return the equivalent (valid) solr filters
     */
    public String[] translateSearchFilters(String[] filters, Map<String, String> mappedFilterFields) {
	if (filters == null)
	    return null;
	if (mappedFilterFields == null || mappedFilterFields.isEmpty())
	    return filters;

	String[] solrFilters = new String[filters.length];
	String filterField;
	String filterValue;
	String[] filterElem;
	int count = 0;

	for (String filter : filters) {
	    if (filter.contains(COLON)) { // TODO think of eliminating invalid filters, e.g. filter doesn't contain
					  // colon
		filterElem = filter.split(COLON);
		filterField = filterElem[0];
		filterValue = filterElem[1];

		if (!mappedFilterFields.containsKey(filterField))
		    solrFilters[count] = filter;// preserve filter
		else {
		    solrFilters[count] = mappedFilterFields.get(filterField) + COLON + filterValue;
		}
	    }
	    count++;
	}

	return solrFilters;
    }

    public SolrQuery toSolrQuery(Query searchQuery, String searchHandler) {

	SolrQuery solrQuery = new SolrQuery();

	solrQuery.setRequestHandler(searchHandler);
	solrQuery.setQuery(searchQuery.getQuery());

	solrQuery.setRows(searchQuery.getPageSize());
	solrQuery.setStart(computeSolrQueryStart(searchQuery));

	if (searchQuery.getFilters() != null)
	    solrQuery.addFilterQuery(searchQuery.getFilters());

	if (searchQuery.getFacetFields() != null) {
	    solrQuery.setFacet(true);
	    solrQuery.addFacetField(searchQuery.getFacetFields());
	    solrQuery.setFacetMinCount(Query.DEFAULT_FACET_MINCOUNT);
	    solrQuery.setFacetLimit(Query.DEFAULT_FACET_LIMIT);
	}

	if (searchQuery.getSortCriteria() != null) {
	    buildSortQuery(solrQuery, searchQuery.getSortCriteria());
	}
	if (searchQuery.getViewFields() != null) {
	    solrQuery.setFields(searchQuery.getViewFields());
	}

	return solrQuery;
    }

    public int getDefaultStarPage() {
      return Query.DEFAULT_PAGE;
    }
    
    protected int computeSolrQueryStart(Query searchQuery) {
      return (searchQuery.getPageNr() - getDefaultStarPage()) * searchQuery.getPageSize();
    }

    /**
     * This method should be implemented in subclasses (if the fieldName is not
     * allowed for sorting an Runtime Exception and the api responses must indicate
     * the request as invalid (reported with a HTTP 400)
     * 
     * @param fieldName The name of the Solr field
     */
    protected void verifySortField(String fieldName) {
	// superclasses must implement the method
    }

    /**
     * This method builds sort query string array from provided sort criteria array.
     * 
     * @param solrQuery   The Solr query
     * @param inputFields The Solr input fields intended for sorting
     */
    protected void buildSortQuery(SolrQuery solrQuery, String[] inputFields) {

	String[] inputArray;
	String fieldName;
	SolrQuery.ORDER order;

	for (String field : inputFields) {
	    inputArray = StringUtils.splitByWholeSeparator(field, " ");
	    fieldName = inputArray[0];
	    verifySortField(fieldName);
	    fieldName = processSortField(fieldName);

	    order = (inputArray.length == 2) ? SolrQuery.ORDER.valueOf(inputArray[1]) : SolrQuery.ORDER.asc;
	    solrQuery.addSort(fieldName, order);
	}
    }

    protected String processSortField(String fieldName) {
	// handle random search
	if (SORT_RANDOM.equals(fieldName)) {
	    return SORT_RANDOM + "_" + generateRandomSeed();
	}
	return fieldName;

    }

    String generateRandomSeed() {
	return RandomUtils.randomString(getSeedLength());
    }

    private int getSeedLength() {
	return 10;
    }

    /*
     * This method splits the list of values provided as concatenated string to the
     * corresponding array representation
     * 
     * @param concatenatedStrings
     * 
     * @return
     */
    public String[] toArray(String concatenatedStrings) {
	if (StringUtils.isEmpty(concatenatedStrings))
	    return null;
	String[] array = StringUtils.splitByWholeSeparator(concatenatedStrings, ",");
	return StringUtils.stripAll(array);
    }
}
