package eu.europeana.api.commons.definitions.statistics.set;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europeana.api.commons.definitions.statistics.Metric;
import eu.europeana.api.commons.definitions.statistics.UsageStatsFields;

public class SetMetric extends Metric {

    @JsonProperty(UsageStatsFields.PRIVATE_SETS)
    private long noOfPrivateSets;

    @JsonProperty(UsageStatsFields.PUBLIC_SETS)
    private long noOfPublicSets;

    @JsonProperty(UsageStatsFields.ITEMS_LIKED)
    private long noOfItemsLiked;

    @JsonProperty(UsageStatsFields.SETS_PER_USER)
    private long averageSetsPerUser;

    public long getNoOfPrivateSets() {
        return noOfPrivateSets;
    }

    public void setNoOfPrivateSets(long noOfPrivateSets) {
        this.noOfPrivateSets = noOfPrivateSets;
    }

    public long getNoOfPublicSets() {
        return noOfPublicSets;
    }

    public void setNoOfPublicSets(long noOfPublicSets) {
        this.noOfPublicSets = noOfPublicSets;
    }

    public long getNoOfItemsLiked() {
        return noOfItemsLiked;
    }

    public void setNoOfItemsLiked(long noOfItemsLiked) {
        this.noOfItemsLiked = noOfItemsLiked;
    }

    public long getAverageSetsPerUser() {
        return averageSetsPerUser;
    }

    public void setAverageSetsPerUser(long averageSetsPerUser) {
        this.averageSetsPerUser = averageSetsPerUser;
    }
}
