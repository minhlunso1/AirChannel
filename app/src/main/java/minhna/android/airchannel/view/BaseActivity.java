package minhna.android.airchannel.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import minhna.android.airchannel.R;
import minhna.android.airchannel.app.AirChannelApplication;
import minhna.android.airchannel.injection.component.DaggerViewComponent;
import minhna.android.airchannel.injection.component.ViewComponent;
import minhna.android.airchannel.injection.module.ActivityModule;
import rx.subjects.BehaviorSubject;

/**
 * Created by Minh on 11/10/2017.
 */

public class BaseActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    protected Snackbar snackbar;
    protected boolean canFinishMain;

    private ViewComponent viewComponent;

    private final BehaviorSubject<BaseActivity> preDestroy = BehaviorSubject.create();
    protected BehaviorSubject<BaseActivity> preDestroy() {
        return preDestroy;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onDestroy() {
        preDestroy.onNext(this);
        super.onDestroy();
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

    protected View.OnClickListener showErrorToast() {
        return v -> Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();
    }
}
