package eu.europeana.api.commons.definitions.statistics.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europeana.api.commons.definitions.statistics.Metric;
import eu.europeana.api.commons.definitions.statistics.UsageStatsFields;

import java.util.List;

@JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityMetric extends Metric {

    /**
     * Entities per Type
     */
    @JsonProperty(UsageStatsFields.ENTITIES_PER_TYPE)
    private EntityStats entitiesPerType;

    /**
     * Entities per language
     */
    @JsonProperty(UsageStatsFields.ENTITIES_PER_LANG)
    private List<EntitiesPerLanguage> entitiesPerLanguages;

    /**
     * In Europeana Entities per Type
     */
    @JsonProperty(UsageStatsFields.EUROPEANA_ENTITIES_PER_TYPE)
    private EntityStats inEuropeanaPerType;

    /**
     * In Europeana Entities per language
     */
    @JsonProperty(UsageStatsFields.EUROPEANA_ENTITIES_PER_LANG)
    private List<EntitiesPerLanguage> inEuropeanaPerLanguage;


    public List<EntitiesPerLanguage> getEntitiesPerLanguages() {
        return entitiesPerLanguages;
    }

    public void setEntitiesPerLanguages(List<EntitiesPerLanguage> entitiesPerLanguages) {
        this.entitiesPerLanguages = entitiesPerLanguages;
    }

    public EntityStats getInEuropeanaPerType() {
        return inEuropeanaPerType;
    }

    public void setInEuropeanaPerType(EntityStats inEuropeanaPerType) {
        this.inEuropeanaPerType = inEuropeanaPerType;
    }

    public List<EntitiesPerLanguage> getInEuropeanaPerLanguage() {
        return inEuropeanaPerLanguage;
    }

    public void setInEuropeanaPerLanguage(List<EntitiesPerLanguage> inEuropeanaPerLanguage) {
        this.inEuropeanaPerLanguage = inEuropeanaPerLanguage;
    }

    public EntityStats getEntitiesPerType() {
        return entitiesPerType;
    }

    public void setEntitiesPerType(EntityStats entitiesPerType) {
        this.entitiesPerType = entitiesPerType;
    }
}
