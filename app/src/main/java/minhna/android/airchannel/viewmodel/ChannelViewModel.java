package minhna.android.airchannel.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import minhna.android.airchannel.BR;
import minhna.android.airchannel.data.pojo.Channel;

/**
 * Created by Minh on 11/11/2017.
 */

public class ChannelViewModel extends BaseObservable {
    private Channel channel;

    public ChannelViewModel(Channel channel) {
        this.channel = channel;
    }

    @Bindable
    public String getChannelTitle() {
        return ". " + channel.getChannelTitle();
    }
    public void setChannelTitle(String title) {
        channel.setChannelTitle(title);
        notifyPropertyChanged(BR.channelTitle);
    }

    public long getChannelId() {
        return channel.getChannelId();
    }

    public int getImgFavRes() {
        return channel.imgFavRes;
    }

    @BindingAdapter({"image"})
    public static void loadImage(ImageView view, int res) {
       view.setImageResource(res);
    }
    public View.OnClickListener onImgFavClick() {
        return view -> {};
    }

}
