package com.ytempest.variety;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;


/**
 * @author ytempest
 * @since 2021/1/11
 */
public class TabGroup extends LinearLayout {

    public TabGroup(Context context) {
        this(context, null);
    }

    public TabGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    public void addTabView(View tab) {
        addView(tab);
    }

    public void addTabView(View tab, int index) {
        addView(tab, index);
    }

    public View getTabAt(int position) {
        return getChildAt(position);
    }

    public int getTabCount() {
        return getChildCount();
    }

    public int indexOfTab(View tab) {
        return indexOfChild(tab);
    }

    public void removeTabAt(int index) {
        removeViewAt(index);
    }
}
