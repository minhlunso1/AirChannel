package minhna.android.airchannel.data.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Minh on 11/12/2017.
 */

@IntDef({SortType.ID, SortType.NAME})
@Retention(RetentionPolicy.SOURCE)
public @interface SortType {
    int ID = 0;
    int NAME = 1;
}

