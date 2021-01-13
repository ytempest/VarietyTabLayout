package com.ytempest.varietylib;

import android.support.annotation.IntDef;

import com.ytempest.variety.indicator.DynamicIndicator;
import com.ytempest.variety.indicator.FixedIndicator;
import com.ytempest.variety.indicator.IIndicatorDecorator;
import com.ytempest.variety.indicator.NylonIndicator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author heqidu
 * @since 2021/1/12
 */
public class IndicatorControl {

    public static IIndicatorDecorator getIndicatorByStyle(@Style int style) {
        switch (style) {
            default:
            case Style.FIXED:
                return new FixedIndicator();

            case Style.DYNAMIC_FRAME:
                return new DynamicIndicator();

            case Style.NYLON:
                return new NylonIndicator();
        }
    }


    @IntDef({
            Style.FIXED,
            Style.DYNAMIC_FRAME,
            Style.NYLON,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Style {
        int FIXED = 1;
        int DYNAMIC_FRAME = 2;
        int NYLON = 3;
    }

    public enum Item {
        FIXED(Style.FIXED, "固定小条"),
        DYNAMIC_FRAME(Style.DYNAMIC_FRAME, "动态选框"),
        NYLON(Style.NYLON, "尼龙效果"),
        ;
        final int style;
        final String text;

        Item(@Style int style, String text) {
            this.style = style;
            this.text = text;
        }
    }

}
