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
    private Channel channel;
    @SerializedName("programmeTitle")
    private String eventTitle;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public static Comparator<Event> NameComparator = (obj1, obj2) -> {
        String property1 = obj1.getChannel().getChannelTitle();
        String property2 = obj2.getChannel().getChannelTitle();
        return property1.compareTo(property2);
    };
    public static Comparator<Event> IDComparator = (obj1, obj2) -> {
        int property1 = obj1.getChannel().getChannelId();
        int property2 = obj2.getChannel().getChannelId();
        return property1 - property2;
    };
}
