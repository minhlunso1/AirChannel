package minhna.android.airchannel.viewmodel;

import android.databinding.Bindable;
import android.view.View;

import minhna.android.airchannel.R;
import minhna.android.airchannel.data.local.LocalManager;
import minhna.android.airchannel.data.model.Channel;

/**
 * Created by Minh on 11/11/2017.
 */

public class FavViewModel extends BaseViewModel {
    private Channel channel;

    public interface IFavViewMode {
        void onRemove(int channelId, int position);
    }

    public FavViewModel(int position, Channel channel, LocalManager localManager) {
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
    public int getImgDelete() {
        return R.mipmap.ic_remove;
    }

    public View.OnClickListener onRemoveClick() {
        return view -> {
            try {
                if (localManager.deleteChannel(getChannelId()))
                    localManager.getFavChannelMap().remove(getChannelId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

}
