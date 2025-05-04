package eu.europeana.api.commons.definitions.statistics.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import eu.europeana.api.commons.definitions.statistics.UsageStatsFields;

@JsonPropertyOrder({UsageStatsFields.AGENT, UsageStatsFields.CONCEPT, UsageStatsFields.ORGANISATION, UsageStatsFields.PLACE,
        UsageStatsFields.TIMESPAN})
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

    @JsonProperty(UsageStatsFields.ALL)
    private float all;

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

    public float getAll() {
        return all;
    }

    public void setAll(float all) {
        this.all = all;
    }

    @JsonIgnore
    public float getOverall() {
        return (this.agents + this.concepts + this.organisations + this.places + this.timespans);
    }

    public boolean entitiesAvailable() {
        return (this.agents != 0 || this.concepts != 0 || this.organisations !=0 || this.places !=0 || this.timespans !=0) ;
    }
}

