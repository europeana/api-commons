package eu.europeana.api.commons.definitions.search.result;

import java.util.List;

import eu.europeana.api.commons.definitions.search.FacetFieldView;

public interface ResultsPage<T> {
	
	void setPrevPageUri(String prevPageUri);

	String getPrevPageUri();

	void setNextPageUri(String nextPageUri);

	String getNextPageUri();

	void setCurrentPageUri(String currentPageUri);

	String getCurrentPageUri();

	void setCollectionUri(String resultCollectionUri);

	String getCollectionUri();

	void setCurrentPage(int currentPage);

	int getCurrentPage();

	public List<T> getItems();

	public void setItems(List<T> items);

	void setTotalInCollection(long totalInCollection);

	long getTotalInCollection();

	void setTotalInPage(long totalInPage);

	long getTotalInPage();

	void setFacetFields(List<FacetFieldView> facetFields);

	List<FacetFieldView> getFacetFields();

}
