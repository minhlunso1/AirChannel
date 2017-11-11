package minhna.android.airchannel.viewmodel;

import android.databinding.BaseObservable;

import minhna.android.airchannel.data.local.LocalManager;

/**
 * Created by Minh on 11/11/2017.
 */

public class BaseViewModel extends BaseObservable {
    protected int position;
    protected LocalManager localManager;

    public BaseViewModel(int position, LocalManager localManager) {
        this.position = position;
        this.localManager = localManager;
    }
}
