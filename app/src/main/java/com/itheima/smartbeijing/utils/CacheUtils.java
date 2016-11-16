package com.itheima.smartbeijing.utils;

import android.content.Context;

/**
 * Created by acer on 2016/11/11.
 */

public class CacheUtils {
    /**
     *通过sp设置缓存
     * @param context 上下文
     * @param content
     * @param key
     */
    public static void setCache(Context context,String content, String key) {
        SPUtil.put(context, content, key);
    }

    /**
     * 获取缓存数据
     * @param context 上下文
     * @param url 网址
     * @return
     */
    public static String getCache (Context context, String url){
        return  SPUtil.get(context, url, "").toString();
    }

}
