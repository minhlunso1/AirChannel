package minhna.android.airchannel.viewmodel;

import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import minhna.android.airchannel.BR;
import minhna.android.airchannel.R;
import minhna.android.airchannel.data.local.LocalManager;
import minhna.android.airchannel.data.model.Channel;

/**
 * Created by Minh on 11/11/2017.
 */

public class ChannelViewModel extends BaseViewModel {
    private Channel channel;

    public ChannelViewModel(int position, Channel channel, LocalManager localManager) {
        super(position, localManager);
        this.channel = channel;
    }

    public String getChannelTitle() {
        return ". " + channel.getChannelTitle();
    }

    public int getChannelId() {
        return channel.getChannelId();
    }

    @Bindable
    public int getImgFavRes() {
        return channel.imgFavRes;
    }
    public void setImgFavRes(int imgFavRes) {
        channel.imgFavRes = imgFavRes;
        notifyPropertyChanged(BR.imgFavRes);
    }

    @BindingAdapter({"image"})
    public static void loadImage(ImageView view, int res) {
       view.setImageResource(res);
    }
    public View.OnClickListener onImgFavClick() {
        return view -> {
            try {
                if (getImgFavRes() == R.mipmap.ic_on_fav) {
                    localManager.createChannel(channel);
                    if (localManager.deleteChannel(getChannelId())) {
                        localManager.getFavChannelMap().remove(getChannelId());
                        setImgFavRes(R.mipmap.ic_non_fav);
                    }
                } else if (getImgFavRes() == R.mipmap.ic_non_fav) {
                    localManager.createChannel(channel);
                    localManager.getFavChannelMap().put(getChannelId(), channel);
                    setImgFavRes(R.mipmap.ic_on_fav);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

}
