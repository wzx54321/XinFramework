package com.xin.framework.xinframwork.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 定制含有滑动监听的ScrollView
 */
public class CustomScrollView extends ScrollView {
    private OnScrollChangeListener listener;

    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (listener != null){
            listener.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public void setOnScrollChangeListener(OnScrollChangeListener listener){
        this.listener = listener;
    }

    public interface OnScrollChangeListener{
        void onScrollChanged(int x, int y, int oldX, int oldY);
    }
}
