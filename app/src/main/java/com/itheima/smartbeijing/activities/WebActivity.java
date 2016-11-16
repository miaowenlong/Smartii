package com.itheima.smartbeijing.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.itheima.smartbeijing.R;
import com.itheima.smartbeijing.utils.SPUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by acer on 2016/11/14.
 */

public class WebActivity extends AppCompatActivity {
    @InjectView(R.id.news_web_title)
    TextView newsWebTitle;
    @InjectView(R.id.web_news)
    WebView webNews;
    @InjectView(R.id.bt_news_web_back)
    ImageButton btNewsWebBack;
    @InjectView(R.id.bt_web_news_txtsize)
    ImageButton btWebNewsTxtsize;
    @InjectView(R.id.bt_web_news_share)
    ImageButton btWebNewsShare;
    private String url;
    String [] sizes = new String []{"小号字体","常规字体","大号字体"};
    private WebSettings webSettings;
    private int web_txt_size;
    private String TAG = "WebActivity";
    private int tempWhich;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_web);
        ButterKnife.inject(this);




        //设置back的点击事件
        btNewsWebBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WebActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //设置字体大小的点击事件
        btWebNewsTxtsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WebActivity.this);

                builder.setTitle("字体大小").setSingleChoiceItems(sizes,(int) SPUtil.get(WebActivity.this, "web_txt_size", 1), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tempWhich = which;
                    }

                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setTextSize(tempWhich);
                        SPUtil.put(WebActivity.this,"web_txt_size",tempWhich);

                    }
                }).show();
            }
        });

        //分享按钮的点击事件
        btWebNewsShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });




        Intent intent = getIntent();
        url = intent.getStringExtra("url");

        //浏览器设置
        webSettings = webNews.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        web_txt_size = (int) SPUtil.get(WebActivity.this, "web_txt_size", 1);
        setTextSize(web_txt_size);
        //加载网页
        webNews.loadUrl(url);

        webNews.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webNews.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        webNews.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                newsWebTitle.setText(title);
            }
        });



    }

    private void setTextSize(int size) {
        switch(size){
            case 0:
                webSettings.setTextSize(WebSettings.TextSize.SMALLER);
                break;

            case 1:
                webSettings.setTextSize(WebSettings.TextSize.NORMAL);
                break;

            case 2:
                webSettings.setTextSize(WebSettings.TextSize.LARGER);
                break;

        }
    }


    @Override
    public void onBackPressed() {
        if (webNews.canGoBack()) {
            webNews.goBack();
        } else {
            super.onBackPressed();
        }


    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }

}
