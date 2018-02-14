package eu.europeana.api.commons.definitions.search.result.impl;

import java.util.List;

import eu.europeana.api.commons.definitions.search.FacetFieldView;
import eu.europeana.api.commons.definitions.search.result.ResultsPage;

public class ResultsPageImpl<T> implements ResultsPage<T>{

		private long totalInPage;
		private long totalInCollection;
		private List<T> items;
		/**
		 * The list of facets
		 */
		private List<FacetFieldView> facetFields;
		
		//pagination
		private int currentPage;
		private String resultCollectionUri;
		private String currentPageUri;
		private String nextPageUri;
		private String prevPageUri;
		
		
		public ResultsPageImpl (){
		}
		
		public List<T> getItems() {
			return items;
		}


		public void setItems(List<T> items) {
			this.items = items;
		}

		@Override
		public int getCurrentPage() {
			return currentPage;
		}
		@Override
		public void setCurrentPage(int currentPage) {
			this.currentPage = currentPage;
		}
		@Override
		public String getCollectionUri() {
			return resultCollectionUri;
		}
		@Override
		public void setCollectionUri(String resultCollectionUri) {
			this.resultCollectionUri = resultCollectionUri;
		}
		@Override
		public String getCurrentPageUri() {
			return currentPageUri;
		}
		@Override
		public void setCurrentPageUri(String currentPageUri) {
			this.currentPageUri = currentPageUri;
		}
		@Override
		public String getNextPageUri() {
			return nextPageUri;
		}
		@Override
		public void setNextPageUri(String nextPageUri) {
			this.nextPageUri = nextPageUri;
		}
		@Override
		public String getPrevPageUri() {
			return prevPageUri;
		}
		@Override
		public void setPrevPageUri(String prevPageUri) {
			this.prevPageUri = prevPageUri;
		}

		@Override
		public long getTotalInPage() {
			return totalInPage;
		}

		@Override
		public void setTotalInPage(long totalInPage) {
			this.totalInPage = totalInPage;
		}

		@Override
		public long getTotalInCollection() {
			return totalInCollection;
		}

		@Override
		public void setTotalInCollection(long totalInCollection) {
			this.totalInCollection = totalInCollection;
		}

		@Override
		public List<FacetFieldView> getFacetFields() {
			return facetFields;
		}

		@Override
		public void setFacetFields(List<FacetFieldView> facetFields) {
			this.facetFields = facetFields;
		}

		public String getResultCollectionUri() {
			return resultCollectionUri;
		}

		public void setResultCollectionUri(String resultCollectionUri) {
			this.resultCollectionUri = resultCollectionUri;
		}
		
}
