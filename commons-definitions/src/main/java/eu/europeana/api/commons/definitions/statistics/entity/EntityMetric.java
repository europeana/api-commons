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

    @JsonProperty(UsageStatsFields.ENTITIES_PER_LANG_TYPE)
    private List<EntitiesPerLanguage> entities;

    public List<EntitiesPerLanguage> getEntities() {
        return entities;
    }

    public void setEntities(List<EntitiesPerLanguage> entities) {
        this.entities = entities;
    }
}
