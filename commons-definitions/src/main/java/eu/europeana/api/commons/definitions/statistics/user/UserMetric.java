package eu.europeana.api.commons.definitions.statistics.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europeana.api.commons.definitions.statistics.Metric;
import static eu.europeana.api.commons.definitions.statistics.UsageStatsFields.NumberOfUsers;
import static eu.europeana.api.commons.definitions.statistics.UsageStatsFields.RegisteredClients;

public class UserMetric extends Metric {

    @JsonProperty(NumberOfUsers)
    private int numberOfUsers;

    @JsonProperty(RegisteredClients)
    private ClientMetric registeredClients;

    public int getNumberOfUsers() {
        return numberOfUsers;
    }

    public ClientMetric getRegisteredClients() {
        return registeredClients;
    }
}
