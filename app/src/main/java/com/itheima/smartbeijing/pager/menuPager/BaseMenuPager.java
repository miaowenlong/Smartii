package com.itheima.smartbeijing.pager.menuPager;

import android.app.Activity;
import android.view.View;

/**
 * Created by acer on 2016/11/10.
 */

public abstract class BaseMenuPager {
    public Activity mActivity;
    public View mRootView;


    public BaseMenuPager(Activity activity) {
        mActivity = activity;
        mRootView = initViews();
    }




    public abstract View initViews();

    //初始化数据
    public void initData() {

    }
}
