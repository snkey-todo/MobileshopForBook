package com.mobileshop.mobile.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobileshop.mobile.R;
import com.mobileshop.mobile.model.Category;

import java.util.List;

/**
 * Created by Dawei on 3/24/15.
 */
public class CategoryLeftListAdapter extends BaseAdapter {

    private List<Category> categoryList;
    private Activity context;

    //选中的索引
    private int selectedCategoryId = 0;

    public CategoryLeftListAdapter(Activity context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
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

    /**
     * 设置选中行
     * @param categoryId
     */
    public void setSelection(int categoryId){
        selectedCategoryId = categoryId;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Category category = (Category)getItem(position);
        if (convertView == null) {
            convertView = context.getLayoutInflater().inflate(R.layout.category_left_item, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.text);
        tv.setText(category.getName());

        if(category.getCat_id().intValue() == selectedCategoryId){
            convertView.setBackgroundResource(R.drawable.category_left_bg_focus);
            tv.setTextColor(convertView.getResources().getColor(R.color.category_left_red_font));
        }else{
            convertView.setBackgroundResource(R.drawable.category_left_bg_normal);
            tv.setTextColor(convertView.getResources().getColor(R.color.category_left_dark_font));
        }
        return convertView;
    }
}
