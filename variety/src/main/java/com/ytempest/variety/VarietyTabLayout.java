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

    private final LayoutInflater mInflater;
    private final FrameLayout mRootContainer;
    private final TabGroup mTabGroup;
    private final FrameLayout mIndicatorGroup;
    private final List<TabActionListener> mTabActionListeners;
    private View mIndicator;
    private ViewPager mViewPager;
    private TrackIndicatorPageChangeListener mPageChangeListener;
    private BaseAdapter mAdapter;
    private IIndicatorDecorator mIndicatorDecorator;
    private ITabDecorator mTabDecorator;
    private TabObserver mTabObserver;
    private View.OnClickListener mTabClickListener;
    private int mSelectedPosition;
    private boolean isSmoothScroll = true;
    private boolean isDataChanged;

    public VarietyTabLayout(Context context) {
        this(context, null);
    }

    public VarietyTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VarietyTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(getContext());
        mRootContainer = new FrameLayout(context);
        addView(mRootContainer);

        mTabGroup = new TabGroup(context);
        mRootContainer.addView(mTabGroup);

        mIndicatorGroup = new FrameLayout(context);
        mRootContainer.addView(mIndicatorGroup, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

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
        mPageChangeListener.reset();
        mViewPager.addOnPageChangeListener(mPageChangeListener);
    }

    public void setAdapter(BaseAdapter adapter) {
        if (mViewPager == null) {
            throw new NullPointerException("Please setup the ViewPager by setupWithViewPager() before set adapter");
        }
        if (adapter == null) {
            throw new NullPointerException("adapter can't be null");
        }

        if (mAdapter != null) {
            mAdapter.setTabLayoutObserver(null);
        }

        reset();
        this.mAdapter = adapter;
        this.mIndicatorDecorator = mAdapter.getIndicatorDecorator();
        this.mTabDecorator = mAdapter.getTabDecorator();

        if (mTabObserver == null) {
            mTabObserver = new TabObserver();
        }
        mAdapter.setTabLayoutObserver(mTabObserver);

        // 添加Tab
        final List dataList = mAdapter.getDataList();
        for (int i = 0, count = dataList.size(); i < count; i++) {
            addTab(dataList.get(i), i);
        }

        // 添加指示器
        this.mIndicator = mIndicatorDecorator.createIndicatorView(mInflater, mRootContainer);
        mIndicatorGroup.addView(mIndicator);
        LayoutParams params = (LayoutParams) mIndicator.getLayoutParams();
        params.gravity = mIndicatorDecorator.getGravity();
        mIndicator.setLayoutParams(params);

        setSelectedTab(mSelectedPosition);
    }

    private void addTab(Object data, int position) {
        addTab(data, position, -1);
    }

    private void addTab(Object data, int position, int addIdx) {
        final View tab = mTabDecorator.createTabView(mInflater, mTabGroup, position);
        mTabDecorator.onBindTabView(tab, data, position);
        setupTabClickListener(tab);
        mTabGroup.addTabView(tab, addIdx);
    }

    private void reset() {
        mSelectedPosition = 0;
        mTabGroup.removeAllViews();
        mIndicatorGroup.removeAllViews();
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

    private void dispatchOnPageScrolled(int position, float positionOffset) {
        if (isPositionLegal(position)) {
            // 分发滚动事件给Tab，让Tab处理自身的UI状态变换
            final int nextPosition = Math.min(position + 1, mTabGroup.getTabCount() - 1);
            final View nextTab = mTabGroup.getTabAt(nextPosition);
            final View currentTab = mTabGroup.getTabAt(position);
            mTabDecorator.onNextTabOffset(nextTab, positionOffset);
            mTabDecorator.onCurrentTabOffset(currentTab, positionOffset);

            updateIndicatorLocation(currentTab, nextTab, positionOffset);

            scrollTo(calculateScrollXForTab(currentTab, nextTab, positionOffset), 0);
        }
    }

    private void updateIndicatorLocation(View current, View next, float positionOffset) {
        final View indicator = mIndicator;
        mIndicatorDecorator.updateIndicatorLocation(indicator, current, next, positionOffset);
    }

    /**
     * 计算当前Tab在屏幕居中时，{@link VarietyTabLayout}需要的偏移量
     */
    private int calculateScrollXForTab(View current, View next, float positionOffset) {
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

            if (lastPosition != mSelectedPosition) {
                for (TabActionListener listener : mTabActionListeners) {
                    listener.onTabReleaseSelect(mTabGroup.getTabAt(lastPosition), lastPosition);
                }
            }

            for (TabActionListener listener : mTabActionListeners) {
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
            if (position < 0 || position >= tabCount - 1) {
                return;
            }

            // 用于修正滚动过程中tab偏移起始和终止值控制
            fixScrollThresholdControl(position);
            mVarietyTabLayout.dispatchOnPageScrolled(position, positionOffset);
        }

        /**
         * 由于ViewPager滚动过程中的positionOffset偏移比没有提供起始值(0)和结束值(1)，故在这里添加额外
         * 处理，以为滚动过程中经历的任何一个Position提供起始值或者结束值
         *
         * @param position 当前滚动经过的位置
         */
        private void fixScrollThresholdControl(int position) {
            if (lastPosition < position) { // 向右滚动
                final int startPosition = lastPosition;
                for (int passPos = startPosition; passPos < position; ++passPos) {
                    mVarietyTabLayout.dispatchOnPageScrolled(passPos, 1);
                }

            } else if (lastPosition > position) { // 向左滚动
                final int endPosition = lastPosition;
                for (int passPos = endPosition; passPos >= position; --passPos) {
                    mVarietyTabLayout.dispatchOnPageScrolled(passPos, 0);
                }
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

        void reset() {
            lastPosition = 0;
        }
    }

    /*Data observer*/

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (isDataChanged) {
            dispatchOnPageScrolled(mSelectedPosition - 1, 1);
            dispatchOnPageScrolled(mSelectedPosition, 0);
            isDataChanged = false;
        }
    }

    private void updatePositionOnTabChanged() {
        int tabCount = mTabGroup.getTabCount();
        if (tabCount == 0) {
            mSelectedPosition = 0;

        } else if (mSelectedPosition >= tabCount) {
            mSelectedPosition = tabCount - 1;
        }
    }

    void onDataSetChanged() {
        isDataChanged = true;
        mTabGroup.removeAllViews();
        final List dataList = mAdapter.getDataList();
        for (int i = 0, size = dataList.size(); i < size; i++) {
            addTab(dataList.get(i), i);
        }

        updatePositionOnTabChanged();
        requestLayout();
    }

    void onDataChanged(int position) {
        isDataChanged = true;
        final View tab = mTabGroup.getTabAt(position);
        final Object data = mAdapter.getData(position);
        mTabDecorator.onBindTabView(tab, data, position);

        requestLayout();
    }

    void onDataInserted(int position) {
        isDataChanged = true;
        final List dataList = mAdapter.getDataList();
        final Object data = dataList.get(position);
        addTab(data, position, position);

        updatePositionOnTabChanged();
        requestLayout();
    }

    void onDataRemoved(int position) {
        isDataChanged = true;
        mTabGroup.removeTabAt(position);

        updatePositionOnTabChanged();
        requestLayout();
    }

    private class TabObserver extends AdapterDataObserver {

        @Override
        public void onDataSetChanged() {
            super.onDataSetChanged();
            VarietyTabLayout.this.onDataSetChanged();
        }

        @Override
        public void onItemChanged(int position) {
            super.onItemChanged(position);
            onDataChanged(position);
        }

        @Override
        public void onItemInserted(int position) {
            super.onItemInserted(position);
            onDataInserted(position);
        }

        @Override
        public void onItemRemoved(int position) {
            super.onItemRemoved(position);
            onDataRemoved(position);
        }
    }
}
