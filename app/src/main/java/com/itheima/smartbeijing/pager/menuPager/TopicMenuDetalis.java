package com.itheima.smartbeijing.pager.menuPager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by acer on 2016/11/10.
 */

public class TopicMenuDetalis extends BaseMenuPager {


    public TopicMenuDetalis(Activity activity) {
        super(activity);
    }

    @Override
    public View initViews() {
        TextView view = new TextView(mActivity);
        view.setText("菜单详情页-专题");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        return view;
    }

}
