package eu.europeana.api.commons.utils;

import java.util.Map;
import java.util.TreeMap;

import org.apache.stanbol.commons.jsonld.JsonLd;
import org.apache.stanbol.commons.jsonld.JsonLdProperty;
import org.apache.stanbol.commons.jsonld.JsonLdPropertyValue;
import org.apache.stanbol.commons.jsonld.JsonLdResource;

import eu.europeana.api.commons.definitions.search.FacetFieldView;
import eu.europeana.api.commons.definitions.search.result.ResultsPage;
import eu.europeana.api.commons.definitions.vocabulary.CommonApiConstants;
import eu.europeana.api.commons.definitions.vocabulary.CommonLdConstants;

public abstract class ResultsPageSerializer<T> extends JsonLd {

	ResultsPage<T> resultsPage;
	String context;
	String type;

	public ResultsPageSerializer(ResultsPage<T> resPage) {
		this(resPage, null, null);
	}

	public ResultsPageSerializer(ResultsPage<T> resPage, String context, String type) {
		this.resultsPage = resPage;
		this.context = context;
		this.type = type;
	}

	public ResultsPage<T> getResultsPage() {
		return resultsPage;
	}

	public void setResultsPage(ResultsPage<T> resultsPage) {
		this.resultsPage = resultsPage;
	}

	public void setContext(String context) {
		this.context = context;
	}

	protected String getContext() {
		return context;
	}

	public String serialize(String profile) {

		setUseTypeCoercion(false);
		setUseCuries(false);
		setApplyNamespaces(false);
		// if(isApplyNamespaces())
		// setUsedNamespaces(namespacePrefixMap);

		JsonLdResource jsonLdResource = new JsonLdResource();
		jsonLdResource.setSubject("");
		jsonLdResource.putProperty(CommonLdConstants.AT_CONTEXT, getContext());
		// annotation page
		if (resultsPage.getCurrentPageUri() != null)
			jsonLdResource.putProperty(CommonLdConstants.ID, resultsPage.getCurrentPageUri());
		jsonLdResource.putProperty(CommonLdConstants.TYPE, getType());
		jsonLdResource.putProperty(CommonLdConstants.TOTAL, resultsPage.getTotalInPage());

		// collection
		if (resultsPage.getCurrentPageUri() != null) {
			JsonLdProperty collectionProp = new JsonLdProperty(CommonLdConstants.PART_OF);
			JsonLdPropertyValue collectionPropValue = new JsonLdPropertyValue();
			collectionPropValue.putProperty(new JsonLdProperty(CommonLdConstants.ID, resultsPage.getCollectionUri()));
			collectionPropValue.putProperty(new JsonLdProperty(CommonLdConstants.TYPE, CommonLdConstants.RESULT_LIST));
			collectionPropValue
					.putProperty(new JsonLdProperty(CommonLdConstants.TOTAL, resultsPage.getTotalInCollection()));
			collectionProp.addValue(collectionPropValue);
			jsonLdResource.putProperty(collectionProp);
		}

		// items
		serializeItems(jsonLdResource, profile);
		serializeFacets(jsonLdResource, profile);

		// nagivation
		if (resultsPage.getPrevPageUri() != null)
			jsonLdResource.putProperty(CommonLdConstants.PREV, resultsPage.getPrevPageUri());
		if (resultsPage.getNextPageUri() != null)
			jsonLdResource.putProperty(CommonLdConstants.NEXT, resultsPage.getNextPageUri());

		put(jsonLdResource);

		return toString(4);
	}

	protected void serializeFacets(JsonLdResource jsonLdResource, String profile) {
		if (getResultsPage() == null || getResultsPage().getFacetFields() == null
				|| getResultsPage().getFacetFields().isEmpty())
			return;

		JsonLdProperty facetsProperty = new JsonLdProperty(CommonApiConstants.SEARCH_RESP_FACETS);

		for (FacetFieldView view : getResultsPage().getFacetFields())
			facetsProperty.addValue(buildFacetPropertyValue(view));

		if (facetsProperty.getValues() != null && !facetsProperty.getValues().isEmpty())
			jsonLdResource.putProperty(facetsProperty);

	}

	JsonLdPropertyValue buildFacetPropertyValue(FacetFieldView view) {

		JsonLdPropertyValue facetViewEntry = new JsonLdPropertyValue();

		facetViewEntry.putProperty(new JsonLdProperty(CommonApiConstants.SEARCH_RESP_FACETS_FIELD, view.getName()));

		// only if values for facet count are available
		if (view.getValueCountMap() != null && !view.getValueCountMap().isEmpty()) {

			JsonLdProperty values = new JsonLdProperty(CommonApiConstants.SEARCH_RESP_FACETS_VALUES);
			JsonLdPropertyValue labelCountValue;
			Map<String, String> valueMap;

			for (Map.Entry<String, Long> valueCount : view.getValueCountMap().entrySet()) {
				labelCountValue = new JsonLdPropertyValue();
				valueMap = new TreeMap<String, String>();
				valueMap.put(CommonApiConstants.SEARCH_RESP_FACETS_LABEL, valueCount.getKey());
				valueMap.put(CommonApiConstants.SEARCH_RESP_FACETS_COUNT, valueCount.getValue().toString());
				labelCountValue.setValues(valueMap);

				values.addValue(labelCountValue);
			}

			facetViewEntry.putProperty(values);
		}

		return facetViewEntry;
	}

	protected abstract void serializeItems(JsonLdResource jsonLdResource, String profile);

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
