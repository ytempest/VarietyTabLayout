package com.ytempest.variety.indicator;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ytempest.variety.R;

/**
 * @author heqidu
 * @since 2021/1/10
 */
public class FixedIndicator implements IIndicatorDecorator {

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    public View createIndicatorView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.indicator_fixed, parent, false);
    }

    @Override
    public void updateIndicatorLocation(View indicator, View currentTab, View nextTab, float offsetPercent) {
        final int indicatorW = indicator.getWidth();
        // indicator在当前tab居中需要的左边距
        final float curMargin = (currentTab.getWidth() - indicatorW) / 2F * (1 - offsetPercent);
        // indicator在下一个tab居中需要的左边距
        final float nextMargin = (nextTab.getWidth() - indicatorW) / 2F * offsetPercent;
        // 滚动过程中indicator距离左边距的距离
        final float totalOffset = currentTab.getLeft() + currentTab.getWidth() * offsetPercent;

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) indicator.getLayoutParams();
        params.leftMargin = (int) (curMargin + totalOffset + nextMargin);
        indicator.setLayoutParams(params);
    }
}
