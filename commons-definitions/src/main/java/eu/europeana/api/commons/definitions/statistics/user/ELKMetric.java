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
    private long activeApiUsers;

    /**
     *
     */
    private long totalExternalTraffic;

    /**
     * Total number of distinct users (external)
     */
    private long totalApiUsers;

    public ELKMetric(long totalApiUsers, long activeApiUsers, long totalExternalTraffic) {
        this.activeApiUsers = activeApiUsers;
        this.totalExternalTraffic = totalExternalTraffic;
        this.totalApiUsers = totalApiUsers;
    }

    public long getActiveApiUsers() {
        return activeApiUsers;
    }

    public long getTotalExternalTraffic() {
        return totalExternalTraffic;
    }

    public long getTotalApiUsers() {
        return totalApiUsers;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public Date getTimestamp() {
        return this.timestamp;
    }

}
