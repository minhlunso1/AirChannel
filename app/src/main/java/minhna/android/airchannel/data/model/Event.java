package minhna.android.airchannel.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Minh on 11/12/2017.
 */

public class Event implements Serializable {
    @SerializedName("eventID")
    private String eventId;
    @SerializedName("channelId")
    private int channelId;
    @SerializedName("channelTitle")
    private String channelTitle;
    @SerializedName("programmeTitle")
    private String eventTitle;

    public int color;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
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

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public static Comparator<Event> NameComparator = (obj1, obj2) -> {
        String property1 = obj1.getChannelTitle();
        String property2 = obj2.getChannelTitle();
        return property1.compareTo(property2);
    };
    public static Comparator<Event> IDComparator = (obj1, obj2) -> {
        int property1 = obj1.getChannelId();
        int property2 = obj2.getChannelId();
        return property1 - property2;
    };

    public static Comparator<Event> getEventComparator(int type) {
        Comparator<Event> returnValue = IDComparator;
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
}
