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

    @JsonProperty(UsageStatsFields.NUMBER_OF_USER_WITH_GALLERY)
    private long numberOfUsersWithGallery;

    @JsonProperty(UsageStatsFields.NUMBER_OF_USER_WITH_LIKE)
    private long numberOfUsersWithLike;

    @JsonProperty(UsageStatsFields.NUMBER_OF_USER_WITH_LIKE_OR_GALLERY)
    private long numberOfUsersWithLikeOrGallery;

    @JsonProperty(UsageStatsFields.NUMBER_OF_ENTITY_SETS)
    private long numberOfEntitySets;

    @JsonProperty(UsageStatsFields.NUMBER_OF_ITEMS_IN_ENTITY_SETS)
    private long numberOfItemsInEntitySets;

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

    public long getNumberOfUsersWithGallery() {
        return numberOfUsersWithGallery;
    }

    public void setNumberOfUsersWithGallery(long numberOfUsersWithGallery) {
        this.numberOfUsersWithGallery = numberOfUsersWithGallery;
    }

    public long getNumberOfUsersWithLike() {
        return numberOfUsersWithLike;
    }

    public void setNumberOfUsersWithLike(long numberOfUsersWithLike) {
        this.numberOfUsersWithLike = numberOfUsersWithLike;
    }

    public long getNumberOfUsersWithLikeOrGallery() {
        return numberOfUsersWithLikeOrGallery;
    }

    public void setNumberOfUsersWithLikeOrGallery(long numberOfUsersWithLikeOrGallery) {
        this.numberOfUsersWithLikeOrGallery = numberOfUsersWithLikeOrGallery;
    }

    public long getNumberOfEntitySets() {
      return numberOfEntitySets;
    }

    public void setNumberOfEntitySets(long numberOfEntitySets) {
      this.numberOfEntitySets = numberOfEntitySets;
    }

    public long getNumberOfItemsInEntitySets() {
      return numberOfItemsInEntitySets;
    }

    public void setNumberOfItemsInEntitySets(long numberOfItemsInEntitySets) {
      this.numberOfItemsInEntitySets = numberOfItemsInEntitySets;
    }
}
