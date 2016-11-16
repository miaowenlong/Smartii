package com.itheima.smartbeijing.fragment;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.itheima.smartbeijing.R;
import com.itheima.smartbeijing.activities.MainActivity;
import com.itheima.smartbeijing.pager.BasePager;
import com.itheima.smartbeijing.pager.GovAffairsPager;
import com.itheima.smartbeijing.pager.HomePager;
import com.itheima.smartbeijing.pager.NewsCenterPager;
import com.itheima.smartbeijing.pager.SettingPager;
import com.itheima.smartbeijing.pager.SmartServicePager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by acer on 2016/11/8.
 */

public class ContentFragment extends BaseFragment {
    @InjectView(R.id.vp_content)
    ViewPager vpContent;
    protected List<BasePager> mPagers;
    @InjectView(R.id.rb_tab_home)
    RadioButton rbTabHome;
    @InjectView(R.id.rb_tab_newcenter)
    RadioButton rbTabNewcenter;
    @InjectView(R.id.rb_tab_smartservice)
    RadioButton rbTabSmartservice;
    @InjectView(R.id.rb_tab_govaffairs)
    RadioButton rbTabGovaffairs;
    @InjectView(R.id.rb_tab_setting)
    RadioButton rbTabSetting;
    @InjectView(R.id.rg_parent)
    RadioGroup rgParent;


    @Override
    protected View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        return view;
    }

    /**
     * 思路：
     * 1.在ContentFragment中创建集合，把五个页面的对象添加到集合中
     * 2.ViewPager设置adapter ，把五个页面view对象返回，并且调用initData方法
     */
    @Override
    public void initData() {
        super.initData();

        mPagers = new ArrayList<>();
        mPagers.add(new HomePager(mActivity));
        mPagers.add(new NewsCenterPager(mActivity));
        mPagers.add(new SmartServicePager(mActivity));
        mPagers.add(new GovAffairsPager(mActivity));
        mPagers.add(new SettingPager(mActivity));

        //给viewpager设置适配器
        vpContent.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mPagers.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                BasePager pager = mPagers.get(position);
                View view = pager.mRootView;//获取当前页面布局
                //初始化数据
                pager.initData();
                container.addView(view);

                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });

        //给下边栏设置点击事件
        rgParent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_tab_home:
                        //首页
                        //设置没有滚动效果
                        vpContent.setCurrentItem(0, false);
                        setSlidingMenuEnabled(false);
                        break;

                    case R.id.rb_tab_newcenter:
                        vpContent.setCurrentItem(1, false);
                        setSlidingMenuEnabled(true);
                        break;
                    case R.id.rb_tab_smartservice:
                        vpContent.setCurrentItem(2, false);
                        setSlidingMenuEnabled(true);
                        break;
                    case R.id.rb_tab_govaffairs:
                        vpContent.setCurrentItem(3, false);
                        setSlidingMenuEnabled(true);
                        break;
                    case R.id.rb_tab_setting:
                        vpContent.setCurrentItem(4, false);
                        setSlidingMenuEnabled(false);
                        break;

                }
            }
        });
        rbTabHome.setChecked(true);

        //由于viewpager会预加载下一页，所以数据加载时要等到页面真正切换
        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BasePager pager = mPagers.get(position);
                pager.initData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void setSlidingMenuEnabled(boolean enabled) {
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();

        if (enabled) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }
    //获取新闻中心的对象
    public NewsCenterPager getNewsCenterPager() {
        NewsCenterPager pager = (NewsCenterPager) mPagers.get(1);
        return pager;
    }
}
