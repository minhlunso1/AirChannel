package minhna.android.airchannel.util;

import java.util.List;

/**
 * Created by Minh on 11/14/2017.
 */

public class ListUtil {
    public static String formatListToString(List<String> list) {
        String returnValue ="";
        for (String tmp: list)
            returnValue += tmp +",";
        return returnValue.substring(0, returnValue.length() - 1);
    }
}
