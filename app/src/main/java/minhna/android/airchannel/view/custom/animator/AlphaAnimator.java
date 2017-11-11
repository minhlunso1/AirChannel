package minhna.android.airchannel.view.custom.animator;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by Minh on 3/29/2017.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AlphaAnimator extends ValueAnimator {

    public static AlphaAnimator create(View target, @FloatRange(from = 0.0f, to = 1.0f) float fromAlpha,
                                       @FloatRange(from = 0.0f, to = 1.0f) float toAlpha) {
        return new AlphaAnimator(target, fromAlpha, toAlpha);
    }

    private final float mFromAlpha;

    private final WeakReference<View> mTarget;

    private final float mToAlpha;

    protected AlphaAnimator(View target, float fromAlpha, float toAlpha) {
        mTarget = new WeakReference<>(target);
        mFromAlpha = fromAlpha;
        mToAlpha = toAlpha;
        setFloatValues(fromAlpha, toAlpha);
        addUpdateListener(new AnimatorUpdateListener(target));
    }

    @Override
    public void start() {
        View target = mTarget.get();
        if (target != null) {
            target.setAlpha(mFromAlpha);
        }
        super.start();
    }

    private static class AnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        private final WeakReference<View> mTarget;

        public AnimatorUpdateListener(View target) {
            mTarget = new WeakReference<>(target);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            View target = mTarget.get();
            if (target != null) {
                float alpha = (float) animation.getAnimatedValue();
                target.setAlpha(alpha);
            }
        }
    }
}
