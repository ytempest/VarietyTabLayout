package com.ytempest.variety.indicator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ytempest.variety.VarietyTabLayout;


/**
 * a indicator decorator for {@link VarietyTabLayout}, you can create yourself indicator and implement
 * animation of indicator in scroll
 *
 * @author ytempest
 * @since 2021/1/11
 */
public interface IIndicatorDecorator {
    /**
     * control gravity of indicator in {@link VarietyTabLayout}
     *
     * @return {@link android.view.Gravity}
     */
    int getGravity();

    /**
     * create the indicator for {@link VarietyTabLayout}
     * you need control the width and height for indicator
     */
    View createIndicatorView(LayoutInflater inflater, ViewGroup parent);

    /**
     * update the location of indicator int {@link VarietyTabLayout}
     * <p>
     * advice change the location by leftMargin of indicator.
     * it may be shake in scrolling if you use View#setTranslationX()
     *  @param indicator      the indicator you create
     * @param currentTab     the tab of selected
     * @param nextTab        next tab of selected tab
     * @param offsetPercent offset percent, between [0,1]
     */
    void updateIndicatorLocation(View indicator, View currentTab, View nextTab, float offsetPercent);
}
