package com.itheima.smartbeijing.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * Created by acer on 2016/11/12.
 */

public class TopNewsViewPager extends ViewPager{

    private int startX;
    private int startY;

    public TopNewsViewPager(Context context) {
        super(context);
    }

    public TopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //请求父控件不要拦截
        getParent().requestDisallowInterceptTouchEvent(true);

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //移动
                int endX = (int) ev.getX();
                int endY = (int) ev.getY();

                int dx = endX - startX;
                int dy = endY - startY;

                if (Math.abs(dx) > Math.abs(dy)) {
                    //左右滑动
                    int currentItem = getCurrentItem();
                    if (dx > 0) {
                        //右划
                        if (currentItem == 0) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    } else {
                        //左划
                        int count = getAdapter().getCount();
                        if (currentItem == count - 1) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }

                } else {
                    //上下滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }

        return super.onTouchEvent(ev);
    }
}
