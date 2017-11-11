package minhna.android.airchannel.view.custom.metric;

import android.graphics.PointF;

import java.util.Arrays;

import minhna.android.airchannel.util.DimenUtil;

public class ArcMetric {

  public static ArcMetric evaluate(float startX, float startY, float endX, float endY, float degree, Side side) {
    ArcMetric arcMetric = new ArcMetric();
    arcMetric.mStartPoint.set(startX, startY);
    arcMetric.mEndPoint.set(endX, endY);
    arcMetric.setDegree(degree);
    arcMetric.mSide = side;
    arcMetric.createAxisVariables();

    arcMetric.calcStartEndSeg();
    arcMetric.calcRadius();
    arcMetric.calcMidAxisSeg();
    arcMetric.calcMidPoint();
    arcMetric.calcAxisPoints();
    arcMetric.calcZeroPoint();
    arcMetric.calcDegrees();

    return arcMetric;
  }

  public float mAnimationDegree;

  public PointF mAxisPoint[] = new PointF[2];

  public float mEndDegree;

  public PointF mEndPoint = new PointF();

  public float mMidAxisSegment;

  //SEGMENTS. This Segments create virtual triangle except mZeroStartSegment

  public PointF mMidPoint = new PointF();

  public float mRadius;

  //Side of animation
  public Side mSide;

  public float mSideDegree;

  //DEGREES.

  public float mStartDegree;

  public float mStartEndSegment;

  public PointF mStartPoint = new PointF();

  public PointF mZeroPoint = new PointF();

  public float mZeroStartDegree;

  public float mZeroStartSegment;

  public ArcMetric() {
  }

  @Override
  public String toString() {
    return "ArcMetric{" +
        "\nmStartPoint=" + mStartPoint +
        "\n mEndPoint=" + mEndPoint +
        "\n mMidPoint=" + mMidPoint +
        "\n mAxisPoint=" + Arrays.toString(mAxisPoint) +
        "\n mZeroPoint=" + mZeroPoint +
        "\n mStartEndSegment=" + mStartEndSegment +
        "\n mRadius=" + mRadius +
        "\n mMidAxisSegment=" + mMidAxisSegment +
        "\n mZeroStartSegment=" + mZeroStartSegment +
        "\n mAnimationDegree=" + mAnimationDegree +
        "\n mSideDegree=" + mSideDegree +
        "\n mZeroStartDegree=" + mZeroStartDegree +
        "\n mStartDegree=" + mStartDegree +
        "\n mEndDegree=" + mEndDegree +
        "\n mSide=" + mSide +
        '}';
  }

  /**
   * Return evaluated end degree
   *
   * @return the end degree
   */
  public float getEndDegree() {
    return mEndDegree;
  }

  /**
   * Return evaluated start degree
   *
   * @return the start degree
   */
  public float getStartDegree() {
    return mStartDegree;
  }

  public void setDegree(float degree) {
    degree = Math.abs(degree);
    if (degree > 180) {
      setDegree(degree % 180);
    } else if (degree == 180) {
      setDegree(degree - 1);
    } else if (degree < 30) {
      setDegree(30);
    } else {
      this.mAnimationDegree = degree;
    }
  }

  public PointF getAxisPoint() {
    return mAxisPoint[mSide.value];
  }

  public void calcAxisPoints() {
    if (mStartPoint.y > mEndPoint.y || mStartPoint.y == mEndPoint.y) {
      mAxisPoint[0].x = mMidPoint.x + mMidAxisSegment * (mEndPoint.y - mStartPoint.y) / mStartEndSegment;
      mAxisPoint[0].y = mMidPoint.y - mMidAxisSegment * (mEndPoint.x - mStartPoint.x) / mStartEndSegment;

      mAxisPoint[1].x = mMidPoint.x - mMidAxisSegment * (mEndPoint.y - mStartPoint.y) / mStartEndSegment;
      mAxisPoint[1].y = mMidPoint.y + mMidAxisSegment * (mEndPoint.x - mStartPoint.x) / mStartEndSegment;
    } else {
      mAxisPoint[0].x = mMidPoint.x - mMidAxisSegment * (mEndPoint.y - mStartPoint.y) / mStartEndSegment;
      mAxisPoint[0].y = mMidPoint.y + mMidAxisSegment * (mEndPoint.x - mStartPoint.x) / mStartEndSegment;

      mAxisPoint[1].x = mMidPoint.x + mMidAxisSegment * (mEndPoint.y - mStartPoint.y) / mStartEndSegment;
      mAxisPoint[1].y = mMidPoint.y - mMidAxisSegment * (mEndPoint.x - mStartPoint.x) / mStartEndSegment;
    }
  }

