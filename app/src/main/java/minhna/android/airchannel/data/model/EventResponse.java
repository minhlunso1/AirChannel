package minhna.android.airchannel.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Minh on 11/13/2017.
 */

public class EventResponse {
    @SerializedName("getevent")
    public List<Event> events;
}
