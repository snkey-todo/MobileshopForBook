package com.mobileshop.mobile;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileshop.mobile.adapter.StoreDetailAdapter;
import com.mobileshop.mobile.info.ShopGoodsInfo;
import com.mobileshop.mobile.info.Store_detail_info;
import com.mobileshop.mobile.utils.HttpUtils;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class StoreDetailActivity extends Activity {

	private Context ctx;
	Store_detail_info storetail_info = new Store_detail_info();
	private PullToRefreshListView pullToRefreshListView;
	private TextView nodataTextView;
	private ImageView back, img;
	private TextView store_name;

	private int store_id;
	private int goods_id;
	private String name;
	private String keyword;
	private int seckill;

	// 全部商品数量 店铺收藏
	private TextView count, count2;
	// 是否收藏
	private Boolean isCollect;

	// 商品相册
	private ImageView imagePager;
	// private TextView imagePagerIndex;
	private List<String> galleryList = new ArrayList<>();

	// 列表以及适配器
	private List<ShopGoodsInfo> goodsList = new ArrayList<ShopGoodsInfo>();
	private StoreDetailAdapter goodsListAdapter = null;

	private String[] types = { "new", "recommend", "hot" };

	private String Type;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		setContentView(R.layout.srore_detail_activity);

		findView();
		init();
		listener();
		new LoadStoreDetailTask().execute();
		new LoadGoodsListTask1().execute();
	}

	private void findView() {
		back = (ImageView) findViewById(R.id.title_back);
		count = (TextView) findViewById(R.id.count);
		count2 = (TextView) findViewById(R.id.count2);
		// 商家信用 五角星
		img = (ImageView) findViewById(R.id.img);
		nodataTextView = (TextView) findViewById(R.id.goodslist_nodata);
		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.storegoodslist);
		store_name = (TextView) findViewById(R.id.store_name);
		imagePager = (ImageView) findViewById(R.id.image_pager);
	}

	private void init() {
		ctx = this;
		Intent intent = getIntent();
		this.store_id = intent.getIntExtra("store_id", 0);
		this.goods_id = intent.getIntExtra("goods_id", 0);
		goodsListAdapter = new StoreDetailAdapter(ctx, goodsList);
		new LoadStoreDetailTask().execute();
	}

	private void listener() {
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				StoreDetailActivity.this.finish();
			}
		});
	}

	@SuppressLint("NewApi")
	private class LoadGoodsListTask1 extends
			AsyncTask<Integer, Integer, String> {

		@Override
		protected String doInBackground(Integer... params) {

			String json = HttpUtils
			 .getJson("/api/mobile/store!storeGoodsList.do?storeid="
			 + store_id + "&mark=" + types[0] + "&num" + 5);

			return json;
		}

		@Override
		protected void onPostExecute(String json) {
			try {
				JSONArray array = new JSONObject(json).getJSONArray("data");
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					ShopGoodsInfo shopGoodsInfo = new ShopGoodsInfo();
					shopGoodsInfo.setName(obj.getString("name"));
					shopGoodsInfo.setThumbnail(obj.getString("thumbnail"));
					shopGoodsInfo.setPrice(obj.getDouble("price"));
					shopGoodsInfo.setNameType("新品上市");
					shopGoodsInfo.setGoods_id(obj.getInt("goods_id"));
					goodsList.add(shopGoodsInfo);
				}

				new LoadGoodsListTask2().execute();
			} catch (Exception ex) {
				Log.e("loadCategories", ex.getMessage());
			}
			super.onPostExecute(json);
		}
	}

	@SuppressLint("NewApi")
	private class LoadStoreDetailTask extends
			AsyncTask<Integer, Integer, String> {

		@Override
		protected String doInBackground(Integer... params) {
			String json = HttpUtils
					.getJson("/api/mobile/store!storeIntro.do?storeid="
							+ store_id);
			return json;
		}

		@Override
		protected void onPostExecute(String json) {
			try {
				JSONObject object = new JSONObject(json).getJSONObject("data");
				isCollect = object.getBoolean("isCollect");
				JSONObject object2 = object.getJSONObject("store");
				storetail_info.setGoods_num(object2.getInt("goods_num"));
				storetail_info.setPraise_rate(object2.getInt("praise_rate"));
				storetail_info.setStore_credit(object2.getInt("store_credit"));
				storetail_info.setStore_logo(object2.getString("store_logo"));
				count.setText(object2.getInt("goods_num") + "");
				store_name.setText(object2.getString("store_name"));
				count2.setText(object2.getInt("store_collect") + "");


				Constants.imageLoader.displayImage(
						storetail_info.getStore_logo(), imagePager,
						Constants.displayImageOptions);

			} catch (Exception ex) {
				Log.e("loadCategories", ex.getMessage());
			}
			super.onPostExecute(json);
		}
	}

	@SuppressLint("NewApi")
	private class LoadGoodsListTask2 extends
			AsyncTask<Integer, Integer, String> {

		@Override
		protected String doInBackground(Integer... params) {
			String json = HttpUtils
					.getJson("/api/mobile/store!storeGoodsList.do?storeid="
							+ store_id + "&mark=" + types[1] + "&num" + 5);
			return json;
		}

		@Override
		protected void onPostExecute(String json) {
			try {
				JSONArray array = new JSONObject(json).getJSONArray("data");
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					ShopGoodsInfo shopGoodsInfo = new ShopGoodsInfo();
					shopGoodsInfo.setName(obj.getString("name"));
					shopGoodsInfo.setThumbnail(obj.getString("thumbnail"));
					shopGoodsInfo.setPrice(obj.getDouble("price"));
					shopGoodsInfo.setGoods_id(obj.getInt("goods_id"));
					shopGoodsInfo.setNameType("商家推荐");
					goodsList.add(shopGoodsInfo);
				}

				// if (goodsList.size() > 0) {
				// pullToRefreshListView.setAdapter(goodsListAdapter);
				// goodsListAdapter.notifyDataSetChanged();
				// pullToRefreshListView.onRefreshComplete();
				// new LoadGoodsListTask3().execute();
				// } else {
				// nodataTextView.setVisibility(View.VISIBLE);
				// }
				new LoadGoodsListTask3().execute();
			} catch (Exception ex) {
				Log.e("loadCategories", ex.getMessage());
			}
			super.onPostExecute(json);
		}
	}

	@SuppressLint("NewApi")
	public class LoadGoodsListTask3 extends AsyncTask<Integer, Integer, String> {

		@Override
		protected String doInBackground(Integer... params) {
			String json = HttpUtils
					.getJson("/api/mobile/store!storeGoodsList.do?storeid="
							+ store_id + "&mark=" + types[2] + "&num" + 5);
			return json;
		}

		@Override
		protected void onPostExecute(String json) {
			try {
				JSONArray array = new JSONObject(json).getJSONArray("data");
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					ShopGoodsInfo shopGoodsInfo = new ShopGoodsInfo();
					shopGoodsInfo.setName(obj.getString("name"));
					shopGoodsInfo.setThumbnail(obj.getString("thumbnail"));
					shopGoodsInfo.setPrice(obj.getDouble("price"));
					shopGoodsInfo.setNameType("正在热卖");
					shopGoodsInfo.setGoods_id(obj.getInt("goods_id"));
					goodsList.add(shopGoodsInfo);
				}

				if (goodsList.size() > 0) {
					pullToRefreshListView.setAdapter(goodsListAdapter);
					goodsListAdapter.notifyDataSetChanged();
					pullToRefreshListView.onRefreshComplete();
				} else {
					nodataTextView.setVisibility(View.VISIBLE);
				}
			} catch (Exception ex) {
				Log.e("loadCategories", ex.getMessage());
			}
			super.onPostExecute(json);
		}
	}

}
