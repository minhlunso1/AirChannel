package minhna.android.airchannel.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Minh on 11/14/2017.
 */

public class DateUtil {
    public static String getCurrentTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
    }
}
