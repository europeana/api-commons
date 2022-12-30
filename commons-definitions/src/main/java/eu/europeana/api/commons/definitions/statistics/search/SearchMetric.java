package eu.europeana.api.commons.definitions.statistics.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import eu.europeana.api.commons.definitions.statistics.Metric;
import eu.europeana.api.commons.definitions.statistics.UsageStatsFields;

@JsonPropertyOrder({UsageStatsFields.TYPE, UsageStatsFields.CREATED, UsageStatsFields.ITEMS_LINKED_TO_ENTITIES, UsageStatsFields.ALL_RECORDS,
        UsageStatsFields.NON_COMPLAINT_RECORDS, UsageStatsFields.ALL_COMPLAINT_RECORDS, UsageStatsFields.HIGH_QUALITY_DATA,
        UsageStatsFields.HIGH_QUALITY_CONTENT, UsageStatsFields.HIGH_QUALITY_RESUABLE_CONTENT, UsageStatsFields.HIGH_QUALITY_METADATA
})
@JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchMetric extends Metric {

    /**
     * Items linked to entities per type
     */
    @JsonProperty(UsageStatsFields.ITEMS_LINKED_TO_ENTITIES)
    private LinkedItemMetric itemsLinkedToEntities;

    /**
     * Break down of all records per type
     */
    @JsonProperty(UsageStatsFields.ALL_RECORDS)
    private HighQualityMetric allRecords;

    /**
     * Break down of Tier 0 content per type
     */
    @JsonProperty(UsageStatsFields.NON_COMPLAINT_RECORDS)
    private HighQualityMetric nonCompliantRecord;

    /**
     * Break down of all records (excl. content tier 0) per type
     */
    @JsonProperty(UsageStatsFields.ALL_COMPLAINT_RECORDS)
    private HighQualityMetric allCompliantRecords;

    /**
     * Break down of Tier 2+ material in Tier A+ per type
     */
    @JsonProperty(UsageStatsFields.HIGH_QUALITY_DATA)
    private HighQualityMetric highQualityData;

    /**
     * Break down of Tier 2 + material per type
     */
    @JsonProperty(UsageStatsFields.HIGH_QUALITY_CONTENT)
    private HighQualityMetric highQualityContent;

    /**
     * Break down of Tier 3+ material per type
     */
    @JsonProperty(UsageStatsFields.HIGH_QUALITY_RESUABLE_CONTENT)
    private HighQualityMetric highQualityReusableContent;

    /**
     * Break down of Tier A+ material per type
     */
    @JsonProperty(UsageStatsFields.HIGH_QUALITY_METADATA)
    private HighQualityMetric highQualityMetadata;


    public LinkedItemMetric getItemsLinkedToEntities() {
        return itemsLinkedToEntities;
    }

    public void setItemsLinkedToEntities(LinkedItemMetric itemsLinkedToEntities) {
        this.itemsLinkedToEntities = itemsLinkedToEntities;
    }

    public HighQualityMetric getAllRecords() {
        return allRecords;
    }

    public void setAllRecords(HighQualityMetric allRecords) {
        this.allRecords = allRecords;
    }

    public HighQualityMetric getNonCompliantRecord() {
        return nonCompliantRecord;
    }

    public void setNonCompliantRecord(HighQualityMetric nonCompliantRecord) {
        this.nonCompliantRecord = nonCompliantRecord;
    }

    public HighQualityMetric getAllCompliantRecords() {
        return allCompliantRecords;
    }

    public void setAllCompliantRecords(HighQualityMetric allCompliantRecords) {
        this.allCompliantRecords = allCompliantRecords;
    }

    public HighQualityMetric getHighQualityData() {
        return highQualityData;
    }

    public void setHighQualityData(HighQualityMetric highQualityData) {
        this.highQualityData = highQualityData;
    }

    public HighQualityMetric getHighQualityContent() {
        return highQualityContent;
    }

    public void setHighQualityContent(HighQualityMetric highQualityContent) {
        this.highQualityContent = highQualityContent;
    }

    public HighQualityMetric getHighQualityReusableContent() {
        return highQualityReusableContent;
    }

    public void setHighQualityReusableContent(HighQualityMetric highQualityReusableContent) {
        this.highQualityReusableContent = highQualityReusableContent;
    }

    public HighQualityMetric getHighQualityMetadata() {
        return highQualityMetadata;
    }

    public void setHighQualityMetadata(HighQualityMetric highQualityMetadata) {
        this.highQualityMetadata = highQualityMetadata;
    }
}
