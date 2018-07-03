package com.mobileshop.mobile.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobileshop.mobile.Constants;
import com.mobileshop.mobile.R;
import com.mobileshop.mobile.info.SettlementGoodsListInfo;
import com.mobileshop.mobile.info.SettlementStoreInfo;

public class SettlementAdapter extends BaseExpandableListAdapter {

	private Context ctx;
	private LayoutInflater mInflater;
	public String edit_flog = "";
	private Handler handler;
	private ArrayList<SettlementStoreInfo> groupData = new ArrayList<SettlementStoreInfo>();
	private ArrayList<ArrayList<SettlementGoodsListInfo>> childrenData = new ArrayList<ArrayList<SettlementGoodsListInfo>>();

	public SettlementAdapter(Context _ctx,
			ArrayList<SettlementStoreInfo> groupList,
			ArrayList<ArrayList<SettlementGoodsListInfo>> childList,
			Handler handler) {
		this.groupData = groupList;
		this.childrenData = childList;
		this.mInflater = LayoutInflater.from(_ctx);
		this.handler = handler;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childrenData.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildrenViewHolder holder;
		if (convertView == null) {
			holder = new ChildrenViewHolder();
			convertView = mInflater.inflate(R.layout.settlement_item, null);
			holder.good_img = (ImageView) convertView
					.findViewById(R.id.shop_photo);
			holder.good_name = (TextView) convertView
					.findViewById(R.id.shop_name);
			holder.num = (TextView) convertView.findViewById(R.id.shop_number);
			holder.price = (TextView) convertView.findViewById(R.id.shop_price);
			holder.add_btn = (ImageButton) convertView.findViewById(R.id.add);
			holder.sub_btn = (ImageButton) convertView.findViewById(R.id.sub);

			holder.numchange_relat = (RelativeLayout) convertView
					.findViewById(R.id.numchange_relat);
			holder.imgTV = (ImageView) convertView
					.findViewById(R.id.shop_photo);
			convertView.setTag(holder);
		} else {
			holder = (ChildrenViewHolder) convertView.getTag();
		}
		final SettlementGoodsListInfo settlementGoodsListInfo = childrenData
				.get(groupPosition).get(childPosition);
		holder.good_name.setText(settlementGoodsListInfo.getName());
		holder.num.setText(settlementGoodsListInfo.getNum() + "");
		holder.price.setText("￥"
				+ String.format("%.2f", settlementGoodsListInfo.getPrice()));
		// 显示商品图片
		if (!settlementGoodsListInfo.getImage_default().equals("")) {
			// 显示商品图片
			Constants.imageLoader.displayImage(
					settlementGoodsListInfo.getImage_default(), holder.imgTV,
					Constants.displayImageOptions);
		} else {
			holder.imgTV.setImageResource(R.drawable.default_pic);
		}
		// // 数量的增加
		// holder.add_btn.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View arg0) {
		// Message msg = new Message();
		// Bundle data = new Bundle();
		// data.putInt("id", shopCarGoodsInfo.getId());
		// data.putInt("produceid", shopCarGoodsInfo.getProduct_id() + 1);
		// data.putInt("num", shopCarGoodsInfo.getNum());
		// msg.setData(data);
		// msg.what = 002;
		// // 发出消息
		// handler.sendMessage(msg);
		// notifyDataSetChanged();
		// }
		// });
		//
		// // 数量的减少
		// holder.sub_btn.setOnClickListener(new OnClickListener() {
		// //
		// @Override
		// public void onClick(View arg0) {
		// if (shopCarGoodsInfo.getNum() != 1) {
		// Message msg = new Message();
		// Bundle data = new Bundle();
		// // 设置选中索引
		// data.putInt("id", shopCarGoodsInfo.getGoods_id());
		// data.putInt("produceid", shopCarGoodsInfo.getProduct_id());
		// data.putInt("num", shopCarGoodsInfo.getNum() - 1);
		// msg.setData(data);
		// msg.what = 003;
		// // 发出消息
		// handler.sendMessage(msg);
		// notifyDataSetChanged();
		// }
		// }
		//
		// });
		// convertView.setOnLongClickListener(new OnLongClickListener() {
		//
		// @Override
		// public boolean onLongClick(View arg0) {
		// Message msg = new Message();
		// Bundle data = new Bundle();
		// // 设置选中索引
		// data.putInt("id", shopCarGoodsInfo.getGoods_id());
		// msg.setData(data);
		// Log.i("ssssssssssssssssssssssss",
		// shopCarGoodsInfo.getGoods_id() + "");
		// msg.what = 001;
		// // 发出消息
		// handler.sendMessage(msg);
		// return false;
		// }
		// });
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		try {
			return childrenData.get(groupPosition).size();
		} catch (Exception e) {
			return 0;
		}

	}

	@Override
	public Object getGroup(int groupPosition) {
		return groupData.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groupData.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupViewHolder holder = null;
		if (convertView == null) {
			holder = new GroupViewHolder();
			convertView = mInflater.inflate(R.layout.settlement_heard_item,
					null);
			holder.stationNameTV = (TextView) convertView
					.findViewById(R.id.station_name);
			holder.shipTV = (TextView) convertView.findViewById(R.id.ship);
			holder.ship_edit_layout = (RelativeLayout) convertView
					.findViewById(R.id.ship_edit_layout);
			convertView.setTag(holder);
		} else {
			holder = (GroupViewHolder) convertView.getTag();
		}
		final SettlementStoreInfo settlementStoreInfo = groupData
				.get(groupPosition);
		holder.stationNameTV.setText(settlementStoreInfo.getStore_name());
		try {
	//		Log.i("aaaaaaaa", settlementStoreInfo.getShiplist());
			if (settlementStoreInfo.getShipname().equals("")) {
				holder.shipTV.setText(settlementStoreInfo.getShiplist().get(0).getName());
		} else {
			holder.shipTV.setText(settlementStoreInfo.getShipname());
		}
		}catch(Exception ex) {
			Log.e("LoadCart", ex.getMessage());
		}
		holder.ship_edit_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				Bundle data = new Bundle();
				data.putInt("store_id", settlementStoreInfo.getStore_id());
				ArrayList list = new ArrayList(); // 这个list用于在budnle中传递
													// 需要传递的ArrayList<Object>
				list.add(settlementStoreInfo.getShiplist());
				data.putParcelableArrayList("shiplist", list);
				data.putInt("position", groupPosition);
				msg.setData(data);
				msg.what = 004;
				// 发出消息
				handler.sendMessage(msg);
			}
		});
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	// ViewHolder静态类
	static class GroupViewHolder {
		private TextView stationNameTV, shipTV;
		private ImageView stationCK;
		private RelativeLayout ship_edit_layout;
	}

	// ViewHolder静态类
	static class ChildrenViewHolder {
		public ImageView good_ckeck;
		public ImageView good_img;
		public TextView good_name;
		public ImageView imgTV;
		public TextView price;
		public TextView num;
		public ImageButton sub_btn;
		public ImageButton add_btn;
		public RelativeLayout numchange_relat;
	}

}
