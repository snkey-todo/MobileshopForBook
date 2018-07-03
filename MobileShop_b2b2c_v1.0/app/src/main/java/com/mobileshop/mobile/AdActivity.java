package com.mobileshop.mobile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.mobileshop.mobile.utils.HttpUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONObject;

/**
 * Created by Dawei on 7/18/15.
 */
public class AdActivity extends Activity {

    /**
     * 等待几秒自动跳过
     */
    private final int TIME_SECOND = 3;

    private ImageView adImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);

        adImage = (ImageView) findViewById(R.id.ad_image);
        loadAd();

        Button skipBtn = (Button) findViewById(R.id.skip_button);
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip();
            }
        });
    }

    /**
     * 载入广告
     */
    private void loadAd() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what != 1)
                    return;

                DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder().
                        showImageForEmptyUri(R.drawable.image_empty).
                        showImageOnFail(R.drawable.image_error).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
                Constants.imageLoader.displayImage(msg.obj.toString(), adImage, displayImageOptions, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        timer();
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        timer();
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        skip();
                    }
                });
            }
        };

        new Thread() {
            @Override
            public void run() {
                String json = HttpUtils.getJson("/api/mobile/adv!getOneAdv.do?advid=" + Constants.AD_ID);
                if("".equals(json)){
                    handler.sendEmptyMessage(-1);
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if(jsonObject == null){
                        handler.sendEmptyMessage(-1);
                        return;
                    }

                    Message msg = Message.obtain();
                    msg.what = jsonObject.getInt("result");
                    msg.obj = jsonObject.getJSONObject("data").getString("atturl");
                    handler.sendMessage(msg);
                    return;
                }catch(Exception ex){
                    Log.e("Load Ad:", ex.getMessage());
                }
            }
        }.start();
    }

    private void timer() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                skip();
            }
        };

        //发送购物车请求
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < TIME_SECOND; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception ex) {
                    }
                }
                handler.sendEmptyMessage(-1);
            }
        }.start();
    }

    /**
     * 跳过广告
     */
    private void skip() {
        startActivity(new Intent(AdActivity.this, MainActivity.class));
    }

}
