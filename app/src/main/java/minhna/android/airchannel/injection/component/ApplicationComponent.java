package minhna.android.airchannel.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import minhna.android.airchannel.app.AirChannelApplication;
import minhna.android.airchannel.data.local.AP;
import minhna.android.airchannel.data.local.DBHelper;
import minhna.android.airchannel.data.local.LocalManager;
import minhna.android.airchannel.injection.annotation.ApplicationContext;
import minhna.android.airchannel.injection.module.ApplicationModule;

/**
 * Created by Minh on 11/9/2017.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(AirChannelApplication application);

    @ApplicationContext
    Context context();
    Application application();
    LocalManager localManager();
    AP AP();
    DBHelper DBHelper();
}
