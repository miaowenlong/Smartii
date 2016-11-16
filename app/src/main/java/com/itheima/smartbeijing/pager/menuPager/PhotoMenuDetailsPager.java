package com.itheima.smartbeijing.pager.menuPager;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.itheima.smartbeijing.R;
import com.itheima.smartbeijing.domain.Photos;
import com.itheima.smartbeijing.utils.LoggerUtil;

import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by acer on 2016/11/10.
 */

public class PhotoMenuDetailsPager extends BaseMenuPager {


    @InjectView(R.id.gv_pager_photos)
    GridView gvPagerPhotos;
    @InjectView(R.id.swipe_photos)
    SwipeRefreshLayout swipePhotos;
    private NetworkImageView networkImageView;
    private RequestQueue mQueue;


    public PhotoMenuDetailsPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.pager_photos, null);
        ButterKnife.inject(this, view);

        swipePhotos.setColorSchemeColors(Color.BLUE);
        swipePhotos.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipePhotos.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        return view;
    }

    /**
     * 初始化数据
     */
    public void initData(int numColumns, String url) {

        mQueue = Volley.newRequestQueue(mActivity);
        //根据输入的要求分列
        gvPagerPhotos.setNumColumns(numColumns);
        loadUrl(url);
    }

    /**
     * 解析json字符串，得到数据对象
     */
    private void processData(String result) {
        Gson gson = new Gson();
        final Photos photos = gson.fromJson(result, Photos.class);
        final List<Photos.DataBean.NewsBean> news = photos.getData().getNews();

        //为listview加载数据
        gvPagerPhotos.setAdapter(new BaseAdapter() {


            @Override
            public int getCount() {
                return news.size();
            }

            @Override
            public String getItem(int position) {
                return news.get(position).getListimage();
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = View.inflate(mActivity, R.layout.item_photos, null);
                    networkImageView = (NetworkImageView) convertView.findViewById(R.id.iv_item_pics);
                }
                //加载图片
                //imageView.setTag(getItem(position));
                //if (imageView.getTag()!=null&&imageView.getTag().equals(getItem(position))) {
//                    Picasso.with(mActivity)
//                            .load(news.get(position).getListimage())
//                            .placeholder(R.drawable.news_pic_default)
//                            .error(R.drawable.news_pic_default)
//                            .into(imageView);
                //}


                ImageRequest imageRequest = new ImageRequest(
                        news.get(position).getListimage(), new com.android.volley.Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        networkImageView.setImageBitmap(response);
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        networkImageView.setImageResource(R.drawable.pic_item_list_default);
                    }
                }
                );
                mQueue.add(imageRequest);
               /* networkImageView.setDefaultImageResId(R.drawable.news_pic_default);
                networkImageView.setImageUrl(news.get(position).getListimage(),null);*/


                return convertView;
            }
        });
    }

    /**
     * 解析网址得到json字符串
     */
    String result;

    private void loadUrl(final String url) {
        //从服务器获取数据
        new Thread() {

            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                try {

                    Request request = new Request.Builder().get().tag(this).url(url).build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        result = response.body().string();
                        LoggerUtil.e(result);
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
}
