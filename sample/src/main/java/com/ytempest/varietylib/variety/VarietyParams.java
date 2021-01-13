package com.ytempest.varietylib.variety;

import java.io.Serializable;

/**
 * @author heqidu
 * @since 2021/1/12
 */
public class VarietyParams implements Serializable {

    private static final long serialVersionUID = 1L;

    public int mTabStyle;
    public int mIndicatorStyle;
    public int mTabCount;
    public boolean isContentLenRandom;

    public VarietyParams(int tabStyle, int indicatorStyle, int tabCount, boolean isRegularTabContent) {
        mTabStyle = tabStyle;
        mIndicatorStyle = indicatorStyle;
        mTabCount = tabCount;
        this.isContentLenRandom = isRegularTabContent;
    }

    @Override
    public String toString() {
        return "VarietyParams{" +
                "mTabStyle=" + mTabStyle +
                ", mIndicatorStyle=" + mIndicatorStyle +
                ", mTabCount=" + mTabCount +
                ", isRegularTabContent=" + isContentLenRandom +
                '}';
    }
}
