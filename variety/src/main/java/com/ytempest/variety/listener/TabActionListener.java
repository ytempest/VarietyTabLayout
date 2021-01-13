package com.ytempest.variety.listener;

import android.view.View;

/**
 * @author ytempest
 * @since 2021/1/11
 */
public interface TabActionListener {
    void onTabClick(View tab, int position);

    void onTabSelected(View tab, int position);

    void onTabReleaseSelect(View tab, int position);
}
