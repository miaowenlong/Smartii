package com.itheima.smartbeijing.utils;

/**
 * Created by acer on 2016/11/8.
 */

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

/**
 * 将一些常用的动画效果封装的工具类
 */
public class ActionAnimationUtils {
    /**
     * 以自身中心为旋转中心的旋转动画
     * @param fromDegree 开始角度
     * @param toDegree 结束角度
     * @return
     */
    public static RotateAnimation SelfRotateAnimation(int fromDegree, int toDegree) {
        RotateAnimation rotateAnimation = new RotateAnimation(fromDegree,toDegree, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        return rotateAnimation;
    }


}
