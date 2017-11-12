package minhna.android.airchannel.view.presenter;

import android.content.Context;

import rx.subjects.BehaviorSubject;

/**
 * Created by Minh on 11/12/2017.
 */

public class BasePresenter {
    protected Context context;

    private final BehaviorSubject<BasePresenter> preDestroy = BehaviorSubject.create();
    protected BehaviorSubject<BasePresenter> preDestroy() {
        return preDestroy;
    }

    public void onDestroy() {
        preDestroy.onNext(this);
        context = null;
    }
}
