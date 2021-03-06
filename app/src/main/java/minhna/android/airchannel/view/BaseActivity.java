package minhna.android.airchannel.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import minhna.android.airchannel.R;
import minhna.android.airchannel.app.AirChannelApplication;
import minhna.android.airchannel.injection.component.DaggerViewComponent;
import minhna.android.airchannel.injection.component.ViewComponent;
import minhna.android.airchannel.injection.module.ActivityModule;
import minhna.android.airchannel.presenter.BasePresenter;
import minhna.android.airchannel.util.UIUtil;
import rx.subjects.BehaviorSubject;

/**
 * Created by Minh on 11/10/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    protected Snackbar snackbar;
    protected boolean canFinishMain;
    protected ProgressDialog pDialog;

    private ViewComponent viewComponent;
    protected BasePresenter basePresenter;

    private final BehaviorSubject<BaseActivity> preDestroy = BehaviorSubject.create();
    protected BehaviorSubject<BaseActivity> preDestroy() {
        return preDestroy;
    }

    protected abstract BasePresenter initPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        canFinishMain = true;
        setupDialog();
        basePresenter = initPresenter();
    }

    @Override
    protected void onDestroy() {
        preDestroy.onNext(this);
        super.onDestroy();
        if (basePresenter != null)
            basePresenter.onDestroy();
    }

    public ViewComponent getViewComponent() {
        if (viewComponent == null) {
            viewComponent = DaggerViewComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(AirChannelApplication.get(this).getComponent())
                    .build();
        }
        return viewComponent;
    }

    protected void showSnackbar(View view, String message, int length) {
        if (snackbar != null && snackbar.isShown())
            snackbar.dismiss();
        if (message == null || message.isEmpty())
            message = getString(R.string.suggest_check_network);
        snackbar = Snackbar.make(view, message, length);
        snackbar.show();
    }

    protected void toggleSnackbar(View view, boolean toShow, String message) {
        if (toShow) {
            snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        } else {
            if (snackbar != null && snackbar.isShown())
                new Handler().postDelayed(() -> {
                    snackbar.dismiss();
                }, 500);
        }
    }

    protected void setupDialog() {
        pDialog = new ProgressDialog(this, R.style.AppAlertDialogStyle);
        pDialog.setMessage(getString(R.string.loading));
    }

    public void showProgressDialog(String message) {
        if (message != null)
            pDialog.setMessage(message);
        pDialog.show();
    }

    public void hideProgressDialog() {
        pDialog.dismiss();
    }

    public static void startNewTaskWith(Context context, Class activity) {
        Intent intent = new Intent(context, activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void transitionToActivity(Class target, View fromView, int stringId, Activity activity) {
        final Pair<View, String>[] pairs = UIUtil.createSafeTransitionParticipants(activity, false,
                new Pair<>(fromView, activity.getString(stringId)));
        startActivityWithSharedElement(target, pairs, activity);
    }

    private void startActivityWithSharedElement(Class target, Pair<View, String>[] pairs, Activity activity) {
        Intent i = new Intent(activity, target);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pairs);
        activity.startActivity(i, transitionActivityOptions.toBundle());
    }
}
