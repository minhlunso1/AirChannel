package minhna.android.airchannel.presenter;

import android.content.Context;

import java.util.List;

import minhna.android.airchannel.data.local.LocalManager;
import minhna.android.airchannel.data.model.Event;
import minhna.android.airchannel.data.net.RemoteManager;

/**
 * Created by Minh on 11/12/2017.
 */

public class PresenterOnAir extends BaseDataPresenter {
    private IOnAir iView;

    public interface IOnAir extends BaseIView {
        void onLoadShowDone(List<Event> list, boolean fromRemote);
        void onLoadingEvent();
    }

    public PresenterOnAir(Context context, IOnAir iView) {
        this.context = context;
        this.iView = iView;
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
    }
}
