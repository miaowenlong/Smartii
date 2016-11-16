package com.itheima.smartbeijing.pager;

/**
 * Created by acer on 2016/11/9.
 */

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.itheima.smartbeijing.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * viewpager的页的基类
 */
public class BasePager {
    public Activity mActivity;
    public View mRootView;//当前页面布局对象

    @InjectView(R.id.ib_menu)
    protected ImageButton ibMenu;
    @InjectView(R.id.tv_title)
    protected TextView tvTitle;
    @InjectView(R.id.fl_container_base_pager)
    protected FrameLayout flContainerBasePager;//空的帧布局对象
    @InjectView(R.id.bt_photos_style)
    protected ImageButton btPhotosStyle;

    public BasePager(Activity activity) {
        mActivity = activity;
        mRootView = initView();
    }

    /**
     * 初始化
     *
     * @return
     */
    public View initView() {
        View view = View.inflate(mActivity, R.layout.base_pager, null);
        ButterKnife.inject(this, view);
        return view;
    }

    /**
     * 初始化数据
     */
    public void initData() {

    }


}
