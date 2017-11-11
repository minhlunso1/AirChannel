package minhna.android.airchannel.view.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class CircularRevealFrameLayout extends FrameLayout {

  private float mCenterX;

  private float mCenterY;

  private boolean mIsClipOutlines;

  private boolean mIsShouldNotClipOutlines;

  private float mRadius;

  private Path mRevealPath;

  public CircularRevealFrameLayout(Context context) {
    super(context);
    init();
  }

  public CircularRevealFrameLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public CircularRevealFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public CircularRevealFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override
  public void draw(Canvas canvas) {
    if (!mIsClipOutlines || mIsShouldNotClipOutlines) {
      super.draw(canvas);
      return;
    }
    try {
      final int state = canvas.save();
      mRevealPath.reset();
      mRevealPath.addCircle(mCenterX, mCenterY, mRadius, Path.Direction.CW);
      canvas.clipPath(mRevealPath);
      super.draw(canvas);
      canvas.restoreToCount(state);
    } catch (Exception ignore) {
      mIsShouldNotClipOutlines = true;
    }
  }

  public void setClipCenter(int x, int y) {
    mCenterX = x;
    mCenterY = y;
  }

  public void setClipOutLines(boolean isClipOutlines) {
    mIsClipOutlines = isClipOutlines;
  }

  public void setRadius(float radius) {
    mRadius = radius;
    invalidate();
  }

  private void init() {
    mRevealPath = new Path();
    mIsClipOutlines = false;
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      setLayerType(LAYER_TYPE_SOFTWARE, null);
    }
    setWillNotDraw(false);
  }
}