package com.itheima.smartbeijing.pager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by acer on 2016/11/9.
 */

public class SettingPager extends BasePager {
    public SettingPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        super.initData();
        //给帧布局填充对象
        TextView textView = new TextView(mActivity);
        textView.setText("设置");
        textView.setTextSize(22);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);

        flContainerBasePager.addView(textView);

        tvTitle.setText("设置");
        ibMenu.setVisibility(View.INVISIBLE);
    }
}
