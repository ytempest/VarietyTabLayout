package com.ytempest.variety;

import com.ytempest.variety.indicator.DynamicIndicator;
import com.ytempest.variety.indicator.IIndicatorDecorator;
import com.ytempest.variety.indicator.NylonIndicator;
import com.ytempest.variety.tab.ITabDecorator;
import com.ytempest.variety.tab.ScaleTabDecorator;
import com.ytempest.variety.tab.TrackTabDecorator;

import java.util.List;


/**
 * @author ytempest
 * @since 2021/1/11
 */
public class SimpleAdapter extends BaseAdapter<String> {

    public SimpleAdapter(List<String> list) {
        super(list);
    }

    @Override
    public ITabDecorator<String> getTabDecorator() {
        return new ScaleTabDecorator();
    }

    @Override
    public IIndicatorDecorator getIndicatorDecorator() {
        return new NylonIndicator();
    }
}
