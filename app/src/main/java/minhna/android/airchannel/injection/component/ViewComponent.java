package minhna.android.airchannel.injection.component;

import dagger.Component;
import minhna.android.airchannel.view.ChannelActivity;
import minhna.android.airchannel.view.MainActivity;
import minhna.android.airchannel.injection.annotation.SingletonScope;
import minhna.android.airchannel.injection.module.ActivityModule;
import minhna.android.airchannel.view.OnAirActivity;

/**
 * Created by Minh on 11/9/2017.
 */

@SingletonScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ViewComponent {
    void inject(MainActivity mainActivity);
    void inject(ChannelActivity channelActivity);
    void inject(OnAirActivity onAirActivity);
}
