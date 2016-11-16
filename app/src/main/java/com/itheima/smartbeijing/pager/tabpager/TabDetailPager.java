package com.itheima.smartbeijing.pager.tabpager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itheima.smartbeijing.R;
import com.itheima.smartbeijing.activities.WebActivity;
import com.itheima.smartbeijing.domain.CategoriesData;
import com.itheima.smartbeijing.domain.NewsData;
import com.itheima.smartbeijing.utils.CacheUtils;
import com.itheima.smartbeijing.utils.SPUtil;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by acer on 2016/11/11.
 */

public class TabDetailPager {
    public Activity mActivity;
    public View mRootView;
    protected CategoriesData.DataBean.ChildrenBean children;
    @InjectView(R.id.lv_tab_news)
    ListView lvTabNews;
    @InjectView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;


    private String path;
    private NewsData newsData;
    private static List<NewsData.DataBean.TopnewsBean> topnews;
    private ViewPager viewPager;
    private TextView textView;
    private CirclePageIndicator indicator;
    private AdapterView.OnItemClickListener listener;
    private int id2;


    public TabDetailPager(Activity mActivity, CategoriesData.DataBean.ChildrenBean childrenBean) {
        this.mActivity = mActivity;
        this.children = childrenBean;
        this.mRootView = initViews();
    }

    public View initViews() {

        View view = View.inflate(mActivity, R.layout.pager_tab, null);
        ButterKnife.inject(this, view);

        View header = View.inflate(mActivity, R.layout.pager_tab_top_news, null);
        viewPager = (ViewPager) header.findViewById(R.id.vp_tab_top_news);
        textView = (TextView) header.findViewById(R.id.tv_title_top_news);
        indicator = (CirclePageIndicator) header.findViewById(R.id.indicator_top_news);
        lvTabNews.addHeaderView(header);
        //给listview的条目设置点击事件
        listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击后字体颜色变灰
                TextView news = (TextView) view.findViewById(R.id.tv_title_news);
                news.setTextColor(Color.GRAY);

                //记录点击条目
                if (newsData != null) {

                    if (position>0) {
                        int id1 = newsData.getData().getNews().get(position-1).getId();
                        String news_ids = CacheUtils.getCache(mActivity, "news_ids");
                        if (!news_ids.contains(id1+"")) {
                            SPUtil.put(mActivity,"news_ids",news_ids+id1+",");
                        }
                    }

                }
                gotoNewsWeb(newsData.getData().getNews().get(position-1).getUrl());

            }
        };
        lvTabNews.setOnItemClickListener(listener);

        //设置刷新时动画的颜色，可以设置4个
        //, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        Toast.makeText(mActivity, "刷新完成", Toast.LENGTH_SHORT).show();
                    }
                }, 2000);

            }
        });
        return view;
    }

    /**
     * 去新闻详情页
     * @param
     */
    private void gotoNewsWeb(String url) {
        Intent intent = new Intent(mActivity, WebActivity.class);
        intent.putExtra("url", url);
        mActivity.startActivity(intent);
    }

    public void initData() {
        //对应children的url
        path = mActivity.getString(R.string.service_url) + children.getUrl();

        getDataFromServer();

    }

    public void getDataFromServer() {
        new Thread() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                try {
                    URL url = new URL(path);
                    Request request = new Request.Builder().get().tag(this).url(url).build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        final String result = response.body().string();
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
        newsData = gson.fromJson(result, NewsData.class);
        topnews = newsData.getData().getTopnews();
        final List<NewsData.DataBean.NewsBean> news = newsData.getData().getNews();

        initHeader(topnews);

        lvTabNews.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return news.size();
            }

            @Override
            public NewsData.DataBean.NewsBean getItem(int position) {

               return news.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder;
                    id2 = newsData.getData().getNews().get(position).getId();
                String news_ids = CacheUtils.getCache(mActivity, "news_ids");
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    convertView = View.inflate(mActivity, R.layout.item_tab_news, null);
                    viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                    viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title_news);
                    viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                //加载图片
                Picasso.with(mActivity)
                        .load(getItem(position).getListimage())
                        .placeholder(R.drawable.news_pic_default)
                        .error(R.drawable.news_pic_default)
                        .into(viewHolder.ivIcon);
                viewHolder.tvTitle.setText(getItem(position).getTitle());
                if (news_ids.contains(id2+"")) {
                    viewHolder.tvTitle.setTextColor(Color.GRAY);
                }else {
                    viewHolder.tvTitle.setTextColor(Color.BLACK);
                }
                viewHolder.tvTime.setText(getItem(position).getPubdate());
                return convertView;
            }
        });
    }

    private void initHeader(final List<NewsData.DataBean.TopnewsBean> topnews) {
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return topnews.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                String imgPath = topnews.get(position).getTopimage();
                ImageView imageView = new ImageView(mActivity);
                //加载图片
                Picasso.with(mActivity)
                        .load(imgPath)
                        .placeholder(R.drawable.news_pic_default)
                        .error(R.drawable.news_pic_default)
                        .into(imageView);
                container.addView(imageView);
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(((View) object));
            }
        });

        //设置小圆点和标题
        indicator.setViewPager(viewPager, 0);
        textView.setText(topnews.get(0).getTitle());
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                textView.setText(topnews.get(position).getTitle());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    static class ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvTime;

    }
}
