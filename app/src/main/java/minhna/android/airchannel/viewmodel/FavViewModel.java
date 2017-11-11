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
    private IFavViewMode iFavViewMode;

    public interface IFavViewMode {
        void onRemoveFav(int channelId, int position);
    }

    public FavViewModel(int position, Channel channel, LocalManager localManager, IFavViewMode iFavViewMode) {
        super(position, localManager);
        this.channel = channel;
        this.iFavViewMode = iFavViewMode;
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
                if (localManager.deleteChannel(getChannelId())) {
                    localManager.getFavChannelMap().remove(getChannelId());
                    iFavViewMode.onRemoveFav(getChannelId(), position);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}