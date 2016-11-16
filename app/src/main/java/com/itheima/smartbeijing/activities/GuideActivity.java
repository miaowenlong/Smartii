package com.itheima.smartbeijing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.itheima.smartbeijing.R;
import com.itheima.smartbeijing.utils.DensityUtils;
import com.itheima.smartbeijing.utils.SPUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by acer on 2016/11/8.
 */

public class GuideActivity extends AppCompatActivity {
    @InjectView(R.id.vp_guide)
    ViewPager vpGuide;
    @InjectView(R.id.bt_guide_start)
    Button btGuideStart;
    @InjectView(R.id.ll_guide_ballscontainer)
    LinearLayout llGuideBallscontainer;
    @InjectView(R.id.iv_red_point)
    ImageView ivRedPoint;
    @InjectView(R.id.rl_container)
    RelativeLayout rlContainer;
    private MyGuideAdapter mGuideAdapter;

    private int mPointDis;

    /**
     * 数组用于存储图片的的id
     */
    private int[] imagesIds = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
    private ImageView[] imageViews = new ImageView[3];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guide);
        ButterKnife.inject(this);
        mGuideAdapter = new MyGuideAdapter();
        vpGuide.setAdapter(mGuideAdapter);
        initViews();
        initDatas();
        //设置viewpager的监听事件，根据滑动的距离来移动红色小球
        vpGuide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //通过修改小红点的左边距
                int leftMargin = (int) (positionOffset*mPointDis+position*mPointDis+0.5f);

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
                params.leftMargin = leftMargin;
                ivRedPoint.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                if (position==2) {
                    btGuideStart.setVisibility(View.VISIBLE);
                }else {
                    btGuideStart.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //点击监听事件
        btGuideStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMainActivity();
                finish();
                SPUtil.put(GuideActivity.this,"first_run",false);
            }
        });
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void initDatas() {
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //一旦视图数的方法调用完成，就会调用此方法
                mPointDis = llGuideBallscontainer.getChildAt(1).getLeft()-llGuideBallscontainer.getChildAt(0).getLeft();

                //移除观察者
                ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });
    }

    private void initViews() {
        for (int i = 0; i < imagesIds.length; i++) {
            imageViews[i] = new ImageView(this);
            imageViews[i].setBackgroundResource(imagesIds[i]);

            //初始化小球
            ImageView grayBall = new ImageView(this);
            grayBall.setImageResource(R.drawable.gray_ball);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i > 0) {
                params.leftMargin = DensityUtils.dp2px(this, 10);
            }

            grayBall.setLayoutParams(params);
            llGuideBallscontainer.addView(grayBall);
        }
    }

    private class MyGuideAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return imagesIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = imageViews[position];
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }



}
