package eu.europeana.api.commons.definitions.search.impl;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;

import eu.europeana.api.commons.definitions.search.FacetFieldView;

public class FacetFieldViewImpl implements FacetFieldView{
	
	private FacetField solrFacetField; 
	private SortedMap<String, Long> valueCountMap;
	
	
	public FacetFieldViewImpl(FacetField solrFacetField) {
		this.solrFacetField = solrFacetField;
	}

	@Override
	public Map<String, Long> getValueCountMap() {
		if(solrFacetField.getValues() == null || solrFacetField.getValues().isEmpty())
			return null;
		
		if(valueCountMap == null){
			valueCountMap = new TreeMap<String, Long>();
		 
			for(Count entry: solrFacetField.getValues())
				valueCountMap.put(entry.getName(), entry.getCount());
			
		}	
		
		return valueCountMap;
	}
	

	@Override
	public String getName() {
		return solrFacetField.getName();
	}

}
