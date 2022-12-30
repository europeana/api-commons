package eu.europeana.api.commons.definitions.statistics.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import eu.europeana.api.commons.definitions.statistics.UsageStatsFields;

@JsonPropertyOrder({UsageStatsFields.IMAGE, UsageStatsFields.TEXT, UsageStatsFields.AUDIO, UsageStatsFields.VIDEO,
        UsageStatsFields.THREE_D, UsageStatsFields.TOTAL})
public class HighQualityMetric {

    @JsonProperty(UsageStatsFields.IMAGE)
    private long image;

    @JsonProperty(UsageStatsFields.VIDEO)
    private long video;

    @JsonProperty(UsageStatsFields.TEXT)
    private long text;

    @JsonProperty(UsageStatsFields.AUDIO)
    private long audio;

    @JsonProperty(UsageStatsFields.THREE_D)
    private long threeD;

    @JsonProperty(UsageStatsFields.TOTAL)
    private long total;

    public long getImage() {
        return image;
    }

    public void setImage(long image) {
        this.image = image;
    }

    public long getVideo() {
        return video;
    }

    public void setVideo(long video) {
        this.video = video;
    }

    public long getText() {
        return text;
    }

    public void setText(long text) {
        this.text = text;
    }

    public long getAudio() {
        return audio;
    }

    public void setAudio(long audio) {
        this.audio = audio;
    }

    public long getThreeD() {
        return threeD;
    }

    public void setThreeD(long threeD) {
        this.threeD = threeD;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    @JsonIgnore
    public long getOverall() {
        return this.image + this.text + this.audio + this.video + this.threeD;
    }
}
