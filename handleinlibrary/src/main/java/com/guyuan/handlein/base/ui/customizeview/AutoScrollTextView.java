package com.guyuan.handlein.base.ui.customizeview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;


public class AutoScrollTextView extends AppCompatTextView {
    public AutoScrollTextView(Context context) {
        super(context);
    }

    public AutoScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoScrollTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {//必须重写，且返回值是true
        return true;
    }
}
