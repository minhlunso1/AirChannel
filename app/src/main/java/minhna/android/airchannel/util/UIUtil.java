package minhna.android.airchannel.util;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static Pair<View, String>[] createSafeTransitionParticipants(@NonNull Activity activity,
                                                                        boolean includeStatusBar, @Nullable Pair... otherParticipants) {
        View decor = activity.getWindow().getDecorView();
        View statusBar = null;
        if (includeStatusBar) {
            statusBar = decor.findViewById(android.R.id.statusBarBackground);
        }
        View navBar = decor.findViewById(android.R.id.navigationBarBackground);

        List<Pair> participants = new ArrayList<>(3);
        addNonNullViewToTransitionParticipants(statusBar, participants);
        addNonNullViewToTransitionParticipants(navBar, participants);
        if (otherParticipants != null && !(otherParticipants.length == 1
                && otherParticipants[0] == null)) {
            participants.addAll(Arrays.asList(otherParticipants));
        }
        return participants.toArray(new Pair[participants.size()]);
    }

    private static void addNonNullViewToTransitionParticipants(View view, List<Pair> participants) {
        if (view == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            participants.add(new Pair<>(view, view.getTransitionName()));
        }
    }
}
