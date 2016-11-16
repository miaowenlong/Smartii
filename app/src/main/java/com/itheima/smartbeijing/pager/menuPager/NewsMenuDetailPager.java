package com.itheima.smartbeijing.pager.menuPager;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.itheima.smartbeijing.R;
import com.itheima.smartbeijing.activities.MainActivity;
import com.itheima.smartbeijing.domain.CategoriesData;
import com.itheima.smartbeijing.pager.tabpager.TabDetailPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by acer on 2016/11/10.
 */

public class NewsMenuDetailPager extends BaseMenuPager {


    @InjectView(R.id.indicator)
    TabPageIndicator indicator;
    @InjectView(R.id.vp_menu_news)
    ViewPager vpMenuNewscenter;
    private View view;
    ArrayList<CategoriesData.DataBean> data;
    private List<CategoriesData.DataBean.ChildrenBean> childrens;
    List<TabDetailPager> pagers;
    private MyViewPagerAdapter adapter;


    public NewsMenuDetailPager(Activity mActivity, ArrayList<CategoriesData.DataBean> data) {
        super(mActivity);
        this.data = data;
    }

    @Override
    public View initViews() {
        view = View.inflate(mActivity, R.layout.pager_menu_news, null);
        ButterKnife.inject(this,view);
        return view;

    }

    @Override
    public void initData() {
        childrens = data.get(0).getChildren();
        System.out.println(childrens.toString());
        pagers = new ArrayList<>();
        for (int i = 0; i < childrens.size(); i++) {
            System.out.println(childrens.get(i).toString());
            TabDetailPager pager = new TabDetailPager(mActivity, childrens.get(i));
            pagers.add(pager);
        }
        childrens = data.get(0).getChildren();
        //设置viewpager的ui
        adapter = new MyViewPagerAdapter();
        vpMenuNewscenter.setAdapter(adapter);

        indicator.setViewPager(vpMenuNewscenter);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    setSlidingMenuEnable(true);
                } else {
                    setSlidingMenuEnable(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }
    /**
     * 控制侧边栏滑动
     * @param enable
     */
    private void setSlidingMenuEnable(boolean enable) {
        //获取侧边栏对象
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();

        if (enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }



    public class MyViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return childrens.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            System.out.println(pagers.size());
            if (pagers.size()==0) {
                return null;
            }
            TabDetailPager pager = pagers.get(position);
            View view = pager.mRootView;
            pager.initData();
            container.addView(view);
           return  view;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return childrens.get(position).getTitle();
        }


    }



}
