package eu.europeana.api.commons.definitions.statistics.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import static eu.europeana.api.commons.definitions.statistics.UsageStatsFields.*;

public class ClientMetric {

    @JsonProperty(Personal)
    private int personal;

    @JsonProperty(Project)
    private int project;

    @JsonProperty(Internal)
    private int internal;

    public int getPersonal() {
        return personal;
    }

    public int getProject() {
        return project;
    }

    public int getInternal() {
        return internal;
    }
}
