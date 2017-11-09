package minhna.android.airchannel.injection.module;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import dagger.Module;
import dagger.Provides;
import minhna.android.airchannel.injection.annotation.ActivityContext;

/**
 * Created by Minh on 11/9/2017.
 */

@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @ActivityContext
    Context providesContext() {
        return mActivity;
    }

    @Provides
    FragmentManager providesFragmentManager() {
        return ((FragmentActivity) mActivity).getSupportFragmentManager();
    }

}
