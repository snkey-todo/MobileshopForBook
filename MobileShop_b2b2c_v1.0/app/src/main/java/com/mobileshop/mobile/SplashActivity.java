package com.mobileshop.mobile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;

import com.mobileshop.mobile.utils.HttpUtils;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.json.JSONObject;

import java.io.File;


/**
 * Created by Dawei on 5/27/15.
 */
public class SplashActivity extends Activity {
    private static  final String TAG="SplashActivity";
    private ImageView mSplashItem_iv = null;
    private Handler mHandler;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mSplashItem_iv = (ImageView) findViewById(R.id.splash_loading_item);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        Constants.SCREEN_DENSITY = metrics.density;
//        Constants.SCREEN_HEIGHT = metrics.heightPixels;
//        Constants.SCREEN_WIDTH = metrics.widthPixels;

        CookieSyncManager.createInstance(this);

        //初始化ImageLoader
        File diskCacheFile = StorageUtils.getOwnCacheDirectory(getApplicationContext(), getString(R.string.mobileshop));
        ImageLoaderConfiguration localImageLoaderConfiguration = new ImageLoaderConfiguration.Builder(getApplicationContext()).memoryCacheExtraOptions(480, 800).diskCacheExtraOptions(480, 800, null).threadPoolSize(3).threadPriority(4).tasksProcessingOrder(QueueProcessingType.FIFO).denyCacheImageMultipleSizesInMemory().memoryCache(new LruMemoryCache(2097152)).memoryCacheSize(2097152).memoryCacheSizePercentage(13).diskCache(new UnlimitedDiscCache(diskCacheFile)).diskCacheSize(52428800).diskCacheFileCount(100).diskCacheFileNameGenerator(new HashCodeFileNameGenerator()).imageDownloader(new BaseImageDownloader(getApplicationContext())).imageDecoder(new BaseImageDecoder(false)).defaultDisplayImageOptions(DisplayImageOptions.createSimple()).writeDebugLogs().build();
        ImageLoader.getInstance().init(localImageLoaderConfiguration);

        mHandler = new Handler(getMainLooper());
        initView();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * 检测登录状态
     */
    private void checkLogin() {
        final Handler handler = new Handler() {
            @Override
            // 当有消息发送出来的时候就执行Handler的这个方法
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    SharedPreferences.Editor localEditor = getSharedPreferences("user", 0).edit();
                    localEditor.remove("username");
                    localEditor.remove("face");
                    localEditor.remove("level");
                    localEditor.commit();
                }
                SplashActivity.this.finish();
                super.handleMessage(msg);
            }
        };

        //从api加载分类数据
        new Thread() {
            @Override
            public void run() {
                String json = HttpUtils.getJson("/api/mobile/member!isLogin.do");
                Message msg = Message.obtain();
                if ("".equals(json)) {
                    handler.sendEmptyMessage(0);
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject == null) {
                        handler.sendEmptyMessage(0);
                        return;
                    }
                    handler.sendEmptyMessage(jsonObject.getInt("result"));
                    Log.i(TAG,"LOGIN_MSG"+json);
                } catch (Exception ex) {
                    Log.e("Check Login", ex.getMessage());
                }
            }
        }.start();
//        String url = Constants.baseUrl +"/api/mobile/member!isLogin.do";
//        //1.获得Client对象，这里使用单例的形式
//        OkHttpClient client =  Constants.getHttpInstance();
//        //2.创建一个请求队列
//        Request request  =new Request.Builder()
//                .url(url)
//                .build();
//        //3.执行请求
//        Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//            }
//        });

    }

    protected void initView() {
        Animation translate = AnimationUtils.loadAnimation(this,
                R.anim.splash_loading);
        translate.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!Constants.AD_ENABLE) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, AdActivity.class));
                }

                overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);
                //请求服务器,判断是否已经登录
                checkLogin();
            }
        });
        mSplashItem_iv.setAnimation(translate);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Splash Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.mobileshop.mobile/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Splash Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.mobileshop.mobile/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
