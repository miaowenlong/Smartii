package com.itheima.smartbeijing.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.itheima.smartbeijing.R;
import com.itheima.smartbeijing.activities.MainActivity;
import com.itheima.smartbeijing.domain.CategoriesData;
import com.itheima.smartbeijing.pager.NewsCenterPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

/**
 * Created by acer on 2016/11/8.
 */

public class MenuFragment extends BaseFragment {

    private ListView lvMenu;
    private ArrayList<CategoriesData.DataBean> datas;
    private int mCurrentPos;//当前菜单的位置
    private MenuLvAdapter menuLvAdapter;

    @Override
    protected View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_menu, null);
        lvMenu = (ListView) view.findViewById(R.id.lv_menu);
        return view;
    }

    public void setMenuData( ArrayList<CategoriesData.DataBean> datas) {
        this.datas = datas;

        //当前选中的位置为0，避免侧边栏选中位置和菜单详情也不同步
        mCurrentPos = 0;

        menuLvAdapter = new MenuLvAdapter();
        lvMenu.setAdapter(menuLvAdapter);

        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPos = position;
                //刷新listview
                menuLvAdapter.notifyDataSetChanged();

          //收回侧边栏
                toggle();
                changeMenuDetailPager(position);
                
            }
        });
    }

    /**
     * 修改菜单详情页
     */
    private void changeMenuDetailPager(int position) {
        //修改新闻的帧布局
        //获取新闻中心的对象
        System.out.println("修改详情页");
        MainActivity mainUI = (MainActivity) mActivity;
        ContentFragment fragment = mainUI.getcContentFragment();
        NewsCenterPager pager = fragment.getNewsCenterPager();
        pager.setMenuDetailPager(position);

    }

    /**
     * 控制侧边栏开关
     */
    public void toggle() {
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        slidingMenu.toggle();
    }

    private class MenuLvAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view1 = View.inflate(mActivity, R.layout.list_item_menu, null);
            TextView textView = (TextView) view1.findViewById(R.id.tv_menu_item);

            //设置textview可用或者不可用来控制颜色
            if (mCurrentPos==position) {
                textView.setEnabled(true);
            }else {
                textView.setEnabled(false);
            }

            CategoriesData.DataBean info = (CategoriesData.DataBean) getItem(position);
            String title = info.getTitle();

            textView.setText(title);
            return  view1;
        }
    }






}
