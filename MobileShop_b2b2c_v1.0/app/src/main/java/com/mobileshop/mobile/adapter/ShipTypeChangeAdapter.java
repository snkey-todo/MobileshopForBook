package com.mobileshop.mobile.adapter;

import java.util.List;

import com.mobileshop.mobile.R;
import com.mobileshop.mobile.info.ShiplistInfo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ShipTypeChangeAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<ShiplistInfo> goodsList;
	private Context context;
	private Handler handler;

	public ShipTypeChangeAdapter(Context context, List<ShiplistInfo> goodsList,
			Handler handler) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
		this.goodsList = goodsList;
		this.handler = handler;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public Object getItem(int position) {
		return goodsList.get(position);
	}

	@Override
	public int getCount() {
		return goodsList.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.ship_item, null);
			holder.shipName = (TextView) convertView
					.findViewById(R.id.ship_name);
			holder.shipPrice = (TextView) convertView
					.findViewById(R.id.ship_price);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final ShiplistInfo shiplistInfo = goodsList.get(position);
		
		holder.shipName.setText(shiplistInfo.getName());
		holder.shipPrice.setText("￥"
				+ String.format("%.2f", shiplistInfo.getShipPrice()));
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				Bundle data = new Bundle();
				data.putInt("type_id", shiplistInfo.getType_id());
				data.putString("shipname", shiplistInfo.getName());
				data.putDouble("shipprice", shiplistInfo.getShipPrice());
				msg.setData(data);
				msg.what = 005;
				handler.sendMessage(msg);
			}
		});

		return convertView;
	}

	static class ViewHolder {
		private TextView shipName;
		private TextView shipPrice;
	}
}
