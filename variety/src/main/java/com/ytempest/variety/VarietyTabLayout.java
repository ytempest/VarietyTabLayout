package com.ytempest.variety;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import com.ytempest.variety.indicator.IIndicatorDecorator;
import com.ytempest.variety.listener.TabActionListener;
import com.ytempest.variety.tab.ITabDecorator;

import java.util.ArrayList;
import java.util.List;


/**
 * @author ytempest
 * @since 2021/1/11
 */
public class VarietyTabLayout extends HorizontalScrollView {

    private static final String TAG = VarietyTabLayout.class.getSimpleName();

    private final FrameLayout mRootContainer;
    private final TabGroup mTabGroup;
    private View mIndicator;
    private ViewPager mViewPager;
    private TrackIndicatorPageChangeListener mPageChangeListener;
    private BaseAdapter mAdapter;
    private IIndicatorDecorator mIndicatorDecorator;
    private ITabDecorator mTabDecorator;
    private final List<TabActionListener> mTabActionListeners;
    private final Runnable mDefSelectTask = new Runnable() {
        @Override
        public void run() {
            setSelectedTab(mSelectedPosition);
        }
    };
    private View.OnClickListener mTabClickListener;
    private int mSelectedPosition;
    private boolean isSmoothScroll = true;

    public VarietyTabLayout(Context context) {
        this(context, null);
    }

    public VarietyTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VarietyTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mRootContainer = new FrameLayout(context);
        addView(mRootContainer);

        mTabGroup = new TabGroup(context);
        mRootContainer.addView(mTabGroup);

