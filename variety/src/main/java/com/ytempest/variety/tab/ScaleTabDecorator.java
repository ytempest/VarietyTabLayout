package com.ytempest.variety.tab;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ytempest.variety.R;
import com.ytempest.variety.TabGroup;


/**
 * @author heqidu
 * @since 2021/1/12
 */
public class ScaleTabDecorator implements ITabDecorator<String> {

    @Override
    public final View createTabView(LayoutInflater inflater, TabGroup parent, int position) {
        View view = onCreateTabView(inflater, parent, position);
        float scale = getScaleRatio();
        view.setScaleX(scale);
        view.setScaleY(scale);
        return view;
    }

    protected float getScaleRatio() {
        return 0.85F;
    }

    protected View onCreateTabView(LayoutInflater inflater, TabGroup parent, int position) {
        return inflater.inflate(R.layout.tab_scale_text_view, parent, false);
    }

    @Override
    public void onBindTabView(View tab, String text, int position) {
        TextView view = (TextView) tab;
        view.setText(text);
    }

    @Override
    public void onCurrentTabOffset(View tab, float offsetPercent) {
        final float ratio = getScaleRatio();
        final float scale = ratio - (ratio - 1) * (1 - offsetPercent);
        tab.setPivotX(tab.getWidth() / 2F);
        tab.setPivotY(tab.getHeight() / 2F);
        tab.setScaleX(scale);
        tab.setScaleY(scale);
    }

    @Override
    public void onNextTabOffset(View tab, float offsetPercent) {
        final float ratio = getScaleRatio();
        final float scale = ratio - (ratio - 1) * offsetPercent;
        tab.setPivotX(tab.getWidth() / 2F);
        tab.setPivotY(tab.getHeight() / 2F);
        tab.setScaleX(scale);
        tab.setScaleY(scale);
    }
}
