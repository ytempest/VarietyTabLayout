package com.ytempest.varietylib.variety;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ytempest.variety.BaseAdapter;
import com.ytempest.variety.VarietyTabLayout;
import com.ytempest.variety.indicator.IIndicatorDecorator;
import com.ytempest.variety.tab.ITabDecorator;
import com.ytempest.varietylib.IndicatorControl;
import com.ytempest.varietylib.R;
import com.ytempest.varietylib.TabControl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author heqidu
 * @since 2021/1/12
 */
public class VarietyActivity extends AppCompatActivity {

    private static final String TAG = VarietyActivity.class.getSimpleName();

    private static final String KEY_DATA = "key_data";
    private VarietyParams mParams;

    public static void start(Context context, VarietyParams params) {
        Intent intent = new Intent(context, VarietyActivity.class);
        Bundle value = new Bundle();
        value.putSerializable(KEY_DATA, params);
        intent.putExtra(KEY_DATA, value);
        context.startActivity(intent);
    }

    private VarietyTabLayout mTabLayout;
    private ViewPager mViewPager;
    private CorePagerAdapter<String> mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variety);

        mParams = getParams();
        Log.d(TAG, "onCreate: mParams=" + mParams);
        if (mParams == null) {
            Toast.makeText(this, "参数异常", Toast.LENGTH_SHORT).show();
            return;
        }

        mTabLayout = findViewById(R.id.varietytablayout);
        mViewPager = findViewById(R.id.viewpager);

        mAdapter = new CorePagerAdapter<String>() {
            @NonNull
            @Override
            protected View onCreateView(LayoutInflater inflater, ViewGroup container, String data, int position) {
                View view = inflater.inflate(R.layout.item_variety_content, container, false);
                TextView tv = view.findViewById(R.id.tv_variety);
                tv.setText(data);
                return view;
            }
        };
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        List<String> data = getDataList();
        mAdapter.display(data);

        mTabLayout.setAdapter(new BaseAdapter<String>(data) {
            @Override
            public ITabDecorator<String> getTabDecorator() {
                return TabControl.getTabByStyle(mParams.mTabStyle);
            }

            @Override
            public IIndicatorDecorator getIndicatorDecorator() {
                return IndicatorControl.getIndicatorByStyle(mParams.mIndicatorStyle);
            }
        });

    }

    private List<String> getDataList() {
        Random random = new Random();

        List<String> data = new ArrayList<>(mParams.mTabCount);
        for (int i = 0; i < mParams.mTabCount; i++) {
            if (mParams.isContentLenRandom) {
                StringBuilder text = new StringBuilder();
                for (int k = 0, len = 1 + random.nextInt(4); k < len; k++) {
                    text.append("风");
                }
                data.add(text.toString());

            } else {
                data.add("风风");
            }
        }
        return data;
    }

    private VarietyParams getParams() {
        Bundle bundle = getIntent().getBundleExtra(KEY_DATA);
        return (VarietyParams) bundle.get(KEY_DATA);
    }
}
