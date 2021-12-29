package eu.europeana.api.commons.definitions.statistics.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europeana.api.commons.definitions.statistics.UsageStatsFields;

public class EntityStats {

    @JsonProperty(UsageStatsFields.TIMESPAN)
    private float timespans;

    @JsonProperty(UsageStatsFields.CONCEPT)
    private float concepts;

    @JsonProperty(UsageStatsFields.ORGANISATION)
    private float organisations;

    @JsonProperty(UsageStatsFields.AGENT)
    private float agents;

    @JsonProperty(UsageStatsFields.PLACE)
    private float places;

    @JsonProperty(UsageStatsFields.TOTAL)
    private float total;

    public float getTimespans() {
        return timespans;
    }

    public void setTimespans(float timespans) {
        this.timespans = timespans;
    }

    public float getConcepts() {
        return concepts;
    }

    public void setConcepts(float concepts) {
        this.concepts = concepts;
    }

    public float getOrganisations() {
        return organisations;
    }

    public void setOrganisations(float organisations) {
        this.organisations = organisations;
    }

    public float getAgents() {
        return agents;
    }

    public void setAgents(float agents) {
        this.agents = agents;
    }

    public float getPlaces() {
        return places;
    }

    public void setPlaces(float places) {
        this.places = places;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}

