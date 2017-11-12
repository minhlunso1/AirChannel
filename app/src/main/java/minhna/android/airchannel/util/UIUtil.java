package minhna.android.airchannel.util;

import minhna.android.airchannel.R;

/**
 * Created by Minh on 11/13/2017.
 */

public class UIUtil {
    public static final int MAX_JUMP = 5;
    public static int getColorRes(int jump) {
        switch (jump) {
            case 0:
                return R.color.colorFeature1;
            case 1:
                return R.color.colorFeature2;
            case 2:
                return R.color.colorPrimaryDark;
            case 3:
                return R.color.colorFeature3;
            case 4:
                return R.color.colorFeature4;
        }
        return R.color.colorPrimary;
    }
}
