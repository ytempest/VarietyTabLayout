package com.ytempest.variety.tab;

import android.view.LayoutInflater;
import android.view.View;

import com.ytempest.variety.TabGroup;
import com.ytempest.variety.VarietyTabLayout;


/**
 * a tab decorator for {@link VarietyTabLayout}, you can create yourself tab and bind data for tab
 * according to different scene
 *
 * @param <Data> the data type that the tab need bind it
 * @author ytempest
 * @since 2021/1/11
 */
public interface ITabDecorator<Data> {

    /**
     * create the tab for {@link VarietyTabLayout}
     * <p>
     * Note: not support use margin in tab !
     * Note: not support use margin in tab !
     * Note: not support use margin in tab !
     */
    View createTabView(LayoutInflater inflater, TabGroup parent, int position);

    void onBindTabView(View tab, Data data, int position);

    /**
     * it provide the offset percent of selected tab when scroll ViewPager or click target tab.
     * you can change the selected tab UI status in this method
     *
     * @param tab           the tab of selected, it will be change after finish scroll
     * @param offsetPercent the offset percent, between [0,1]
     */
    void onCurrentTabOffset(View tab, float offsetPercent);

    /**
     * it provide the offset percent of next tab when scroll ViewPager or click target tab.
     * you can change the selected tab UI status in this method
     *
     * @param tab           the tab of next, it will be change after finish scroll
     * @param offsetPercent the offset percent, between [0,1]
     */
    void onNextTabOffset(View tab, float offsetPercent);
}
