package com.mobileshop.mobile.adapter;

import java.util.List;

import com.mobileshop.mobile.Constants;
import com.mobileshop.mobile.GoodsActivity;
import com.mobileshop.mobile.R;
import com.mobileshop.mobile.info.ShopGoodsInfo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StoreAllGoodsAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<ShopGoodsInfo> goodsList;
	private Context context;

	public StoreAllGoodsAdapter(Context ctx, List<ShopGoodsInfo> goodsList) {
		this.context = ctx;
		mInflater = LayoutInflater.from(ctx);
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
			convertView = mInflater.inflate(R.layout.store_goods_item, null);
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

		// TextView goodsComments = (TextView) view
		// .findViewById(R.id.goodslist_comments);
		// TextView goodsType = (TextView) view
		// .findViewById(R.id.goodslist_goodstype);

		final ShopGoodsInfo shopGoodsInfo = goodsList.get(position);
		Constants.imageLoader.displayImage(shopGoodsInfo.getThumbnail(),
				holder.goodsImg, Constants.displayImageOptions);
		holder.goodsName.setText(shopGoodsInfo.getName());
		holder.goodsPrice.setText("￥"
				+ String.format("%.2f", shopGoodsInfo.getPrice()));
		//
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
		private ImageView goodsImg;
		private TextView goodsName;
		private TextView goodsPrice;
	}
}
