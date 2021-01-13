package com.ytempest.variety;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * @author ytempest
 * @since 2021/1/11
 */
public class ColorTrackTextView extends TextView {

    // 变化的颜色覆盖原有的颜色
    public static final int CHANGE_TO_ORIGIN = 1;
    // 原有的颜色覆盖变化的颜色
    public static final int ORIGIN_TO_CHANGE = 2;
    private final Rect mRect = new Rect();
    private int mOriginColor;
    private int mChangeColor;
    private float mProgress = 0F;
    @Direction
    private int mDirection = CHANGE_TO_ORIGIN;

    public ColorTrackTextView(Context context) {
        this(context, null);
    }

    public ColorTrackTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorTrackTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ColorTrackTextView);
        mOriginColor = array.getColor(R.styleable.ColorTrackTextView_origin_color, getTextColors().getDefaultColor());
        mChangeColor = array.getColor(R.styleable.ColorTrackTextView_change_color, getTextColors().getDefaultColor());
        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        CharSequence raw = getText();
        if (TextUtils.isEmpty(raw)) {
            return;
        }
        final String text = (String) raw;
        final int width = getWidth();
        // 获取变色和原色位置
        final int boundary = (int) (mProgress * width);
        drawOriginText(canvas, text, boundary);
        drawChangeText(canvas, text, boundary);
    }

    private void drawOriginText(Canvas canvas, String text, int boundary) {
        if (mDirection == CHANGE_TO_ORIGIN) {
            // 从 boundary 到 getWidth() 一直绘制默认颜色
            drawText(canvas, text, mOriginColor, boundary, getWidth());
        } else {
            // 从 0 到 getWidth()-boundary 一直绘制默认颜色
            drawText(canvas, text, mOriginColor, 0, getWidth() - boundary);
        }
    }

    private void drawChangeText(Canvas canvas, String text, int boundary) {
        if (mDirection == CHANGE_TO_ORIGIN) {
            // 从 0 到 boundary 一直绘制变色的颜色
            drawText(canvas, text, mChangeColor, 0, boundary);
        } else {
            // 从 getWidth()-boundary 到 getWidth() 一直绘制变色的颜色
            drawText(canvas, text, mChangeColor, getWidth() - boundary, getWidth());
        }
    }

    public void drawText(Canvas canvas, String text, int color, int start, int end) {
        TextPaint paint = getPaint();
        paint.setColor(color);

        canvas.save();
        // 截取要绘制的文字部分，后面进行颜色的绘制
        mRect.set(start, 0, end, getHeight());
        canvas.clipRect(mRect);

        // 获取字体的宽度
        float textWidth = paint.measureText(text);
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        float x = getPaddingLeft() + (width - textWidth) / 2F;

        canvas.drawText(text, x, getBaseline(), paint);

        canvas.restore();
    }

    /**
     * 设置当前字体颜色绘制的进度
     */
    public void setProgress(@FloatRange(from = 0, to = 1) float progress) {
        if (mProgress != progress) {
            // 设置刷新进度
            this.mProgress = progress;
            invalidate();
        }
    }

    public void setDirection(@Direction int direction) {
        mDirection = direction;
    }

    @Direction
    public int getDirection() {
        return mDirection;
    }

    public void setOriginColor(int color) {
        mOriginColor = color;
    }

    public void setChangeColor(int color) {
        mChangeColor = color;
    }

    @IntDef({
            CHANGE_TO_ORIGIN,
            ORIGIN_TO_CHANGE
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Direction {
    }
}
