package minhna.android.airchannel.data.model;

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

    public Channel() {
    }

    public Channel(int channelId, String channelTitle) {
        this.channelId = channelId;
        this.channelTitle = channelTitle;
    }

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

    public static String getChannelField(@SortType int type) {
        String returnStr = "";
        switch (type) {
            case SortType.ID:
                 returnStr = "channelId";
                 break;
            case SortType.NAME:
                returnStr = "channelName";
                break;
        }
        return returnStr;
    }
    public static Comparator<Channel> getChannelComparator(@SortType int type) {
        Comparator<Channel> returnValue = IDComparator;
        switch (type) {
            case SortType.ID:
                returnValue = IDComparator;
                break;
            case SortType.NAME:
                returnValue = NameComparator;
                break;
        }
        return returnValue;
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
