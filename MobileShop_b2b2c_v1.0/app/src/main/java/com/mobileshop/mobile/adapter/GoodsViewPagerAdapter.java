package com.mobileshop.mobile.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mobileshop.mobile.Constants;
import com.mobileshop.mobile.GoodsActivity;

import java.util.List;

/**
 * Created by Dawei on 4/12/15.
 */
public class GoodsViewPagerAdapter extends PagerAdapter {

    private List<String> galleryList;
    private Context context;

    public GoodsViewPagerAdapter(List<String> galleryList, Context context){
        this.galleryList = galleryList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return galleryList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView iv = new ImageView(context);
        Constants.imageLoader.displayImage(galleryList.get(position), iv, Constants.displayImageOptions);
        ((ViewPager)container).addView(iv, 0);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GoodsActivity) context).showImageViewer(position);
            }
        });
        return iv;
    }

    @Override
    public void destroyItem (ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