        mTabActionListeners = new ArrayList<>();
    }

    public TabGroup getTabGroup() {
        return mTabGroup;
    }

    public BaseAdapter getAdapter() {
        return mAdapter;
    }

    public void setupWithViewPager(ViewPager viewPager) {
        if (mViewPager != null && mPageChangeListener != null) {
            mViewPager.removeOnPageChangeListener(mPageChangeListener);
        }

        this.mViewPager = viewPager;
        if (mPageChangeListener == null) {
            mPageChangeListener = new TrackIndicatorPageChangeListener(this);
        }
        mViewPager.addOnPageChangeListener(mPageChangeListener);
    }

    public void setAdapter(BaseAdapter adapter) {
        if (mViewPager == null) {
            throw new NullPointerException("Please setup the ViewPager by setupWithViewPager() before set adapter");
        }
        if (adapter == null) {
            throw new NullPointerException("adapter can't be null");
        }

        reset();
        this.mAdapter = adapter;
        this.mIndicatorDecorator = mAdapter.getIndicatorDecorator();
        this.mTabDecorator = mAdapter.getTabDecorator();

        LayoutInflater inflater = LayoutInflater.from(getContext());
        // 添加Tab
        final List dataList = mAdapter.getDataList();
        for (int i = 0, count = dataList.size(); i < count; i++) {
            final View tab = mTabDecorator.createTabView(inflater, mTabGroup, i);
            mTabDecorator.onBindTabView(tab, dataList.get(i), i);
            setupTabClickListener(tab);
            mTabGroup.addTabView(tab);
        }
        if (!dataList.isEmpty()) {
            // 默认选中第一个
            removeCallbacks(mDefSelectTask);
            post(mDefSelectTask);
        }

        // 添加指示器
        this.mIndicator = mIndicatorDecorator.createIndicatorView(inflater, mRootContainer);
        mRootContainer.addView(mIndicator);
        LayoutParams params = (LayoutParams) mIndicator.getLayoutParams();
        params.gravity = mIndicatorDecorator.getGravity();
        mIndicator.setLayoutParams(params);
    }

    private void reset() {
        mSelectedPosition = 0;
        mTabGroup.removeAllViews();
        mRootContainer.removeView(mIndicator);
    }

    private void setupTabClickListener(View tab) {
        if (mTabClickListener == null) {
            mTabClickListener = new OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = mTabGroup.indexOfTab(view);
                    for (TabActionListener listener : mTabActionListeners) {
                        listener.onTabClick(view, position);
                    }
                    mViewPager.setCurrentItem(position, isSmoothScroll);
                }
            };
        }
        tab.setOnClickListener(mTabClickListener);
    }

    private void dispatchOnPageScrolled(int position, int nextPosition, float positionOffset) {
        // 分发滚动事件给Tab，让Tab处理自身的UI状态变换
        mTabDecorator.onNextTabOffset(mTabGroup.getTabAt(nextPosition), positionOffset);
        mTabDecorator.onCurrentTabOffset(mTabGroup.getTabAt(position), positionOffset);

        updateIndicatorLocation(position, nextPosition, positionOffset);

        scrollTo(calculateScrollXForTab(position, nextPosition, positionOffset), 0);
    }

    private void updateIndicatorLocation(int position, int nextPosition, float positionOffset) {
        final View indicator = mIndicator;
        final View current = mTabGroup.getChildAt(position);
        final View next = mTabGroup.getChildAt(nextPosition);

        mIndicatorDecorator.updateIndicatorLocation(indicator, current, next, positionOffset);
    }

    /**
     * 计算当前Tab在屏幕居中时，{@link VarietyTabLayout}需要的偏移量
     */
    private int calculateScrollXForTab(int position, int nexIdx, float positionOffset) {
        final View current = mTabGroup.getChildAt(position);
        final View next = mTabGroup.getChildAt(nexIdx);
        final float totalOffset = current.getLeft() + current.getWidth() * positionOffset;

        // 当前tab的宽度，不同tab不同的宽度，当前tab会切换，所以这有变化范围
        float tabWidthChangeRange = current.getWidth() + (next.getWidth() - current.getWidth()) * positionOffset;
        // 当前tab在屏幕居中需要的左边距
        float left = (getWidth() - tabWidthChangeRange) / 2F;
        return (int) (totalOffset - left);
    }

    private void setSelectedTab(int position) {
        if (isPositionLegal(position)) {
            final int lastPosition = mSelectedPosition;
            mSelectedPosition = position;

            for (TabActionListener listener : mTabActionListeners) {
                listener.onTabReleaseSelect(mTabGroup.getTabAt(lastPosition), lastPosition);

                listener.onTabSelected(mTabGroup.getTabAt(mSelectedPosition), mSelectedPosition);
            }
        }
    }

    public boolean isPositionLegal(int position) {
        return position >= 0 && position < mTabGroup.getTabCount();
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    public void setSmoothScroll(boolean smooth) {
        this.isSmoothScroll = smooth;
    }

    public void addTabActionListener(TabActionListener listener) {
        if (!mTabActionListeners.contains(listener)) {
            mTabActionListeners.add(listener);
        }
    }

    public void removeTabActionListener(TabActionListener listener) {
        mTabActionListeners.remove(listener);
    }

    public void clearTabActionListener() {
        mTabActionListeners.clear();
    }

    private static class TrackIndicatorPageChangeListener implements ViewPager.OnPageChangeListener {

        private final VarietyTabLayout mVarietyTabLayout;
        private int lastPosition;

        TrackIndicatorPageChangeListener(VarietyTabLayout view) {
            mVarietyTabLayout = view;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            final int tabCount = mVarietyTabLayout.getTabGroup().getTabCount();
            final int nextPosition = tabCount == 1 ? position : position + 1;
            if (nextPosition < 0 || nextPosition >= tabCount) {
                return;
            }

            // 用于修正滚动过程中tab偏移起始和终止值控制
            fixScrollThresholdControl(position);
            mVarietyTabLayout.dispatchOnPageScrolled(position, nextPosition, positionOffset);
        }

        /**
         * 由于ViewPager滚动过程中的positionOffset偏移比没有提供起始值(0)和结束值(1)，故在这里添加额外
         * 处理，以为滚动过程中经历的任何一个Position提供起始值或者结束值
         *
         * @param position 当前滚动经过的位置
         */
        private void fixScrollThresholdControl(int position) {
            if (lastPosition < position) { // 向右滚动
                mVarietyTabLayout.dispatchOnPageScrolled(lastPosition, position, 1);
            } else if (lastPosition > position) { // 向左滚动
                mVarietyTabLayout.dispatchOnPageScrolled(lastPosition, lastPosition + 1, 0);
            }
            lastPosition = position;
        }

        @Override
        public void onPageSelected(int position) {
            mVarietyTabLayout.setSelectedTab(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

}
