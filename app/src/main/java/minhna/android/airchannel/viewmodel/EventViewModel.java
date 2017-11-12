package minhna.android.airchannel.viewmodel;

import android.databinding.BindingAdapter;
import android.support.annotation.ColorRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.TextView;

import minhna.android.airchannel.data.model.Event;

/**
 * Created by Minh on 11/13/2017.
 */

public class EventViewModel extends BaseViewModel {
    private int color;
    private Event event;

    public EventViewModel(int position, @ColorRes int color, Event event) {
        super(position);
        this.color = color;
        this.event = event;
    }

    @BindingAdapter("bind:colorTint")
    public static void setColorTint(TextView view, @ColorRes int color) {
        DrawableCompat.setTint(view.getBackground(), color);
    }

    public String getChannelInfo() {
        return event.getChannel().getChannelId() + ". " + event.getChannel().getChannelTitle();
    }

    public int getColor() {
        return color;
    }

    public String getEventInfo() {
        return event.getEventTitle();
    }
}
