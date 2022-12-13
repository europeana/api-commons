package eu.europeana.api.commons.definitions.statistics.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import eu.europeana.api.commons.definitions.statistics.UsageStatsFields;

@JsonPropertyOrder({UsageStatsFields.AGENT, UsageStatsFields.CONCEPT, UsageStatsFields.ORGANISATION, UsageStatsFields.PLACE,
        UsageStatsFields.TIMESPAN, UsageStatsFields.OVERALL})
public class LinkedItemMetric {

    @JsonProperty(UsageStatsFields.TIMESPAN)
    private long timespans;

    @JsonProperty(UsageStatsFields.CONCEPT)
    private long concepts;

    @JsonProperty(UsageStatsFields.ORGANISATION)
    private long organisations;

    @JsonProperty(UsageStatsFields.AGENT)
    private long agents;

    @JsonProperty(UsageStatsFields.PLACE)
    private long places;

    @JsonProperty(UsageStatsFields.OVERALL)
    private long total;

    public long getTimespans() {
        return timespans;
    }

    public void setTimespans(long timespans) {
        this.timespans = timespans;
    }

    public long getConcepts() {
        return concepts;
    }

    public void setConcepts(long concepts) {
        this.concepts = concepts;
    }

    public long getOrganisations() {
        return organisations;
    }

    public void setOrganisations(long organisations) {
        this.organisations = organisations;
    }

    public long getAgents() {
        return agents;
    }

    public void setAgents(long agents) {
        this.agents = agents;
    }

    public long getPlaces() {
        return places;
    }

    public void setPlaces(long places) {
        this.places = places;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    @JsonIgnore
    public long getOverallTotal() {
        return this.places + this.agents + this.concepts + this.organisations + this.timespans;
    }
}
