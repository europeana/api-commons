package eu.europeana.api.commons.definitions.search;

import java.util.List;

/**
 * taken from the corelib-definitions/corelib-search and eliminated explicit solr dependencies
 * @author Sergiu Gordea @ait
 * @author Willem-Jan Boogerd @www.eledge.net
 */
public class ResultSet<T> {

	/**
	 * The request query object
	 */
	private Query query;

	/**
	 * The list of result objects
	 */
	private List<T> results;

	/**
	 * The list of facets
	 */
	private List<FacetFieldView> facetFields;

//	/**
//	 * The query facets response
//	 */
//	private Map<String, Integer> queryFacets;

	// statistics

	/**
	 * The total number of results
	 */
	private long resultSize;

	/**
	 * The time in millisecond how long the search has been taken
	 */
	private long searchTime;

	
	//GETTERS and SETTTERS
	/**
	 * Getter method
	 * @return the list of retrieved items
	 */
	public List<T> getResults() {
		return results;
	}

	/**
	 * Setter method
	 * @param list the retrieved items
	 * @return
	 */
	public void setResults(List<T> list) {
		this.results = list;
	}

	/**
	 * Getter method
	 * @return the search query
	 */
	public Query getQuery() {
		return query;
	}

	/**
	 * Setter method
	 * @param query the search query
	 */
	public void setQuery(Query query) {
		this.query = query;
	}

	/**
	 * Getter method
	 * @return the retrieved facets
	 */
	public List<FacetFieldView> getFacetFields() {
		return facetFields;
	}

	/**
	 * Setter Method
	 * @param facetFields the retrieved facets
	 */
	public void setFacetFields(List<FacetFieldView> facetFields) {
		this.facetFields = facetFields;
	}

	/**
	 * Getter method 
	 * @return the total number of results retrieved with the given query 
	 */
	public long getResultSize() {
		return resultSize;
	}

	/**
	 * Setter method
	 * @param resultSize
	 */
	public void setResultSize(long resultSize) {
		this.resultSize = resultSize;
	}

	/**
	 * Getter method
	 * @return the time spent for searching the index in milliseconds
	 */
	public long getSearchTime() {
		return searchTime;
	}

	/**
	 * Setter method
	 * @param searchTime the time spent for searching the index in milliseconds
	 */
	public void setSearchTime(long searchTime) {
		this.searchTime = searchTime;
	}

//	/**
//	 * 
//	 * @return 
//	 */
//	public Map<String, Integer> getQueryFacets() {
//		return queryFacets;
//	}
//
//	public void setQueryFacets(Map<String, Integer> queryFacets) {
//		this.queryFacets = queryFacets;
//	}

	@Override
	public String toString() {
		return "ResultSet [query=" + query + ", results=" + results
				+ ", facetFields=" + facetFields 
				+ ", resultSize=" + resultSize + ", searchTime=" + searchTime
				+ "]";
	}

	/**
	 * indicate if the results list is empty
	 * @return
	 */
	public boolean isEmpty() {
		return getResults().isEmpty();
	}

}
