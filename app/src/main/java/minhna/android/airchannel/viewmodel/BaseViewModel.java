package minhna.android.airchannel.viewmodel;

import android.databinding.BaseObservable;

import minhna.android.airchannel.data.local.LocalManager;
import minhna.android.airchannel.data.net.RemoteManager;

/**
 * Created by Minh on 11/11/2017.
 */

public class BaseViewModel extends BaseObservable {
    protected int position;
    protected LocalManager localManager;
    protected RemoteManager remoteManager;

    public BaseViewModel(int position, LocalManager localManager, RemoteManager remoteManager) {
        this.position = position;
        this.localManager = localManager;
        this.remoteManager = remoteManager;
    }

    public BaseViewModel(int position) {
        this.position = position;
    }
}
