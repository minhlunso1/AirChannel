package minhna.android.airchannel.app;

import android.app.Application;
import android.content.Context;

import javax.inject.Inject;

import minhna.android.airchannel.data.local.LocalManager;
import minhna.android.airchannel.injection.component.ApplicationComponent;
import minhna.android.airchannel.injection.component.DaggerApplicationComponent;
import minhna.android.airchannel.injection.module.ApplicationModule;

/**
 * Created by Minh on 11/9/2017.
 */

public class AirChannelApplication extends Application {
    ApplicationComponent mApplicationComponent;

    @Inject
    LocalManager localManager;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        mApplicationComponent.inject(this);
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }

    public static AirChannelApplication get(Context context) {
        return (AirChannelApplication) context.getApplicationContext();
    }
}
