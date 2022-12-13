package eu.europeana.api.commons.definitions.statistics.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europeana.api.commons.definitions.statistics.Metric;
import eu.europeana.api.commons.definitions.statistics.UsageStatsFields;

@JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchMetric extends Metric {

    /**
     * Items linked to entities per type
     */
    @JsonProperty(UsageStatsFields.ITEMS_LINKED_TO_ENTITIES)
    private LinkedItemMetric itemsLinkedToEntities;

    public LinkedItemMetric getItemsLinkedToEntities() {
        return itemsLinkedToEntities;
    }

    public void setItemsLinkedToEntities(LinkedItemMetric itemsLinkedToEntities) {
        this.itemsLinkedToEntities = itemsLinkedToEntities;
    }
}
