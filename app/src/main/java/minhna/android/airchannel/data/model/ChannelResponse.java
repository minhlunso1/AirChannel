package minhna.android.airchannel.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Minh on 11/11/2017.
 */

public class ChannelResponse {
    @SerializedName("channels")
    public List<Channel> channels;
}
