package com.itheima.smartbeijing.activities;

import android.os.Bundle;

import com.itheima.smartbeijing.R;
import com.itheima.smartbeijing.fragment.ContentFragment;
import com.itheima.smartbeijing.fragment.MenuFragment;
import com.itheima.smartbeijing.utils.DensityUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * Created by acer on 2016/11/8.
 */

public class MainActivity extends SlidingFragmentActivity {
    private static final String TAG_CONTENT = "TAG_CONTENT";
    private static final String TAG_MENU = "TAG_LEFT_MENU";
    private SlidingMenu slidingMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setBehindContentView(R.layout.left_menu);
        slidingMenu = getSlidingMenu();
        slidingMenu.setBehindOffset(DensityUtils.dp2px(this,200));
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        initFragment();
    }

    private void initFragment() {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fl_main, new ContentFragment(),TAG_CONTENT);
        fragmentTransaction.replace(R.id.fl_menu, new MenuFragment(), TAG_MENU);
        fragmentTransaction.commit();
    }

    /**
     * 获取侧边的fragment
     */
    public MenuFragment getMenuFragment() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        MenuFragment fragment = (MenuFragment) fm.findFragmentByTag(TAG_MENU);
        return fragment;
    }
    /**
     * 获取主界面的fragment
     */
    public ContentFragment getcContentFragment() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        ContentFragment fragment = (ContentFragment) fm.findFragmentByTag(TAG_CONTENT);
        return fragment;
    }
}
