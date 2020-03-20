package com.tisanehealth.Helper;

import android.content.Context;
import android.content.res.Resources;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;



public class MovingViewPager extends ViewPager implements ViewPager.PageTransformer {
    public static final String TAG = "KKViewPager";
    private float MAX_SCALE = 0.0f;
    private int mPageMargin;
    private boolean animationEnabled=true;
    private boolean fadeEnabled=false;
    private  float fadeFactor=0.5f;


    public MovingViewPager(Context context) {
        this(context, null);
    }

    public MovingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClipChildren(false);
        setClipToPadding(false);
        //setOverScrollMode(2);
        setPageTransformer(true, this);
        setOffscreenPageLimit(3);
        mPageMargin = dp2px(context.getResources(), 20);
        setPadding(mPageMargin, 10, mPageMargin, 0);
    }

    public int dp2px(Resources resource, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resource.getDisplayMetrics());
    }
    public void setAnimationEnabled(boolean enable) {
        this.animationEnabled = enable;
    }

    public void setFadeEnabled(boolean fadeEnabled) {
        this.fadeEnabled = fadeEnabled;
    }

    public void setFadeFactor(float fadeFactor) {
        this.fadeFactor = fadeFactor;
    }

    @Override
    public void setPageMargin(int marginPixels) {
        mPageMargin = marginPixels;
        setPadding(mPageMargin, 10, mPageMargin, 0);
    }

    @Override
    public void transformPage(View page, float position) {
        if (mPageMargin <= 0|| !animationEnabled)
            return;
        page.setPadding(mPageMargin/3 , 0, mPageMargin/3 , 0);

        if (MAX_SCALE == 0.0f && position > 0.0f && position < 1.0f) {
            MAX_SCALE = position;
        }
        position = position - MAX_SCALE;
        float absolutePosition = Math.abs(position);
        if (position <= -1.0f || position >= 1.0f) {
            if(fadeEnabled)
                page.setAlpha(fadeFactor);
            // Page is not visible -- stop any running animations

        } else if (position == 0.0f) {

            // Page is selected -- reset any views if necessary
            page.setScaleX((1 + MAX_SCALE));
            page.setScaleY((1 + MAX_SCALE));
            page.setAlpha(1);
        } else {
            page.setScaleX(1 + MAX_SCALE * (1 - absolutePosition));
            page.setScaleY(1 + MAX_SCALE * (1 - absolutePosition));
            if(fadeEnabled)
                page.setAlpha( Math.max(fadeFactor, 1 - absolutePosition));
        }
    }


}