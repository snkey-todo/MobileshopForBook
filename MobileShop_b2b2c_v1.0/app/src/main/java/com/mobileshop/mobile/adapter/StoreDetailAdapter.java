package com.mobileshop.mobile.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobileshop.mobile.Constants;
import com.mobileshop.mobile.GoodsActivity;
import com.mobileshop.mobile.R;
import com.mobileshop.mobile.info.ShopGoodsInfo;

public class StoreDetailAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<ShopGoodsInfo> goodsList;
	private Context context;

	public StoreDetailAdapter(Context context, List<ShopGoodsInfo> goodsList) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
		this.goodsList = goodsList;
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
			convertView = mInflater.inflate(R.layout.store_ditail_item, null);
			holder.goodsType = (TextView) convertView
					.findViewById(R.id.goodslist_type);
			holder.goodsImg = (ImageView) convertView
					.findViewById(R.id.goodslist_img);
			holder.goodsName = (TextView) convertView
					.findViewById(R.id.goodslist_name);
			holder.goodsPrice = (TextView) convertView
					.findViewById(R.id.goodslist_price);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final ShopGoodsInfo shopGoodsInfo = goodsList.get(position);
		Constants.imageLoader.displayImage(shopGoodsInfo.getThumbnail(),
				holder.goodsImg, Constants.displayImageOptions);
		holder.goodsName.setText(shopGoodsInfo.getName());
		holder.goodsPrice.setText("￥"
				+ String.format("%.2f", shopGoodsInfo.getPrice()));
		holder.goodsType.setText(shopGoodsInfo.getNameType());
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				intent.putExtra("goods_id", shopGoodsInfo.getGoods_id());
				intent.setClass(context, GoodsActivity.class);
				context.startActivity(intent);
				
			}
		});
		return convertView;
	}

	static class ViewHolder {
		private TextView goodsType;
		private ImageView goodsImg;
		private TextView goodsName;
		private TextView goodsPrice;
	}
}
