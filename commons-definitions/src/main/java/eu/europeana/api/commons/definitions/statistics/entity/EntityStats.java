package eu.europeana.api.commons.definitions.statistics.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europeana.api.commons.definitions.statistics.UsageStatsFields;

public class EntityStats {

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

    @JsonProperty(UsageStatsFields.ALL)
    private long all;

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

    public long getAll() {
        return all;
    }

    public void setAll(long all) {
        this.all = all;
    }

    @JsonIgnore
    public long getOverall() {
        return (this.agents + this.concepts + this.organisations + this.places + this.timespans);
    }

    public boolean entitiesAvailable() {
        return (this.agents != 0 && this.concepts != 0 && this.organisations !=0 && this.places !=0 && this.timespans !=0) ;
    }
}

