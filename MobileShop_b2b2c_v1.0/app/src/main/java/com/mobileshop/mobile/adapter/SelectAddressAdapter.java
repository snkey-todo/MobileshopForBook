package com.mobileshop.mobile.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.mobileshop.mobile.R;
import com.mobileshop.mobile.SelectAddressActivity;
import com.mobileshop.mobile.model.Region;

import java.util.List;

/**
 * Created by Dawei on 5/12/15.
 */
public class SelectAddressAdapter extends BaseAdapter{

    private List<Region> regionList;
    private SelectAddressActivity selectAddressActivity;

    public SelectAddressAdapter(SelectAddressActivity selectAddressActivity, List<Region> regionList) {
        this.selectAddressActivity = selectAddressActivity;
        this.regionList = regionList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return regionList.get(position);
    }

    @Override
    public int getCount() {
        return regionList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = selectAddressActivity.getLayoutInflater().inflate(R.layout.select_address_item, null);
        } else {
            view = convertView;
        }

        Region region = (Region)getItem(position);
        CheckedTextView checkedTextView = (CheckedTextView)view.findViewById(R.id.select_address_item_name);
        checkedTextView.setText(region.getLocal_name());

        return view;
    }

}
