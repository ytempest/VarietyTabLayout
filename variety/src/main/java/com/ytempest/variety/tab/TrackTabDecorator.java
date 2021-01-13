package com.ytempest.variety.tab;

import android.view.LayoutInflater;
import android.view.View;

import com.ytempest.variety.ColorTrackTextView;
import com.ytempest.variety.R;
import com.ytempest.variety.TabGroup;


/**
 * @author ytempest
 * @since 2021/1/11
 */
public class TrackTabDecorator implements ITabDecorator<String> {

    @Override
    public View createTabView(LayoutInflater inflater, TabGroup parent, int position) {
        return inflater.inflate(R.layout.tab_color_track_view, parent, false);
    }

    @Override
    public void onBindTabView(View view, String text, int position) {
        ColorTrackTextView colorTrackTextView = (ColorTrackTextView) view;
        colorTrackTextView.setText(text);
    }


    @Override
    public void onCurrentTabOffset(View tab, float offsetPercent) {
        ColorTrackTextView current = (ColorTrackTextView) tab;
        current.setDirection(ColorTrackTextView.ORIGIN_TO_CHANGE);
        current.setProgress(1 - offsetPercent);
    }

    @Override
    public void onNextTabOffset(View tab, float offsetPercent) {
        ColorTrackTextView next = (ColorTrackTextView) tab;
        next.setDirection(ColorTrackTextView.CHANGE_TO_ORIGIN);
        next.setProgress(offsetPercent);
    }

}
