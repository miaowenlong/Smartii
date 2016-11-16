package com.itheima.smartbeijing.pager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.itheima.smartbeijing.activities.MainActivity;
import com.itheima.smartbeijing.fragment.MenuFragment;

/**
 * Created by acer on 2016/11/9.
 */

public class GovAffairsPager extends BasePager {
    public GovAffairsPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        super.initData();
        //给帧布局填充对象
        TextView textView = new TextView(mActivity);
        textView.setText("政务");
        textView.setTextSize(22);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);

        flContainerBasePager.addView(textView);

        tvTitle.setText("政务");
        ibMenu.setVisibility(View.VISIBLE);
        ibMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainUI = (MainActivity) mActivity;
                MenuFragment fragment = mainUI.getMenuFragment();
                fragment.toggle();
            }
        });
    }
}
