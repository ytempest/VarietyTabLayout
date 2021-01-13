package com.ytempest.variety.indicator;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.FloatRange;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ytempest.variety.R;

/**
 * @author heqidu
 * @since 2021/1/12
 */
public class NylonIndicator implements IIndicatorDecorator {

    private int mWidth;
    private int mHeight;
    private final Params mParams;

    public NylonIndicator() {
        this(new Params());
    }

    public NylonIndicator(Params params) {
        mParams = params;
    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    public final View createIndicatorView(LayoutInflater inflater, ViewGroup parent) {
        Context context = parent.getContext();
        mWidth = dpToPx(context, mParams.width);
        mHeight = dpToPx(context, mParams.height);

        View indicator = new View(context);
        if (mParams.indicatorBg != null) {
            indicator.setBackgroundDrawable(mParams.indicatorBg);
        } else {
            indicator.setBackgroundResource(mParams.indicatorBgId);
        }
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(mWidth, mHeight);
        indicator.setLayoutParams(params);
        return indicator;
    }

    @Override
    public void updateIndicatorLocation(View indicator, View currentTab, View nextTab, float offsetPercent) {
        indicator.setPivotY(indicator.getHeight() / 2F);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) indicator.getLayoutParams();
        // 当前tab中心和下一个tab中心的距离
        final float distance = nextTab.getRight() - nextTab.getWidth() / 2F - (currentTab.getRight() - currentTab.getWidth() / 2F);

        final float pullThreshold = mParams.pullThreshold;
        final float scaleRatio = mParams.scaleRatio;
        if (offsetPercent <= pullThreshold) {
            // 将当前的偏移比例映射在范围[0,1]内
            float percent = offsetPercent / pullThreshold;

            params.leftMargin = (int) (currentTab.getLeft() + (currentTab.getWidth() - mWidth) / 2F);
            params.width = (int) (mWidth + distance * percent);
            indicator.setLayoutParams(params);
            indicator.setScaleY(1F - (1F - scaleRatio) * percent);

        } else {
            // 将当前的偏移比例映射在范围(0,1]内
            float percent = (offsetPercent - pullThreshold) / (1 - pullThreshold);

            final float curMargin = (currentTab.getWidth() - mWidth) / 2F * (1 - percent);
            final float nextMargin = (nextTab.getWidth() - mWidth) / 2F * percent;
            final float totalOffset = currentTab.getLeft() + currentTab.getWidth() * percent;
            params.leftMargin = (int) (curMargin + totalOffset + nextMargin);
            params.width = (int) (mWidth + distance * (1 - percent));
            indicator.setLayoutParams(params);
            indicator.setScaleY(scaleRatio + (1 - scaleRatio) * percent);
        }
    }

    private int dpToPx(Context context, float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
    }

    public static class Params {
        // indicator宽度
        private float width = 20F;
        // indicator高度
        private float height = 2.5F;
        // indicator的背景
        private int indicatorBgId = R.drawable.bg_indicator_nylon;
        private Drawable indicatorBg;
        // 尼龙绳形变的比例 shape
        private float scaleRatio = 0.6F;
        // 一个滚动动作中尼龙绳开始形变的时机，范围在[0,1]
        private float pullThreshold = 0.7F;

        /**
         * set width of indicator, unit: dp
         */
        public Params setWidth(float widthDp) {
            this.width = widthDp;
            return this;
        }

        /**
         * set height of indicator, unit: dp
         */
        public Params setHeight(float heightDp) {
            this.height = heightDp;
            return this;
        }

        public Params setIndicatorBg(Drawable indicatorBg) {
            this.indicatorBg = indicatorBg;
            return this;
        }

        public Params setIndicatorBgId(int indicatorBgId) {
            this.indicatorBgId = indicatorBgId;
            return this;
        }

        /**
         * transform ratio of the nylon rope, range in (0,1]
         */
        public Params setScaleRatio(@FloatRange(from = 0, to = 1) float scaleRatio) {
            this.scaleRatio = scaleRatio;
            return this;
        }

        public Params setPullThreshold(@FloatRange(from = 0, to = 1, fromInclusive = false, toInclusive = false) float pullThreshold) {
            this.pullThreshold = pullThreshold;
            return this;
        }
    }
}
