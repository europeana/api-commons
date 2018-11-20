package eu.europeana.api.commons.definitions.search;

public interface ResultPage<T> {

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

	void setItems(ResultSet<? extends T> items);

	ResultSet<? extends T> getItems();

	void setSearchQuery(Query searchQuery);

	Query getSearchQuery();

	void setTotalInCollection(long totalInCollection);

	long getTotalInCollection();

	void setTotalInPage(long totalInPage);

	long getTotalInPage();

}
