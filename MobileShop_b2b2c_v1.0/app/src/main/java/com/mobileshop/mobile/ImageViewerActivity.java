package com.mobileshop.mobile;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Dawei on 9/2/15.
 */
public class ImageViewerActivity extends Activity {
    private static String TAG = ImageViewerActivity.class.getName();

    public Context mContext;

    /**
     * 多张图片查看器
     */
    private ViewPager mViewPager;

    /**
     * 当前查看的是第几张图片
     */
    private TextView mTextViewCurrentViewPosition;
    /**
     * 图片的路径列表
     */
    private static ArrayList<String> imagePathList = null;
    private static int currentViewPosition = 0;//当前浏览到哪一张图片



    private static DisplayImageOptions options = null;
    private static ProgressBar spinner = null;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        mContext = this;
        loadData();
        findViews();
    }

    private void loadData() {
        Bundle bundle = getIntent().getExtras();
        imagePathList = bundle.getStringArrayList("imageList");
        if(imagePathList == null){//图片路径都没有,就算了
            Toast.makeText(this, "image path list is null!", Toast.LENGTH_SHORT).show();
            finish();
        }
        currentViewPosition = bundle.getInt("imageIndex");
    }

    public void findViews(){

        mViewPager = (ViewPager) findViewById(R.id.image_view_vp);//使用开源的图片浏览实现方式
        mTextViewCurrentViewPosition = (TextView) findViewById(R.id.image_which);
        mViewPager.setAdapter(new SamplePagerAdapter());


        spinner = (ProgressBar) findViewById(R.id.loading);

        /**
         * 这里只有多张图片才出现第几张的提示，和图片滑动切换
         */
        if(imagePathList.size() > 1){

            /**
             * 设置这个,那么viewPager会将页面缓存起来,这里要注意,当设置过大时,可能会出现内存溢出
             */
            mViewPager.setOffscreenPageLimit(4);

            /**
             * 自己加一个页面变化的监听器，可以进页面的变化作相应的处理
             */
            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {//当前选择的是哪个图片
                    /**
                     * 更新当前图片浏览的位置
                     */
                    currentViewPosition = position;

                    mTextViewCurrentViewPosition.setText((currentViewPosition+1)+"/"+imagePathList.size());
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                    // TODO Auto-generated method stub
//                  Log.d(TAG, "2");
                }

                @Override
                public void onPageScrollStateChanged(int arg0) {
                }

            });


            mViewPager.setCurrentItem(currentViewPosition);//设置当前显示的pager
            mTextViewCurrentViewPosition.setText((currentViewPosition+1)+"/"+imagePathList.size());

        }

    };



    private class SamplePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            if(imagePathList == null){
                return 0;
            }

            return imagePathList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {

            PhotoView photoView = new PhotoView(container.getContext());


            /**
             * 这里的图片分两类:
             * 一个是直接的图片地址
             * 一个是base64格式的图片如：
             */
            String url = imagePathList.get(position).trim();
            Constants.imageLoader.displayImage(url, photoView, Constants.displayImageOptions);

            /*//监听图片区域动作
            photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View arg0, float arg1, float arg2) {
                    ((Activity)mContext).finish();
                }
            });*/


            //监听整个显示区域动作
            photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {

                @Override
                public void onViewTap(View arg0, float arg1, float arg2) {
                    ((Activity)mContext).finish();
                }
            });

            /**
             * 这个是让小图保持原形，不充满全屏的关键
             */
//            photoView.setScaleType(ImageView.ScaleType.CENTER);
            container.addView(photoView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
