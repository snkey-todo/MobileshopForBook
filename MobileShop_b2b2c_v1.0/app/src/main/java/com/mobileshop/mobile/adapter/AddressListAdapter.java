package com.mobileshop.mobile.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobileshop.mobile.R;
import com.mobileshop.mobile.model.Address;
import com.mobileshop.mobile.utils.StringUtils;

import java.util.List;

/**
 * Created by Dawei on 4/21/15.
 */
public class AddressListAdapter extends BaseAdapter {

    private List<Address> addressList;
    private Activity context;
    private int type;

    public AddressListAdapter(Activity context, List<Address> addressList, int type) {
        this.context = context;
        this.addressList = addressList;
        this.type = type;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return addressList.get(position);
    }

    @Override
    public int getCount() {
        return addressList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = context.getLayoutInflater().inflate(R.layout.activity_address_item, null);
        } else {
            view = convertView;
        }

        final Address address = (Address)getItem(position);

        TextView nameTV = (TextView)view.findViewById(R.id.textview_address_name);
        nameTV.setText(address.getName());

        TextView mobileTV = (TextView)view.findViewById(R.id.textview_address_phone);
        if(!StringUtils.isEmpty(address.getMobile())) {
            mobileTV.setText(address.getMobile());
        }else{
            mobileTV.setText(address.getTel());
        }

        TextView addressTV = (TextView)view.findViewById(R.id.textview_address_address);
        addressTV.setText(address.getAddr());

        ImageView arrowIV = (ImageView)view.findViewById(R.id.imageview_address_arrow);
        ImageView defaultIV = (ImageView)view.findViewById(R.id.imageview_address_default);
        if(type == 1){
            arrowIV.setVisibility(View.VISIBLE);
            defaultIV.setVisibility(View.GONE);
        }else{
            arrowIV.setVisibility(View.GONE);
            defaultIV.setVisibility(View.VISIBLE);
            if(address.getDef_addr().equals(1)){
                defaultIV.setVisibility(View.VISIBLE);
            }else{
                defaultIV.setVisibility(View.GONE);
            }
        }

        return view;
    }

}
