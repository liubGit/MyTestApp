package com.xrefresh_library.Refresh;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.animation.DecelerateInterpolator;

import com.xrefresh_library.R;

/**
 * Created by liub on 2017/11/6 .
 */

public class HMLoadingDrawable extends Drawable {
    private static final String TAG = "drawable";
    private RectF rectF1;
    private RectF rectF2;
    private RectF rectF3;
    private RectF rectF4;
    private Paint paint1;
    private Paint paint2;
    private Paint paint3;
    private Paint paint4;
    private final int size;
    private final int corner;
    private final int space;
    private final int distance;
    private ValueAnimator valueAnimator1;
    private ValueAnimator valueAnimator2;
    private ValueAnimator valueAnimator3;
    private ValueAnimator valueAnimator4;
    private boolean stop = true;
    private Handler animatorHandler = new Handler();
    private Runnable repeatTask = new Runnable() {
        public void run() {
            HMLoadingDrawable.this.start();
        }
    };

    public HMLoadingDrawable(Context context) {
        corner = context.getResources().getDimensionPixelOffset(R.dimen.loading_point_corner);
        size = context.getResources().getDimensionPixelOffset(R.dimen.loading_point_size);
        space = context.getResources().getDimensionPixelOffset(R.dimen.loading_point_space);
        distance = context.getResources().getDimensionPixelOffset(R.dimen.loading_point_distance);
        reset();
        paint1 = new Paint();
        paint1.setColor(context.getResources().getColor(R.color.banner_point_gray));
        paint2 = new Paint();
        paint2.setColor(context.getResources().getColor(R.color.colorPrimary));
        paint3 = new Paint();
        paint3.setColor(Color.RED);
        paint4 = new Paint();
        paint4.setColor(context.getResources().getColor(R.color.colorPrimaryDark));
    }

    private ValueAnimator createJumpAnimator(final RectF rectF) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(new int[]{distance, 0});
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                rectF.offsetTo(rectF.left, (float)((Integer)animation.getAnimatedValue()).intValue());
                HMLoadingDrawable.this.invalidateSelf();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                if(rectF == HMLoadingDrawable.this.rectF4 && !HMLoadingDrawable.this.stop) {
                    HMLoadingDrawable.this.animatorHandler.postDelayed(HMLoadingDrawable.this.repeatTask, 70L);
                }

            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setRepeatCount(1);
        valueAnimator.setDuration(2000);
        return valueAnimator;
    }

    public void start() {
        stop = false;
        valueAnimator1.start();
        animatorHandler.postDelayed(new Runnable() {
            public void run() {
                HMLoadingDrawable.this.valueAnimator2.start();
            }
        }, 100L);
        animatorHandler.postDelayed(new Runnable() {
            public void run() {
                HMLoadingDrawable.this.valueAnimator3.start();
            }
        }, 200L);
        animatorHandler.postDelayed(new Runnable() {
            public void run() {
                HMLoadingDrawable.this.valueAnimator4.start();
            }
        }, 300L);
    }

    public void stop() {
        this.stop = true;
        this.animatorHandler.post(new Runnable() {
            public void run() {
                HMLoadingDrawable.this.animatorHandler.removeMessages(0);
                HMLoadingDrawable.this.animatorHandler.removeCallbacksAndMessages(HMLoadingDrawable.this);
                HMLoadingDrawable.this.valueAnimator1.cancel();
                HMLoadingDrawable.this.valueAnimator2.cancel();
                HMLoadingDrawable.this.valueAnimator3.cancel();
                HMLoadingDrawable.this.valueAnimator4.cancel();
                HMLoadingDrawable.this.reset();
                HMLoadingDrawable.this.invalidateSelf();
            }
        });
    }

    private void reset() {
        this.rectF1 = new RectF(0.0F, (float)distance, (float)size, (float)(distance + size));
        int left2 = size + space;
        rectF2 = new RectF((float)left2, (float)distance, (float)(left2 + size), (float)(distance + size));
        int left3 = (size + space) * 2;
        rectF3 = new RectF((float)left3, (float)distance, (float)(left3 + size), (float)(distance + size));
        int left4 = (size + space) * 3;
        rectF4 = new RectF((float)left4, (float)distance, (float)(left4 + size), (float)(distance + size));
        valueAnimator1 = createJumpAnimator(rectF1);
        valueAnimator2 = createJumpAnimator(rectF2);
        valueAnimator3 = createJumpAnimator(rectF3);
        valueAnimator4 = createJumpAnimator(rectF4);
    }

    public void draw(@NonNull Canvas canvas) {
        canvas.drawRoundRect(rectF1, (float)corner, (float)corner, paint1);
        canvas.drawRoundRect(rectF2, (float)corner, (float)corner, paint2);
        canvas.drawRoundRect(rectF3, (float)corner, (float)corner, paint3);
        canvas.drawRoundRect(rectF4, (float)corner, (float)corner, paint4);
    }

    public int getIntrinsicWidth() {
        return size * 4 + space * 3;
    }

    public int getIntrinsicHeight() {
        return distance + size;
    }

    public void setAlpha(@IntRange(from = 0L,to = 255L) int alpha) {
        paint1.setAlpha(alpha);
        paint2.setAlpha(alpha);
        paint3.setAlpha(alpha);
        paint4.setAlpha(alpha);
    }

    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint1.setColorFilter(colorFilter);
        paint2.setColorFilter(colorFilter);
        paint3.setColorFilter(colorFilter);
        paint4.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }
}