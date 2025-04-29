package eu.europeana.api.commons.definitions.statistics.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europeana.api.commons.definitions.statistics.Metric;

import static eu.europeana.api.commons.definitions.statistics.UsageStatsFields.NumberOfUsers;
import static eu.europeana.api.commons.definitions.statistics.UsageStatsFields.NumberOfPersonalClients;
import static eu.europeana.api.commons.definitions.statistics.UsageStatsFields.NumberOfProjectClients;

public class UserMetric extends Metric {

    @JsonProperty(NumberOfUsers)
    private int numberOfUsers;

    @JsonProperty(NumberOfProjectClients)
    private int numberOfProjectClients;

    @JsonProperty(NumberOfPersonalClients)
    private int numberOfPersonalClients;

    public int getNumberOfUsers() {
        return numberOfUsers;
    }

    public int getNumberOfProjectClients() {
        return numberOfProjectClients;
    }

    public int getNumberOfPersonalClients() {
        return numberOfPersonalClients;
    }
}
