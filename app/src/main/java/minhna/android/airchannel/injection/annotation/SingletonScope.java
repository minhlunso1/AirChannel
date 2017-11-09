package minhna.android.airchannel.injection.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Minh on 11/9/2017.
 */

/**
 * Makes dagger to create an object only once and use same object in future.
 * @Singleton has no use when Activity, Application go with .build()
 */
@Scope
@Retention(RetentionPolicy.CLASS)
public @interface SingletonScope {
}
