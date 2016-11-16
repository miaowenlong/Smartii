package com.itheima.smartbeijing.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by acer on 2016/11/9.
 */

public class UnScrolledViewPager extends ViewPager {
    public UnScrolledViewPager(Context context) {
        super(context,null);
    }

    public UnScrolledViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //重写此方法实现对滑动事件的禁用
        return true;
    }
}
