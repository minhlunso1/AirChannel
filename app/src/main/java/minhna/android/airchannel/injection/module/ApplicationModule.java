package minhna.android.airchannel.injection.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import minhna.android.airchannel.BuildConfig;
import minhna.android.airchannel.data.net.RemoteFactory;
import minhna.android.airchannel.data.net.RemoteInterface;
import minhna.android.airchannel.injection.annotation.ApplicationContext;
import minhna.android.airchannel.injection.annotation.DatabaseInfo;

/**
 * Created by Minh on 11/9/2017.
 */

@Module
public class ApplicationModule {
    protected final Application mApplication;
    protected FirebaseAuth mAuth;

    public ApplicationModule(Application application, FirebaseAuth firebaseAuth) {
        mApplication = application;
        mAuth = firebaseAuth;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return BuildConfig.APPLICATION_ID + ".db";
    }

    @Provides
    @DatabaseInfo
    Integer provideDatabaseVersion() {
        return BuildConfig.DB_VERSION;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPrefs() {
        return mApplication.getSharedPreferences(BuildConfig.APPLICATION_ID , Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    RemoteInterface provideBourbonService() {
        return RemoteFactory.buildRemoteInterface();
    }

    @Provides
    @Singleton
    FirebaseAuth provideAuth() {
        return mAuth;
    }
}