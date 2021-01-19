package com.ytempest.variety.indicator;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author ytempest
 * @since 2021/1/11
 */
public class DynamicIndicator implements IIndicatorDecorator {

    private static final int DEF_COLOR = 0x55AAAAAA;
    private static final int INVALID = -1;
    private int originLeftMargin = INVALID;
    private int originRightMargin = INVALID;

    @Override
    public int getGravity() {
        return Gravity.CENTER_VERTICAL;
    }

    @Override
    public View createIndicatorView(LayoutInflater inflater, ViewGroup parent) {
        View view = new View(parent.getContext());
        view.setBackgroundColor(DEF_COLOR);
        return view;
    }

    @Override
    public void updateIndicatorLocation(View indicator, View currentTab, View nextTab, float offsetPercent) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) indicator.getLayoutParams();
        if (originLeftMargin == INVALID) {
            originLeftMargin = params.leftMargin;
        }
        if (originRightMargin == INVALID) {
            originRightMargin = params.rightMargin;
        }

        final int totalOffset = (int) (currentTab.getLeft() + currentTab.getWidth() * offsetPercent);
        final int widthChangeRange = (int) ((nextTab.getWidth() - currentTab.getWidth()) * offsetPercent);

        params.leftMargin = originLeftMargin + totalOffset;
        params.width = currentTab.getWidth() - originLeftMargin - originRightMargin + widthChangeRange;
        params.height = currentTab.getHeight() - params.topMargin - params.bottomMargin;
        indicator.setLayoutParams(params);
    }
}
