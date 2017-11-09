package minhna.android.airchannel.injection.component;

import dagger.Component;
import minhna.android.airchannel.MainActivity;
import minhna.android.airchannel.injection.annotation.SingletonScope;
import minhna.android.airchannel.injection.module.ActivityModule;

/**
 * Created by Minh on 11/9/2017.
 */

@SingletonScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ViewComponent {
    void inject(MainActivity mainActivity);
}
