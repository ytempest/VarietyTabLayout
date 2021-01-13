package com.ytempest.varietylib;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ytempest.varietylib.variety.VarietyActivity;
import com.ytempest.varietylib.variety.VarietyParams;


public class MainActivity extends AppCompatActivity {

    private RadioGroup mTabStyleGroup;
    private RadioGroup mIndicatorStyleGroup;
    private SeekBar mSeekBar;
    private CheckBox mCheckBox;
    private TextView mGenerateTabCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabStyleGroup = findViewById(R.id.group_tab_style);
        mIndicatorStyleGroup = findViewById(R.id.group_indicator_style);
        mCheckBox = findViewById(R.id.cb_content_style);
        mSeekBar = findViewById(R.id.seekbar_count);
        mGenerateTabCount = findViewById(R.id.tv_generate_count);

        for (TabControl.Item item : TabControl.Item.values()) {
            RadioButton button = new RadioButton(mTabStyleGroup.getContext());
            button.setTag(item);
            button.setText(item.text);
            mTabStyleGroup.addView(button);
        }
        ((RadioButton) mTabStyleGroup.getChildAt(0)).setChecked(true);

        for (IndicatorControl.Item item : IndicatorControl.Item.values()) {
            RadioButton button = new RadioButton(mIndicatorStyleGroup.getContext());
            button.setTag(item);
            button.setText(item.text);
            mIndicatorStyleGroup.addView(button);
        }
        ((RadioButton) mIndicatorStyleGroup.getChildAt(0)).setChecked(true);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mGenerateTabCount.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mSeekBar.setProgress(13);

    }

    private int getSelectTabStyle() {
        for (int i = 0; i < mTabStyleGroup.getChildCount(); i++) {
            RadioButton button = (RadioButton) mTabStyleGroup.getChildAt(i);
            if (button.isChecked()) {
                TabControl.Item item = (TabControl.Item) button.getTag();
                return item.style;
            }
        }
        return -1;
    }

    private int getSelectIndicatorStyle() {
        for (int i = 0; i < mIndicatorStyleGroup.getChildCount(); i++) {
            RadioButton button = (RadioButton) mIndicatorStyleGroup.getChildAt(i);
            if (button.isChecked()) {
                IndicatorControl.Item item = (IndicatorControl.Item) button.getTag();
                return item.style;
            }
        }
        return -1;
    }

    public void onShowVarietyTabLayoutClick(View view) {
        int tabStyle = getSelectTabStyle();
        int indicatorStyle = getSelectIndicatorStyle();
        int tabCount = Integer.parseInt(mGenerateTabCount.getText().toString());
        boolean isContentLenRandom = mCheckBox.isChecked();

        VarietyActivity.start(this, new VarietyParams(tabStyle, indicatorStyle, tabCount, isContentLenRandom));
    }
}
