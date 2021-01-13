package com.ytempest.varietylib;

import android.support.annotation.IntDef;

import com.ytempest.variety.tab.ITabDecorator;
import com.ytempest.variety.tab.ScaleTabDecorator;
import com.ytempest.variety.tab.TrackTabDecorator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author heqidu
 * @since 2021/1/12
 */
public class TabControl {

    public static ITabDecorator<String> getTabByStyle(@Style int style) {
        switch (style) {
            default:
            case Style.COLOR_CHANGE:
                return new TrackTabDecorator();

            case Style.SCALE:
                return new ScaleTabDecorator();
        }
    }


    @IntDef({
            Style.COLOR_CHANGE,
            Style.SCALE,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Style {
        int COLOR_CHANGE = 1;
        int SCALE = 2;
    }

    public enum Item {
        COLOR_CHANGE(Style.COLOR_CHANGE, "颜色变化"),
        SCALE(Style.SCALE, "缩放效果"),
        ;
        final int style;
        final String text;

        Item(@Style int style, String text) {
            this.style = style;
            this.text = text;
        }
    }
}
