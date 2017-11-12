package minhna.android.airchannel.view.presenter;

import android.content.Context;
import android.view.View;

import java.util.Collections;
import java.util.List;

import minhna.android.airchannel.R;
import minhna.android.airchannel.data.local.LocalManager;
import minhna.android.airchannel.data.model.Channel;
import minhna.android.airchannel.data.net.RemoteManager;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Minh on 11/12/2017.
 */

public class PresenterChannel extends BasePresenter {
    private IChannel iView;
    private LocalManager localManager;
    private RemoteManager remoteManager;

    public interface IChannel extends BaseIView {
        void onLoadChannelDone(List<Channel> list, boolean fromRemote);
        void onLoadingChannel();
    }

    public PresenterChannel(Context context, IChannel iView) {
        this.context = context;
        this.iView = iView;
    }

    public void bindComponent(LocalManager localManager, RemoteManager remoteManager) {
        this.localManager = localManager;
        this.remoteManager = remoteManager;
    }

    public void loadChannelsFromServer() {
        remoteManager.getChannels()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(response -> {
                    List<Channel> list = response.channels;
                    Collections.sort(list, Channel.IDComparator);
                    return list;
                })
                .takeUntil(preDestroy())
                .doOnSubscribe(() -> iView.onLoadingChannel())
                .subscribe(channels -> {
                    channels = formatList(channels);
                    iView.onLoadChannelDone(channels, true);
                }, error -> {
                    iView.onError(error.getMessage());
                    error.printStackTrace();
                });
    }

    private List<Channel> formatList(List<Channel> list) {
        for (int i = 0; i < list.size(); i++) {
            if (localManager.getFavChannelMap().get(list.get(i).getChannelId()) == null)
                list.get(i).imgFavRes = R.mipmap.ic_non_fav;
            else
                list.get(i).imgFavRes = R.mipmap.ic_on_fav;
        }
        return list;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        iView = null;
        localManager = null;
        remoteManager = null;
    }
}
