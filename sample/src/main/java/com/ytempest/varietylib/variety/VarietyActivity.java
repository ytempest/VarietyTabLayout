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
import com.ytempest.variety.listener.TabActionListener;
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
    private BaseAdapter<String> mTabAdapter;
    private Random mRandom;

    public static void start(Context context, VarietyParams params) {
        Intent intent = new Intent(context, VarietyActivity.class);
        Bundle value = new Bundle();
        value.putSerializable(KEY_DATA, params);
        intent.putExtra(KEY_DATA, value);
        context.startActivity(intent);
    }

    private VarietyTabLayout mTabLayout;
    private ViewPager mViewPager;
    private CorePagerAdapter<String> mPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variety);
        mRandom = new Random();
        mParams = getParams();

        Log.d(TAG, "onCreate: mParams=" + mParams);
        if (mParams == null) {
            Toast.makeText(this, "参数异常", Toast.LENGTH_SHORT).show();
            return;
        }

        mTabLayout = findViewById(R.id.varietytablayout);
        mViewPager = findViewById(R.id.viewpager);
        mTabLayout.addTabActionListener(new TabActionListener() {
            @Override
            public void onTabClick(View tab, int position) {
                Log.d(TAG, "onTabClick: position=" + position);
            }

            @Override
            public void onTabSelected(View tab, int position) {
                Log.d(TAG, "onTabSelected: position=" + position);
            }

            @Override
            public void onTabReleaseSelect(View tab, int position) {
                Log.d(TAG, "onTabReleaseSelect: position=" + position);
            }
        });

        mPagerAdapter = new CorePagerAdapter<String>() {
            @NonNull
            @Override
            protected View onCreateView(LayoutInflater inflater, ViewGroup container, String data, int position) {
                View view = inflater.inflate(R.layout.item_variety_content, container, false);
                TextView tv = view.findViewById(R.id.tv_variety);
                tv.setText(data);
                return view;
            }
        };
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        List<String> data = getDataList();
        mPagerAdapter.display(data);
        mTabAdapter = new BaseAdapter<String>(data) {
            @Override
            public ITabDecorator<String> getTabDecorator() {
                return TabControl.getTabByStyle(mParams.mTabStyle);
            }

            @Override
            public IIndicatorDecorator getIndicatorDecorator() {
                return IndicatorControl.getIndicatorByStyle(mParams.mIndicatorStyle);
            }
        };
        mTabLayout.setAdapter(mTabAdapter);

    }

    private List<String> getDataList() {
        List<String> data = new ArrayList<>(mParams.mTabCount);
        for (int i = 0; i < mParams.mTabCount; i++) {
            int textLen = mParams.isContentLenRandom ? 1 + mRandom.nextInt(4) : 2;
            StringBuilder text = new StringBuilder();
            for (int k = 0; k < textLen; k++) {
                text.append("风");
            }
            data.add(text.toString() + i);
        }
        return data;
    }

    private VarietyParams getParams() {
        Bundle bundle = getIntent().getBundleExtra(KEY_DATA);
        return (VarietyParams) bundle.get(KEY_DATA);
    }

    public void onResetAllClick(View view) {
        List<String> list = getDataList();
        mTabAdapter.display(list);
        mPagerAdapter.display(list);
    }

    public void onUpdateOneClick(View view) {
        List<String> list = mTabAdapter.getDataList();
        if (!list.isEmpty()) {
            int randomIdx = mRandom.nextInt(list.size());
            String change = mRandom.nextBoolean() ? list.get(randomIdx) + "改" : "改";
            mTabAdapter.update(randomIdx, change);
            mPagerAdapter.update(randomIdx, change);
        }
    }

    public void onJumpClick(View view) {
        int count = mViewPager.getAdapter().getCount();
        if (count > 0) {
            mViewPager.setCurrentItem(mRandom.nextInt(count), false);
        }
    }

    public void onInertOneClick(View view) {
        int size = mTabAdapter.getDataList().size();
        int position = mRandom.nextInt(size + 1);
        mTabAdapter.add("插入", position);
        mPagerAdapter.add("插入", position);
    }

    public void onRemoveOneClick(View view) {
        int size = mTabAdapter.getDataList().size();
        int position = mRandom.nextInt(size + 1);
        mTabAdapter.removeAt(position);
        mPagerAdapter.removeAt(position);
    }
}
