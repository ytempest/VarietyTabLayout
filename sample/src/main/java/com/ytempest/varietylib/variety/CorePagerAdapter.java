package com.ytempest.varietylib.variety;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author heqidu
 * @since 2020/11/23
 */
public abstract class CorePagerAdapter<Item> extends PagerAdapter {

    private final List<Item> mItems = new ArrayList<>();
    private final Map<Integer, View> mViews = new HashMap<>();
    private LayoutInflater mInflater;

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
        return obj == view;
    }

    public Item getItem(int pos) {
        return mItems.get(pos);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(container.getContext());
        }
        View view = mViews.get(position);
        if (view == null) {
            Item item = getItem(position);
            view = onCreateView(mInflater, container, item, position);
            mViews.put(position, view);
            container.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = mViews.remove(position);
        container.removeView(view);
    }

    @NonNull
    protected abstract View onCreateView(LayoutInflater inflater, ViewGroup container, Item data, int position);

    public void display(List<Item> items) {
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    public void update(int position, Item data) {
        if (position >= 0 && position < mItems.size()) {
            mItems.set(position, data);
            notifyDataSetChanged();
        }
    }

    public void add(Item item, int position) {
        if (item != null) {
            mItems.add(position, item);
            notifyDataSetChanged();
        }
    }

    public void removeAt(int position) {
        if (position >= 0 && position < mItems.size()) {
            mItems.remove(position);
            notifyDataSetChanged();
        }
    }
}
