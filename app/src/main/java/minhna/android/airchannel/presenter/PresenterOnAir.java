package minhna.android.airchannel.presenter;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import minhna.android.airchannel.data.local.LocalManager;
import minhna.android.airchannel.data.model.Channel;
import minhna.android.airchannel.data.model.Event;
import minhna.android.airchannel.data.net.RemoteManager;
import minhna.android.airchannel.util.DateUtil;
import minhna.android.airchannel.util.ListUtil;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Minh on 11/12/2017.
 */

public class PresenterOnAir extends BaseDataPresenter {
    private IOnAir iView;
    private List<Subscription> subscriptionList = new ArrayList<>();
    private boolean loadFistTimeFlag = true;

    public interface IOnAir extends BaseIView {
        void onLoadShowDone(List<Event> list, boolean firstTime);
        void onAppendShow(List<Event> list);
        void onLoadingEvent();
    }

    public PresenterOnAir(Context context, IOnAir iView) {
        this.context = context;
        this.iView = iView;

        final int timeReload = 60000;
        Observable.interval(timeReload, timeReload, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .takeUntil(preDestroy())
                .subscribe(aLong -> {
                    loadFistTimeFlag = false;
                    doLoadEventSequences();
                });
    }

    @Override
    public void bindComponent(LocalManager localManager, RemoteManager remoteManager) {
        super.bindComponent(localManager, remoteManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        iView = null;
    }

    public void loadEventsFromServer() {
        List<String> fullChannelIds = getFullChannelIds();
        int oneThirds = fullChannelIds.size() / 3;
        List<String> queryFirstChannels = new ArrayList<>();
        for (int i = 0; i < oneThirds; i++)
            queryFirstChannels.add(fullChannelIds.get(i));
        String time = DateUtil.getCurrentTime();
        /**
         * Astro api did not throw events in correct order regarding to channel order request.
         */
        Subscription subscription = remoteManager.getEvents(ListUtil.formatListToString(queryFirstChannels),
                time, time)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .takeUntil(preDestroy())
                .subscribe(response -> {
                    iView.onLoadShowDone(response.events, loadFistTimeFlag);
                    loadRemainEvents(fullChannelIds, oneThirds, time);
                }, error -> {
                    iView.onError(error.getMessage());
                    error.printStackTrace();
                });
        subscriptionList.add(subscription);
    }

    private void loadRemainEvents(List<String> fullChannelIds, int startPivot, String time) {
        int oneFourth = fullChannelIds.size() / 4;
        while (startPivot < fullChannelIds.size()) {
            List<String> queryRemainChannels = new ArrayList<>();
            for (int i = startPivot; i < fullChannelIds.size() && i < startPivot + oneFourth;i++)
                queryRemainChannels.add(fullChannelIds.get(i));
            Subscription subscription = remoteManager.getEvents(ListUtil.formatListToString(queryRemainChannels),
                    time, time)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .takeUntil(preDestroy())
                    .subscribe(response -> iView.onAppendShow(response.events)
                    ,error -> {
                        iView.onError(error.getMessage());
                        error.printStackTrace();
                    });
            subscriptionList.add(subscription);
            startPivot += oneFourth;
        }
    }

    private List<String> getFullChannelIds() {
        List<Channel> list = localManager.getChannelList();
        if (localManager.getProfile() != null)
            Collections.sort(list, Channel.getChannelComparator(localManager.getProfile().getSortType()));
        List<String> fullChannelIds = new ArrayList<>();
        for (Channel tmp: list)
            fullChannelIds.add(String.valueOf(tmp.getChannelId()));
        return fullChannelIds;
    }

    public void doLoadEventSequences() {
        unsubscribeCurrentObservable();
        iView.onLoadingEvent();
        if (localManager.getChannelList().size() > 0)
            loadEventsFromServer();
        else {
            remoteManager.getChannels()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .takeUntil(preDestroy())
                    .subscribe(response -> {
                        localManager.setChannelList(response.channels);
                        loadEventsFromServer();
                    }, error -> {
                        iView.onError(error.getMessage());
                        error.printStackTrace();
                    });
        }
    }

    //To make sure the previous loading does not affect the current.
    private void unsubscribeCurrentObservable() {
        for (Subscription tmp: subscriptionList)
            tmp.unsubscribe();
        subscriptionList.clear();
    }
}
