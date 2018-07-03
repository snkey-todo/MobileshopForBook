package com.mobileshop.mobile.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobileshop.mobile.Constants;
import com.mobileshop.mobile.R;
import com.mobileshop.mobile.fragment.CategoryFragment;
import com.mobileshop.mobile.model.Category;
import com.mobileshop.mobile.utils.HttpUtils;

import java.util.List;

/**
 * Created by Dawei on 3/27/15.
 */
public class CategoryRightListAdapter extends BaseAdapter {
    private static final String TAG="RightListAdapter";
    private List<Category[]> categoryList;
    private Activity context;
    private CategoryFragment categoryFragment;


    public CategoryRightListAdapter(Activity context, List<Category[]> categoryList, CategoryFragment categoryFragment) {
        this.context = context;
        this.categoryList = categoryList;
        this.categoryFragment = categoryFragment;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //取得每个item的数据
        final Category[] categories = (Category[])getItem(position);

        //判断level级别，以确定是显示标题还是产品
        final Category category = categories[0];
        if(category.getLevel() == 2){
            if(convertView == null || !convertView.getTag().toString().equals("level2")) {
                convertView = context.getLayoutInflater().inflate(R.layout.category_right_item_title, null);
                convertView.setTag("level2");
            }
        }else{
            if(convertView == null || !convertView.getTag().toString().equals("level3")) {
                convertView = context.getLayoutInflater().inflate(R.layout.category_right_item, null);
                convertView.setTag("level3");
            }
        }

        if("level2".equals(convertView.getTag())){
            TextView tv1 = (TextView) convertView.findViewById(R.id.right_item_title);
            tv1.setText(category.getName());
            tv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    categoryFragment.go2GoodsList(category);
                }
            });

        }else {
            //每行显示3个item
            LinearLayout linearLayout1 = (LinearLayout)convertView.findViewById(R.id.category_item_have_picture_ll_1);
            LinearLayout linearLayout2 = (LinearLayout)convertView.findViewById(R.id.category_item_have_picture_ll_2);
            LinearLayout linearLayout3 = (LinearLayout)convertView.findViewById(R.id.category_item_have_picture_ll_3);

            TextView tv1 = (TextView) linearLayout1.findViewById(R.id.category_item_have_picture_text_1);
            TextView tv2 = (TextView) linearLayout2.findViewById(R.id.category_item_have_picture_text_2);
            TextView tv3 = (TextView) linearLayout3.findViewById(R.id.category_item_have_picture_text_3);

            ImageView iv1 = (ImageView)linearLayout1.findViewById(R.id.category_item_have_picture_image_1);
            ImageView iv2 = (ImageView)linearLayout2.findViewById(R.id.category_item_have_picture_image_2);
            ImageView iv3 = (ImageView)linearLayout3.findViewById(R.id.category_item_have_picture_image_3);

            //如果一行存在，则改行的第一个item一定存在，第二、三个则需要进行判断
            tv1.setText(category.getName());
            Log.i(TAG,"URL-->"+category.getImage());

            category.setImage(HttpUtils.replaceBaseUrl(context,category.getImage()));
           Constants.imageLoader.displayImage(category.getImage(), iv1, Constants.displayImageOptions);
            //ImageLoader.getInstance().displayImage(category.getImage(),iv1, MyApplication.displayImageOptions);

            linearLayout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    categoryFragment.go2GoodsList(category);
                }
            });

            if (categories.length > 1 && categories[1] != null) {
                linearLayout2.setVisibility(View.VISIBLE);
                tv2.setText(categories[1].getName());

                categories[1].setImage(HttpUtils.replaceBaseUrl(context,categories[1].getImage()));
                Constants.imageLoader.displayImage(categories[1].getImage(), iv2, Constants.displayImageOptions);
                //ImageLoader.getInstance().displayImage(categories[1].getImage(),iv2, MyApplication.displayImageOptions);

                linearLayout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        categoryFragment.go2GoodsList(categories[1]);
                    }
                });
            }else{
                linearLayout2.setVisibility(View.INVISIBLE);
            }

            if (categories.length > 2 && categories[2] != null) {
                linearLayout3.setVisibility(View.VISIBLE);
                tv3.setText(categories[2].getName());
                categories[2].setImage(HttpUtils.replaceBaseUrl(context,categories[2].getImage()));
                Constants.imageLoader.displayImage(categories[2].getImage(), iv3, Constants.displayImageOptions);
                //ImageLoader.getInstance().displayImage(categories[2].getImage(),iv3, MyApplication.displayImageOptions);
                linearLayout3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        categoryFragment.go2GoodsList(categories[2]);
                    }
                });
            }else{
                linearLayout3.setVisibility(View.INVISIBLE);
            }
        }
        return convertView;
    }


}
