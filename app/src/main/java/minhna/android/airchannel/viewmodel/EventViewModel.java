package minhna.android.airchannel.viewmodel;

import minhna.android.airchannel.data.model.Event;

/**
 * Created by Minh on 11/13/2017.
 */

public class EventViewModel extends BaseViewModel {
    private Event event;

    public EventViewModel(int position, Event event) {
        super(position);
        this.event = event;
    }

    public String getChannelInfo() {
        return event.getChannelId() + ". " + event.getChannelTitle();
    }

    public int getColor() {
        return event.color;
    }

    public String getEventInfo() {
        return event.getEventTitle();
    }
}
