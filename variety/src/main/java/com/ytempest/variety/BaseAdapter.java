package com.ytempest.variety;

import com.ytempest.variety.indicator.IIndicatorDecorator;
import com.ytempest.variety.tab.ITabDecorator;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <Data> data type of tab in {@link VarietyTabLayout}, this data type need keep consistent
 *               with {@link ITabDecorator<Data>} and {@link BaseAdapter#getTabDecorator()}
 * @author ytempest
 * @since 2021/1/11
 */
public abstract class BaseAdapter<Data> {

    private final List<Data> mDataList = new ArrayList<>();

    public BaseAdapter(List<Data> list) {
        mDataList.addAll(list);
    }

    public List<Data> getDataList() {
        return mDataList;
    }

    public abstract ITabDecorator<Data> getTabDecorator();

    public abstract IIndicatorDecorator getIndicatorDecorator();
}
