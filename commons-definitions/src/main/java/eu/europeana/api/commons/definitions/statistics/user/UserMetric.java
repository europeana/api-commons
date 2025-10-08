package eu.europeana.api.commons.definitions.statistics.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europeana.api.commons.definitions.statistics.Metric;
import static eu.europeana.api.commons.definitions.statistics.UsageStatsFields.NumberOfUsers;
import static eu.europeana.api.commons.definitions.statistics.UsageStatsFields.RegisteredClients;
import static eu.europeana.api.commons.definitions.statistics.UsageStatsFields.NumberOfPersonalClients;
import static eu.europeana.api.commons.definitions.statistics.UsageStatsFields.NumberOfProjectClients;

public class UserMetric extends Metric {

    @JsonProperty(NumberOfUsers)
    private int numberOfUsers;

    @JsonProperty(RegisteredClients)
    private ClientMetric registeredClients;

    // TODO should be removed once keycloak implements new metric
    @JsonProperty(NumberOfProjectClients)
    private int numberOfProjectClients;

    // TODO should be removed once keycloak implements new metric
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

    public ClientMetric getRegisteredClients() {
        return registeredClients;
    }
}
