package com.ytempest.variety;

import com.ytempest.variety.indicator.IIndicatorDecorator;
import com.ytempest.variety.tab.ITabDecorator;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <Data> data type of tab in {@link VarietyTabLayout}, this data type need keep consistent
 *               with {@link ITabDecorator} and {@link BaseAdapter#getTabDecorator()}
 * @author ytempest
 * @since 2021/1/11
 */
public abstract class BaseAdapter<Data> {

    private final List<Data> mDataList = new ArrayList<>();
    private AdapterDataObservable mObservable;
    private AdapterDataObserver mTabLayoutObserver;

    public BaseAdapter(List<Data> list) {
        mDataList.addAll(list);
    }

    public abstract ITabDecorator<Data> getTabDecorator();

    public abstract IIndicatorDecorator getIndicatorDecorator();

    public List<Data> getDataList() {
        return mDataList;
    }

    public Data getData(int position) {
        return mDataList.get(position);
    }

    public void display(List<Data> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void update(int position, Data data) {
        if (position >= 0 && position < mDataList.size()) {
            mDataList.set(position, data);
            notifyItemChanged(position);
        }
    }

    public void add(Data data) {
        add(data, mDataList.size());
    }

    public void add(Data data, int position) {
        if (data != null) {
            mDataList.add(position, data);
            notifyItemInserted(position);
        }
    }

    public void remove(Data data) {
        int index = mDataList.indexOf(data);
        removeAt(index);
    }

    public void removeAt(int position) {
        if (position >= 0 && position < mDataList.size()) {
            mDataList.remove(position);
            notifyItemRemoved(position);
        }
    }

    void setTabLayoutObserver(AdapterDataObserver observer) {
        synchronized (this) {
            mTabLayoutObserver = observer;
        }
    }

    /**
     * Register an observer to receive callbacks related to the adapter's data changing.
     */
    public void registerDataSetObserver(AdapterDataObserver observer) {
        if (mObservable == null) {
            mObservable = new AdapterDataObservable();
        }
        mObservable.registerObserver(observer);
    }

    /**
     * Unregister an observer from callbacks related to the adapter's data changing.
     */
    public void unregisterDataSetObserver(AdapterDataObserver observer) {
        if (mObservable != null) {
            mObservable.unregisterObserver(observer);
        }
    }

    public final void notifyDataSetChanged() {
        if (mTabLayoutObserver != null) {
            mTabLayoutObserver.onDataSetChanged();
        }
        if (mObservable != null) {
            mObservable.notifyDataSetChanged();
        }
    }

    public final void notifyItemChanged(int position) {
        if (mTabLayoutObserver != null) {
            mTabLayoutObserver.onItemChanged(position);
        }
        if (mObservable != null) {
            mObservable.notifyItemChanged(position);
        }
    }

    public final void notifyItemInserted(int position) {
        if (mTabLayoutObserver != null) {
            mTabLayoutObserver.onItemInserted(position);
        }
        if (mObservable != null) {
            mObservable.notifyItemInserted(position);
        }
    }

    public final void notifyItemRemoved(int position) {
        if (mTabLayoutObserver != null) {
            mTabLayoutObserver.onItemRemoved(position);
        }
        if (mObservable != null) {
            mObservable.notifyItemRemoved(position);
        }
    }
}
