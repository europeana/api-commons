package eu.europeana.api.commons.definitions.statistics.user;

import eu.europeana.api.commons.definitions.statistics.Metric;

public class ELKMetric extends Metric {

    private static String elkMetricReport = """
     
     Usage of clients who hold project keys
     Regular Customer  : %s
     All Customer      : %s
     
     Usage of clients who hold personal keys
     Regular User     : %s
     All User         : %s
     
     Overall All metric obtained without discriminating between personal and project key.
     All : %s
     
     Usage
     Internal        : %s
     External        : %s

     Note: monthly stats""";

    /**
     * Clients who hold project keys
     *
     * Active Users 1: Number of API users that exceeded the average of 5 calls a day per month
     *
     * Active Users 2: Number of API users that were active for more than 5 days in each month
     *
     * Pick the highest from Active Users 1 and 2 instead of reporting both
     */
    private long regularCustomer;

    /**
     * Total clients who hold project keys
     */
    private long allCustomer;

    /**
     * Clients who hold personal key
     * Active Users 1: Number of API users that exceeded the average of 5 calls a day per month
     *
     * Active Users 2: Number of API users that were active for more than 5 days in each month
     *
     * Pick the highest from Active Users 1 and 2 instead of reporting both
     */
    private long regularUser;

    /**
     * total client who hold personal keys
     */
    private long allUser;

    /**
     *  an overall All metric obtained without discriminating between personal and project key.
     */
    private long all;

    /**
     * Total nr of requests reflecting external clients
     */
    private long externalClientUsage;

    /**
     * Total nr of requests reflecting internal clients
     */
    private long internalClientUsage;

    public ELKMetric(long regularCustomer, long allCustomer, long regularUser, long allUser, long all, long externalClientUsage, long internalClientUsage) {
        this.regularCustomer = regularCustomer;
        this.allCustomer = allCustomer;
        this.regularUser = regularUser;
        this.allUser = allUser;
        this.all = all;
        this.externalClientUsage = externalClientUsage;
        this.internalClientUsage = internalClientUsage;
    }

    public long getRegularCustomer() {
        return regularCustomer;
    }

    public long getAllCustomer() {
        return allCustomer;
    }

    public long getRegularUser() {
        return regularUser;
    }

    public long getAllUser() {
        return allUser;
    }

    public long getAll() {
        return all;
    }

    public long getExternalClientUsage() {
        return externalClientUsage;
    }

    public long getInternalClientUsage() {
        return internalClientUsage;
    }

    @Override
    public String toString() {
        return String.format(elkMetricReport,
                getRegularCustomer(), getAllCustomer(),
                getRegularUser(), getAllUser(),
                getAll(),
                getInternalClientUsage(), getExternalClientUsage());
    }
}
