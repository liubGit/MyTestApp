package com.xrefresh_library.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.CheckBox;

/**
 * Created by luokj on 2015/11/25.
 */
public class SquareCheckedText extends CheckBox {

    {
        setButtonDrawable(new BitmapDrawable());
    }

    public SquareCheckedText(Context context) {
        super(context);
    }

    public SquareCheckedText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareCheckedText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
