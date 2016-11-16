package com.itheima.smartbeijing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.itheima.smartbeijing.R;
import com.itheima.smartbeijing.utils.ActionAnimationUtils;
import com.itheima.smartbeijing.utils.SPUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SplashActivity extends AppCompatActivity {

    @InjectView(R.id.activity_splash)
    RelativeLayout activitySplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);

        //旋转动画
        RotateAnimation rotateAnimation = ActionAnimationUtils.SelfRotateAnimation(0, 360);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);
        //缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0,1, 0,1,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);
        //渐变色动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if ((boolean) SPUtil.get(SplashActivity.this, "first_run", true)) {
                   gotoGuide();
                } else {
                    gotoMainActivity();
                }
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        activitySplash.startAnimation(animationSet);


    }

    /**
     * 切换到引导界面
     */
    private void gotoGuide() {
        Intent intent = new Intent(this, GuideActivity.class);
        startActivity(intent);
    }

    /**
     * 切换到主界面
     */
    private void gotoMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
