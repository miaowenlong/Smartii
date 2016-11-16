package com.itheima.smartbeijing.pager;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.itheima.smartbeijing.R;
import com.itheima.smartbeijing.activities.MainActivity;
import com.itheima.smartbeijing.domain.CategoriesData;
import com.itheima.smartbeijing.fragment.MenuFragment;
import com.itheima.smartbeijing.pager.menuPager.BaseMenuPager;
import com.itheima.smartbeijing.pager.menuPager.InteractMenuDetailPager;
import com.itheima.smartbeijing.pager.menuPager.NewsMenuDetailPager;
import com.itheima.smartbeijing.pager.menuPager.PhotoMenuDetailsPager;
import com.itheima.smartbeijing.pager.menuPager.TopicMenuDetalis;
import com.itheima.smartbeijing.utils.CacheUtils;
import com.itheima.smartbeijing.utils.LoggerUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by acer on 2016/11/9.
 */

public class NewsCenterPager extends BasePager {

    private CategoriesData data;
    private ArrayList<BaseMenuPager> mPagers;
    private URL url;
    private String path;
    private PhotoMenuDetailsPager photoMenuDetailsPager;
    private String photosPath;

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        super.initData();


        ibMenu.setVisibility(View.VISIBLE);
        ibMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainUI = (MainActivity) mActivity;
                MenuFragment fragment = mainUI.getMenuFragment();
                fragment.toggle();
            }
        });
        path = mActivity.getString(R.string.service_url) + "categories.json";
        String cache = CacheUtils.getCache(mActivity, path);
        if (!TextUtils.isEmpty(cache)) {
            System.out.println(cache);
            processData(cache);
        }
        getDataFromServer();

    }

    public void getDataFromServer() {
        //从服务器获取数据
        new Thread(){
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                try {
                    url = new URL(path);
                    Request request = new Request.Builder().get().tag(this).url(url).build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        final String result =  response.body().string();
                        CacheUtils.setCache(mActivity,result,path);
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                processData(result);
                            }
                        });
                    } else {
                        throw new IOException("Unexpected code " + response);
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }.start();

    }

    private void processData(String result) {
        Gson gson = new Gson();
        data = gson.fromJson(result, CategoriesData.class);


        //找到侧边栏对象
        MainActivity mainUI = (MainActivity) mActivity;
        MenuFragment menuFragment = mainUI.getMenuFragment();
        menuFragment.setMenuData((ArrayList<CategoriesData.DataBean>) data.getData());

        //网络请求成功后，初始化四个菜单详情页
        mPagers = new ArrayList<>();
        mPagers.remove(mPagers);
        mPagers.add(new NewsMenuDetailPager(mActivity,(ArrayList<CategoriesData.DataBean>) data.getData()));
        mPagers.add(new TopicMenuDetalis(mActivity));
        photoMenuDetailsPager = new PhotoMenuDetailsPager(mActivity);
        mPagers.add(photoMenuDetailsPager);
        mPagers.add(new InteractMenuDetailPager(mActivity));


        //设置新闻处理界面为第一个界面
        setMenuDetailPager(0);
    }

    public void setMenuDetailPager(int position) {
        BaseMenuPager pager = mPagers.get(position);

        //清楚之前显示的内荣
        flContainerBasePager.removeAllViews();
        //修改当前的显示内容
        flContainerBasePager.addView(pager.mRootView);
        pager.initData();


        photosPath = mActivity.getResources().getString(R.string.service_url)+data.getData().get(2).getUrl();
        LoggerUtil.e(photosPath);
        //修改标题栏
        String title = data.getData().get(position).getTitle();
        LoggerUtil.e(title);
        tvTitle.setText(title);
        if ("组图".equals(title)) {
            btPhotosStyle.setVisibility(View.VISIBLE);
            photoMenuDetailsPager.initData(1,photosPath);

        }else {
            btPhotosStyle.setVisibility(View.GONE);
        }



    }
}
