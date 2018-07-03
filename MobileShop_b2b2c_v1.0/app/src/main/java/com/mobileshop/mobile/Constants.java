package com.mobileshop.mobile;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import okhttp3.OkHttpClient;

/**
 * Created by Dawei on 4/1/15.
 */
public class Constants {

    //项目路径
    //public static String baseUrl = "http://192.168.1.108:8080";
    public static String baseUrl = "http://192.168.1.108:8080/mobile_shop";
    //public static String baseUrl = "http://192.168.1.200/mobileshop";

    //ImageLoader配置
    public static DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder().
            showImageOnLoading(R.drawable.image_loading).
            showImageForEmptyUri(R.drawable.image_empty2).
            showImageOnFail(R.drawable.image_error2)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();
    //ImageLoader
    public static ImageLoader imageLoader = ImageLoader.getInstance();

    /**
     * 是否启用广告
     */
    public static boolean AD_ENABLE = false;

    /**
     * 广告位ID
     */
    public static int AD_ID = 1;

    /**
     * 微信支付AppId
     */
    public static String WECHAT_APP_ID = "您的微信应用的AppId";

    /**
     * 是否启用手机验证
     */
    public static boolean MOBILE_VALIDATION = false;

    /**
     * 获得一个OkHttp单例
     */
    private static OkHttpClient client;

    public static OkHttpClient getHttpInstance() {
        if (client == null) {
            synchronized (Constants.class) {
                if (client == null) {
                    client = new OkHttpClient();
                }
            }
        }
        return client;
    }
}
