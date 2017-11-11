package minhna.android.airchannel.view.custom.animator;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;

import java.lang.ref.WeakReference;

import minhna.android.airchannel.util.DimenUtil;
import minhna.android.airchannel.view.custom.metric.ArcMetric;
import minhna.android.airchannel.view.custom.metric.Side;

/**
 * Created by Minh on 3/29/2017.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ArcAnimator extends ValueAnimator {

    public static ArcAnimator create(View target, float endX, float endY, float degree, Side side) {
        ArcMetric arcMetric = ArcMetric.evaluate(DimenUtil.centerX(target), DimenUtil.centerY(target), endX, endY, degree, side);
        return new ArcAnimator(arcMetric, target);
    }

    protected ArcAnimator(ArcMetric arcMetric, View target) {
        setFloatValues(arcMetric.getStartDegree(), arcMetric.getEndDegree());
        addUpdateListener(new AnimatorUpdateListener(arcMetric, target));
    }

    private static class AnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        private final ArcMetric mArcMetric;

        private final WeakReference<View> mTarget;

        public AnimatorUpdateListener(ArcMetric arcMetric, View target) {
            mArcMetric = arcMetric;
            mTarget = new WeakReference<>(target);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            View target = mTarget.get();
            if (target != null) {
                float degree = (float) animation.getAnimatedValue();
                float x = mArcMetric.getAxisPoint().x + mArcMetric.mRadius * DimenUtil.cos(degree);
                float y = mArcMetric.getAxisPoint().y - mArcMetric.mRadius * DimenUtil.sin(degree);
                ViewCompat.setX(target, x - target.getMeasuredWidth() / 2);
                ViewCompat.setY(target, y - target.getMeasuredHeight() / 2);
            }
        }
    }
}
