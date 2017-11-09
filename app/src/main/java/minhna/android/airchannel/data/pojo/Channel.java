package minhna.android.airchannel.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Minh on 11/9/2017.
 */

public class Channel {
    @SerializedName("channelId")
    private long channelId;
    @SerializedName("channelTitle")
    private String channelTitle;

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }
}
