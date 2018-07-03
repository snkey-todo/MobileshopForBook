package com.mobileshop.mobile;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileshop.mobile.adapter.StoreAllGoodsAdapter;
import com.mobileshop.mobile.info.ShopGoodsInfo;
import com.mobileshop.mobile.utils.HttpUtils;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StoreAllGoodsActivity extends Activity {

	private Context ctx;
	private RelativeLayout back;

	public PullToRefreshGridView listview;
	// private ListView goodsListView;
	private TextView nodataTextView;

	// 按默认排序
	private RelativeLayout orderbyRandomLayout;
	private TextView orderbyRandomText;
	private ImageView orderbyRandomImg;

	// 按评论排序
	private RelativeLayout orderbycommandLayout;
	private TextView orderbycommandText;
	private ImageView orderbyCommantImg;
	// 按价格排序
	private RelativeLayout orderbyPriceLayout;
	private TextView orderbyPriceText;
	private ImageView orderbyPriceImg;

	// 按销量排序
	private RelativeLayout orderbycountGoodsLayout;
	private TextView orderbycountGoodsText;
	private ImageView orderbycountImg;

	// 列表以及适配器
	private List<ShopGoodsInfo> goodsList = new ArrayList<ShopGoodsInfo>();
	private StoreAllGoodsAdapter goodsListAdapter = null;

	private int store_id;
	private int brand;
	private String name;
	private String keyword;
	private int seckill;

	// 当前页码
	private int currentPage = 1;

	// 排序（按位置）排序字段 “”（空的）＝默认 buy_count＝销量 price＝价格 view_count＝评价）
	private String sort = "";
	// asc/desc 升序／降序
	private String order = "";

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		setContentView(R.layout.menulist_activity_goods);

		findView();
		init();
		listener();
	}

	private void findView() {

		nodataTextView = (TextView) findViewById(R.id.goodslist_nodata);
		back = (RelativeLayout) findViewById(R.id.back);

		orderbyRandomLayout = (RelativeLayout) findViewById(R.id.goodslist_orderby_random_relat);
		orderbyRandomText = (TextView) findViewById(R.id.goodslist_orderby_random_text);
		orderbyRandomImg = (ImageView) findViewById(R.id.goodslist_orderby_random_img);

		orderbycommandLayout = (RelativeLayout) findViewById(R.id.goodslist_orderby_commant_relat);
		orderbycommandText = (TextView) findViewById(R.id.goodslist_orderby_commant_text);
		orderbyCommantImg = (ImageView) findViewById(R.id.goodslist_orderby_commant_img);

		orderbyPriceLayout = (RelativeLayout) findViewById(R.id.goodslist_orderby_price);
		orderbyPriceText = (TextView) findViewById(R.id.goodslist_orderby_price_text);
		orderbyPriceImg = (ImageView) findViewById(R.id.goodslist_orderby_price_img);

		orderbycountGoodsLayout = (RelativeLayout) findViewById(R.id.goodslist_orderby_count_relat);
		orderbycountGoodsText = (TextView) findViewById(R.id.goodslist_orderby_count_text);
		orderbycountImg = (ImageView) findViewById(R.id.goodslist_orderby_count_img);

		listview = (PullToRefreshGridView) findViewById(R.id.gridview);
	}

	private void init() {

		ctx = this;
		Intent intent = getIntent();
		this.store_id = intent.getIntExtra("store_id", 0);
		goodsListAdapter = new StoreAllGoodsAdapter(ctx, goodsList);
		orderbyRandomText.setTextColor(getResources().getColorStateList(
				R.color.red));
		new LoadGoodsListTask().execute();
	}

	private void listener() {
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				StoreAllGoodsActivity.this.finish();
			}
		});

		orderbyRandomLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sort = "goods_id";
				if (order.equals("asc")) {
					order = "desc";
				} else {
					order = "asc";
				}
				setOrderbySelection();
			}
		});
		// 排序（按位置）排序字段 “”（空的）＝默认 buy_count＝销量 price＝价格 view_count＝评价）
		orderbycommandLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sort = "view_count";
				if (order.equals("asc")) {
					order = "desc";
				} else {
					order = "asc";
				}
				setOrderbySelection();
			}
		});
		orderbyPriceLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sort = "price";
				if (order.equals("asc")) {
					order = "desc";
				} else {
					order = "asc";
				}
				setOrderbySelection();
			}
		});
		orderbycountGoodsLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sort = "buy_count";
				if (order.equals("asc")) {
					order = "desc";
				} else {
					order = "asc";
				}
				setOrderbySelection();
			}
		});
	}

	/**
	 * 设置orderby条件的选中项
	 */
	private void setOrderbySelection() {
		inits();
		goodsList.clear();
		new LoadGoodsListTask().execute();
		
		if (sort.equals("")) {
			orderbyRandomText.setTextColor(getResources().getColorStateList(
					R.color.red));
			if (order.equals("asc")) {
				orderbyRandomImg
						.setImageResource(R.drawable.sort_button_price_up);
			} else {
				orderbyRandomImg
						.setImageResource(R.drawable.sort_button_price_down);
			}
		} else if (sort.contains("view_count")) {
			orderbycommandText.setTextColor(getResources()
					.getColorStateList(R.color.red));
			if (order.equals("asc")) {
				orderbyCommantImg
						.setImageResource(R.drawable.sort_button_price_up);
			} else {
				orderbyCommantImg
						.setImageResource(R.drawable.sort_button_price_down);
			}
		} else if (sort.equals("price")) {
			orderbyPriceText.setTextColor(getResources().getColorStateList(
					R.color.red));
			if (order.equals("asc")) {
				orderbyPriceImg
						.setImageResource(R.drawable.sort_button_price_up);
			} else {
				orderbyPriceImg
						.setImageResource(R.drawable.sort_button_price_down);
			}
		} else if (sort.equals("buy_count")) {
			orderbycountGoodsText.setTextColor(getResources()
					.getColorStateList(R.color.red));
			if (order.equals("asc")) {
				orderbycountImg
						.setImageResource(R.drawable.sort_button_price_up);
			} else {
				orderbycountImg
						.setImageResource(R.drawable.sort_button_price_down);
			}
		}

	}

	private void inits() {
		// 默认
		orderbyRandomText.setTextColor(getResources().getColorStateList(
				R.color.light));
		// 评论
		orderbycommandText.setTextColor(getResources().getColorStateList(
				R.color.light));
		// 价格
		orderbyPriceText.setTextColor(getResources().getColorStateList(
				R.color.light));
		// 销量
		orderbycountGoodsText.setTextColor(getResources().getColorStateList(
				R.color.light));
		orderbyCommantImg.setImageResource(R.drawable.sort_button_price_none);
		orderbyPriceImg.setImageResource(R.drawable.sort_button_price_none);
		orderbycountImg.setImageResource(R.drawable.sort_button_price_none);
		orderbyRandomImg.setImageResource(R.drawable.sort_button_price_none);
	}

	@SuppressLint("NewApi")
	private class LoadGoodsListTask extends AsyncTask<Integer, Integer, String> {

		/*
		 * /api/mobile/goods!search.do 所需参数：store_id ＃店铺id sort： （排序字段 “”（空的）＝默认
		 * buy_count＝销量 price＝价格 view_count＝评价） order： asc/desc 升序／降序
		 */
		@Override
		protected String doInBackground(Integer... params) {

			String json = HttpUtils
					.getJson("/api/mobile/goods!search.do?store_id=" + store_id
							+ "&sort=" + sort + "&order=" + order);
			return json;
		}

		@Override
		protected void onPostExecute(String json) {
			goodsList.clear();
			listview.setAdapter(null);
			try {
				JSONArray array = new JSONObject(json).getJSONArray("data");
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					ShopGoodsInfo shopGoodsInfo = new ShopGoodsInfo();
					shopGoodsInfo.setName(obj.getString("name"));
					shopGoodsInfo.setThumbnail(obj.getString("thumbnail"));
					shopGoodsInfo.setPrice(obj.getDouble("price"));
					shopGoodsInfo.setGoods_id(obj.getInt("goods_id"));
					goodsList.add(shopGoodsInfo);
				}

				if (goodsList.size() > 0) {
					listview.setAdapter(goodsListAdapter);

					goodsListAdapter.notifyDataSetChanged();
					listview.onRefreshComplete();
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
