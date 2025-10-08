package eu.europeana.api.commons.definitions.statistics.user;

import eu.europeana.api.commons.definitions.statistics.Metric;
import eu.europeana.api.commons.definitions.statistics.UsageStatsFields;

import java.util.Date;

public class ELKMetric extends Metric {

    private String type = UsageStatsFields.OVERALL_TOTAL_TYPE;

    private Date timestamp = new Date();

    /**
     * Active Users 1: Number of API users that exceeded the average of 5 calls a day per month
     *
     * Active Users 2: Number of API users that were active for more than 5 days in each month
     *
     * Pick the highest from Active Users 1 and 2 instead of reporting both
     */
    private long activeExternalClients;

    private long allExternalClients;

    /**
     * Total nr of requests reflecting external clients
     */
    private long externalClientUsage;

    /**
     * Total nr of requests reflecting internal clients
     */
    private long internalClientUsage;


    public ELKMetric(long activeExternalClients, long allExternalClients, long externalClientUsage, long internalClientUsage) {
        this.activeExternalClients = activeExternalClients;
        this.allExternalClients = allExternalClients;
        this.externalClientUsage = externalClientUsage;
        this.internalClientUsage = internalClientUsage;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public Date getTimestamp() {
        return this.timestamp;
    }

    public long getActiveExternalClients() {
        return activeExternalClients;
    }

    public long getAllExternalClients() {
        return allExternalClients;
    }

    public long getExternalClientUsage() {
        return externalClientUsage;
    }

    public long getInternalClientUsage() {
        return internalClientUsage;
    }
}
