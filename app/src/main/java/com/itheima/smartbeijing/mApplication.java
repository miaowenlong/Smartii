package com.itheima.smartbeijing;

import android.app.Application;

import com.itheima.smartbeijing.utils.LoggerUtil;

/**
 * Created by acer on 2016/11/15.
 */

public class mApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LoggerUtil.isDebug = true;
    }
}