  public void calcDegrees() {
    mZeroStartSegment = (float) Math.sqrt(Math.pow(mZeroPoint.x - mStartPoint.x, 2) +
        Math.pow(mZeroPoint.y - mStartPoint.y, 2));
    mZeroStartDegree = DimenUtil.acos((2 * Math.pow(mRadius, 2) - Math.pow(mZeroStartSegment, 2)) / (2 * Math.pow(mRadius, 2)));
    switch (mSide) {
      case RIGHT:
        if (mStartPoint.y <= mZeroPoint.y) {
          if (mStartPoint.y > mEndPoint.y ||
              (mStartPoint.y == mEndPoint.y && mStartPoint.x > mEndPoint.x)) {
            mStartDegree = mZeroStartDegree;
            mEndDegree = mStartDegree + mAnimationDegree;
          } else {
            mStartDegree = mZeroStartDegree;
            mEndDegree = mStartDegree - mAnimationDegree;
          }
        } else if (mStartPoint.y >= mZeroPoint.y) {
          if (mStartPoint.y < mEndPoint.y ||
              (mStartPoint.y == mEndPoint.y && mStartPoint.x > mEndPoint.x)) {
            mStartDegree = 0 - mZeroStartDegree;
            mEndDegree = mStartDegree - mAnimationDegree;
          } else {
            mStartDegree = 0 - mZeroStartDegree;
            mEndDegree = mStartDegree + mAnimationDegree;
          }
        }
        break;
      case LEFT:
        if (mStartPoint.y <= mZeroPoint.y) {
          if (mStartPoint.y > mEndPoint.y ||
              (mStartPoint.y == mEndPoint.y && mStartPoint.x < mEndPoint.x)) {
            mStartDegree = 180 - mZeroStartDegree;
            mEndDegree = mStartDegree - mAnimationDegree;
          } else {
            mStartDegree = 180 - mZeroStartDegree;
            mEndDegree = mStartDegree + mAnimationDegree;
          }
        } else if (mStartPoint.y >= mZeroPoint.y) {
          if (mStartPoint.y < mEndPoint.y ||
              (mStartPoint.y == mEndPoint.y && mStartPoint.x < mEndPoint.x)) {
            mStartDegree = 180 + mZeroStartDegree;
            mEndDegree = mStartDegree + mAnimationDegree;
          } else {
            mStartDegree = 180 + mZeroStartDegree;
            mEndDegree = mStartDegree - mAnimationDegree;
          }
        }
        break;
    }
  }

  public void calcMidAxisSeg() {
    mMidAxisSegment = mRadius * DimenUtil.sin(mSideDegree);
  }

  public void calcMidPoint() {
    mMidPoint.x = mStartPoint.x + mStartEndSegment / 2 * (mEndPoint.x - mStartPoint.x) / mStartEndSegment;
    mMidPoint.y = mStartPoint.y + mStartEndSegment / 2 * (mEndPoint.y - mStartPoint.y) / mStartEndSegment;
  }

  public void calcRadius() {
    mSideDegree = (180 - mAnimationDegree) / 2;
    mRadius = mStartEndSegment / DimenUtil.sin(mAnimationDegree) * DimenUtil.sin(mSideDegree);
  }

  public void calcStartEndSeg() {
    mStartEndSegment = (float) Math.sqrt(Math.pow(mStartPoint.x - mEndPoint.x, 2) +
        Math.pow(mStartPoint.y - mEndPoint.y, 2));

  }

  public void calcZeroPoint() {
    switch (mSide) {
      case RIGHT:
        mZeroPoint.x = mAxisPoint[Side.RIGHT.value].x + mRadius;
        mZeroPoint.y = mAxisPoint[Side.RIGHT.value].y;
        break;
      case LEFT:
        mZeroPoint.x = mAxisPoint[Side.LEFT.value].x - mRadius;
        mZeroPoint.y = mAxisPoint[Side.LEFT.value].y;
        break;
    }
  }

  public void createAxisVariables() {
    for (int i = 0; i < mAxisPoint.length; i++) {
      mAxisPoint[i] = new PointF();
    }
  }
}