package minhna.android.airchannel.data.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Comparator;

import minhna.android.airchannel.R;

/**
 * Created by Minh on 11/9/2017.
 */

public class Channel implements Serializable {
    @SerializedName("channelId")
    private int channelId;
    @SerializedName("channelTitle")
    private String channelTitle;

    public int imgFavRes;

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public boolean isFav() {
        if (imgFavRes == R.mipmap.ic_on_fav)
            return true;
        else
            return false;
    }

    public static Comparator<Channel> NameComparator = (obj1, obj2) -> {
        String property1 = obj1.getChannelTitle();
        String property2 = obj2.getChannelTitle();
        //ascending order
        return property1.compareTo(property2);
    };
    public static Comparator<Channel> IDComparator = (obj1, obj2) -> {
        int property1 = obj1.getChannelId();
        int property2 = obj2.getChannelId();
        return property1 - property2;
    };
}
