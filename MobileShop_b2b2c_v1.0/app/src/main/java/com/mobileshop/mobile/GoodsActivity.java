package com.mobileshop.mobile;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileshop.mobile.adapter.GoodsViewPagerAdapter;
import com.mobileshop.mobile.fragment.GoodsSpecFragment;
import com.mobileshop.mobile.model.Product;
import com.mobileshop.mobile.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link BaseActivity} subclass.
 */
public class GoodsActivity extends BaseActivity {

	private int goods_id = 0;
	private Product product;
	private int count = 1;

	private DrawerLayout goodsDrawerlayout;

	private FragmentManager fragmentManager;

	// 商品详细信息
	private LinearLayout goodsDetailLayout;
	private TextView goodsName;
	private TextView goodsPrice;
	private TextView goodsCommentPercent;
	private TextView goodsCommentCount;

	// 商品相册
	private ViewPager imagePager;
	private TextView imagePagerIndex;
	private List<String> galleryList = new ArrayList<>();

	// 商品规格
	private LinearLayout specLayout;
	private TextView goodsSpec;

	// 库存
	private TextView goodsStore;
	private TextView goodsStoreText;

	// 重量
	private TextView goodsWeight;

	// 评论
	private LinearLayout goodsCommentLL;

	// 后退
	private ImageView backIV;

	// 收藏
	private LinearLayout favoriteLayout;

	// 进入购物车
	private RelativeLayout gotoCartRL;

	// 加入购物车
	private TextView addToCartTV;

	// 查看商家全部商品，查看商家信息
	private RelativeLayout show_shop_goods;
	private RelativeLayout show_shop_detaiLayout;

	private GoodsSpecFragment goodsSpecFragment;

	private ProgressDialog progressDialog;

	private TextView seckillTV;
	// 秒杀活动id
	private int act_id;

	// 团购活动id
	private int groupbuy_id;
	// 团购价
	private TextView groupbuyTV;

	// 店铺id
	private int store_id;

	public GoodsActivity() {
		// Required empty public constructor
	}
	/*********************view的点击事件**********************************/
	/**
	 *
	 * 点击标题栏中的返回图标
	 * @param view
     */
	public void clickBackIcon(View view){
		this.finish();
	}

	/**
	 * 查看商家信息
	 * @param view
     */
	public void showShopDetail(View view){
		Intent intent = new Intent();
		intent.putExtra("store_id", store_id);
		intent.putExtra("goods_id", goods_id);
		intent.setClass(GoodsActivity.this, StoreDetailActivity.class);
		startActivity(intent);
	}

	/**
	 * 显示商家所有产品
	 * @param view
     */
	public void showShopAllGoods(View view){
		Intent intent = new Intent();
		intent.putExtra("store_id", store_id);
		intent.setClass(GoodsActivity.this, StoreAllGoodsActivity.class);
		startActivity(intent);
	}

	/**
	 * 显示商品详情页
	 * @param view
     */
	public void showProductDetail(View view){
		Intent intent = new Intent(GoodsActivity.this,
				GoodsDetailActivity.class);
		intent.putExtra("goods_id", goods_id);
		startActivity(intent);
	}

	/**
	 * 显示商品评论
	 * @param view
     */
	public void showGoodsComment(View view){
		Intent intent = new Intent(GoodsActivity.this,
				GoodsCommentActivity.class);
		intent.putExtra("goods_id", goods_id);
		startActivity(intent);
	}
	/******************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods);

		//获得产品的id信息
		Intent intent = getIntent();
		this.goods_id = intent.getIntExtra("goods_id", 0);
		this.act_id = intent.getIntExtra("act_id", 0);
		this.groupbuy_id = intent.getIntExtra("groupbuy_id", 0);

		fragmentManager = getFragmentManager();
		progressDialog = ProgressDialog.show(GoodsActivity.this, null, "载入中…");


		goodsSpec = (TextView) findViewById(R.id.goods_spec);
		// 库存
		goodsStore = (TextView) findViewById(R.id.goods_store);
		goodsStoreText = (TextView) findViewById(R.id.goods_store_text);
		// 重量
		goodsWeight = (TextView) findViewById(R.id.goods_weight);
		goodsName = (TextView) findViewById(R.id.goods_name);
		goodsPrice = (TextView) findViewById(R.id.goods_price);
		goodsCommentPercent = (TextView) findViewById(R.id.goods_comment_percent);
		goodsCommentCount = (TextView) findViewById(R.id.goods_comment_count);
		new DetailTask().execute();

		// 载入相册
		imagePager = (ViewPager) findViewById(R.id.image_pager);
		imagePagerIndex = (TextView) findViewById(R.id.image_pager_index);
		imagePager
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
					@Override
					public void onPageScrolled(int position,
							float positionOffset, int positionOffsetPixels) {
					}

					@Override
					public void onPageSelected(int position) {
						GoodsActivity.this.imagePagerIndex
								.setText((position + 1) + "/"
										+ galleryList.size());
					}

					@Override
					public void onPageScrollStateChanged(int state) {
					}
				});

		new GalleryTask().execute();

		// 商品规格
		goodsDrawerlayout = (DrawerLayout) findViewById(R.id.goods_drawerlayout);
		goodsDrawerlayout
				.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		specLayout = (LinearLayout) findViewById(R.id.spec_layout);
		specLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goodsDrawerlayout.openDrawer(Gravity.RIGHT);
			}
		});

		// 收藏
		favoriteLayout = (LinearLayout) findViewById(R.id.favorite_layout);
		favoriteLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isLogin()) {
					startActivity(new Intent(GoodsActivity.this,
							LoginActivity.class));
					Toast.makeText(GoodsActivity.this, "未登录或登录已过期，请重新登录！",
							Toast.LENGTH_SHORT).show();
					return;
				}
				final String text = favoriteLayout.isSelected() ? "取消收藏" : "收藏";
				progressDialog = ProgressDialog.show(GoodsActivity.this, null,
						"正在" + text + "…");
				final Handler handler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						progressDialog.dismiss();

						if (msg.obj == null) {
							Toast.makeText(GoodsActivity.this,
									text + "失败，请您重试！", Toast.LENGTH_SHORT)
									.show();
							return;
						}

						try {

							JSONObject jsonObject = new JSONObject(msg.obj
									.toString());
							if (jsonObject == null) {
								Toast.makeText(GoodsActivity.this,
										text + "失败，请您重试！", Toast.LENGTH_SHORT)
										.show();
								return;
							}

							int result = jsonObject.getInt("result");
							if (result == -1) {
								startActivity(new Intent(GoodsActivity.this,
										LoginActivity.class));
								Toast.makeText(GoodsActivity.this,
										"未登录或登录已过期，请重新登录！", Toast.LENGTH_SHORT)
										.show();
								return;
							}

							if (result == 0) {
								Toast.makeText(GoodsActivity.this,
										text + "失败，请您重试！", Toast.LENGTH_SHORT)
										.show();
								return;
							}

							setFavoriteState(!favoriteLayout.isSelected());
							Toast.makeText(GoodsActivity.this, text + "成功",
									Toast.LENGTH_SHORT).show();

						} catch (JSONException ex) {
							Toast.makeText(GoodsActivity.this,
									text + "失败，请您重试！", Toast.LENGTH_SHORT)
									.show();
							return;
						}

					}
				};

				if (favoriteLayout.isSelected()) {
					new Thread() {
						@Override
						public void run() {
							String json = HttpUtils
									.getJson("/api/mobile/favorite!delete.do?id="
											+ product.getGoods_id());
							Message msg = Message.obtain();
							msg.obj = json;
							handler.sendMessage(msg);
						}
					}.start();
				} else {
					new Thread() {
						@Override
						public void run() {
							String json = HttpUtils
									.getJson("/api/mobile/favorite!add.do?id="
											+ product.getGoods_id());
							Message msg = Message.obtain();
							msg.obj = json;
							handler.sendMessage(msg);
						}
					}.start();
				}
			}
		});

		// 进入购物车
		gotoCartRL = (RelativeLayout) findViewById(R.id.goto_cart);
		gotoCartRL.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(GoodsActivity.this, CartActivity.class));
			}
		});

		// 加入购物车
		addToCartTV = (TextView) findViewById(R.id.add_to_cart);
		addToCartTV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (act_id > 0) {
					addSeckillToCart();
				} else {
					addToCart();
				}
			}
		});

		// 秒杀
		seckillTV = (TextView) findViewById(R.id.seckill);
		if (act_id > 0) {
			seckillTV.setVisibility(View.VISIBLE);
			specLayout.setVisibility(View.GONE);
		}

		// 团购
		groupbuyTV = (TextView) findViewById(R.id.groupbuy);
		if (groupbuy_id > 0) {
			groupbuyTV.setVisibility(View.VISIBLE);
			specLayout.setVisibility(View.GONE);
		}
	}


	public void showImageViewer(int position) {
		Intent intent = new Intent(this, ImageViewerActivity.class);
		Bundle bundle = new Bundle();
		bundle.putStringArrayList("imageList", (ArrayList) galleryList);
		bundle.putInt("imageIndex", position);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	/**
	 * 设置收藏状态
	 * 
	 * @param favorited
	 */
	public void setFavoriteState(boolean favorited) {
		if (favorited) {
			favoriteLayout.setSelected(true);
			((TextView) favoriteLayout.getChildAt(1)).setText("已收藏");
		} else {
			favoriteLayout.setSelected(false);
			((TextView) favoriteLayout.getChildAt(1)).setText("收藏");
		}
	}

	/**
	 * 加入到购物车
	 */
	public void addToCart() {
		progressDialog = ProgressDialog.show(GoodsActivity.this, null,
				"正在加入购物车…");
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				progressDialog.dismiss();
				switch (msg.what) {
				case -1:
					Toast.makeText(GoodsActivity.this, "添加到购物车失败！",
							Toast.LENGTH_SHORT).show();
					break;
				case 0:
					Toast.makeText(GoodsActivity.this, msg.obj.toString(),
							Toast.LENGTH_SHORT).show();
					break;
				case 1:
					setCartCount(Integer.parseInt(msg.obj.toString()));
					new AlertDialog.Builder(GoodsActivity.this)
							.setTitle("添加成功！")
							.setMessage("商品已成功加入购物车")
							.setPositiveButton("去购物车",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											startActivity(new Intent(
													GoodsActivity.this,
													CartActivity.class));
										}
									})
							.setNegativeButton("再逛逛",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									}).create().show();
					break;

				}
				super.handleMessage(msg);
			}
		};

		// 发送购物车请求
		new Thread() {
			@Override
			public void run() {
				String json = "";

				json = HttpUtils
						.getJson("/api/shop/cart!addProduct.do?productid="
								+ product.getProduct_id() + "&num=" + count);

				if ("".equals(json)) {
					handler.sendEmptyMessage(-1);
					return;
				}
				try {
					JSONObject jsonObject = new JSONObject(json);
					if (jsonObject == null) {
						handler.sendEmptyMessage(-1);
						return;
					}

					Message msg = Message.obtain();
					msg.what = jsonObject.getInt("result");
					if (msg.what == 0) {
						msg.obj = jsonObject.getString("message");
					} else {
						msg.obj = 1;
					}
					handler.sendMessage(msg);
					return;
				} catch (Exception ex) {
					Log.e("AddToCart", ex.getMessage());
				}
			}
		}.start();
	}

	/**
	 * 加入秒杀商品到购物车
	 */
	public void addSeckillToCart() {
		progressDialog = ProgressDialog.show(GoodsActivity.this, null, "正在秒杀…");
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				progressDialog.dismiss();
				switch (msg.what) {
				case -1:
					Toast.makeText(GoodsActivity.this, "秒杀失败！",
							Toast.LENGTH_SHORT).show();
					break;
				case 0:
					Toast.makeText(GoodsActivity.this, msg.obj.toString(),
							Toast.LENGTH_SHORT).show();
					break;
				case 1:
					setCartCount(Integer.parseInt(msg.obj.toString()));
					new AlertDialog.Builder(GoodsActivity.this)
							.setTitle("秒杀成功！")
							.setMessage("商品已成功加入购物车")
							.setPositiveButton("去购物车",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											startActivity(new Intent(
													GoodsActivity.this,
													CartActivity.class));
										}
									})
							.setNegativeButton("再逛逛",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									}).create().show();
					break;

				}
				super.handleMessage(msg);
			}
		};

		// 发送购物车请求
		new Thread() {
			@Override
			public void run() {
				String json = HttpUtils
						.getJson("/api/seckill/queue!addGoods.do?goodsid="
								+ product.getGoods_id() + "&num=" + count);
				if ("".equals(json)) {
					handler.sendEmptyMessage(-1);
					return;
				}
				try {
					JSONObject jsonObject = new JSONObject(json);
					if (jsonObject == null) {
						handler.sendEmptyMessage(-1);
						return;
					}

					Message msg = Message.obtain();
					msg.what = jsonObject.getInt("result");
					if (msg.what == 0) {
						msg.obj = jsonObject.getString("message");
					} else {
						// 获取购物车商品数量
						JSONObject countJson = new JSONObject(
								HttpUtils.getJson("/api/mobile/cart!count.do"));
						msg.obj = countJson.getInt("count");
					}
					handler.sendMessage(msg);
					return;
				} catch (Exception ex) {
					Log.e("AddToCart", ex.getMessage());
				}
			}
		}.start();
	}

	public void initProduct(Product product, int count) {
		this.product = product;
		this.count = count;
		store_id = product.getStore_id();
		goodsName.setText(product.getName());
		goodsPrice.setText("￥" + String.format("%.2f", product.getPrice()));
		if ("".equals(product.getSpecs()) || product.getSpecs().equals("null")) {
			goodsSpec.setText(count + "件");
		} else {
			goodsSpec.setText(product.getSpecs() + " (" + count + "件)");
		}
		goodsStore.setText("" + product.getStore());
		if (product.getStore() == 0) {
			goodsStoreText.setVisibility(View.VISIBLE);
		} else {
			goodsStoreText.setVisibility(View.GONE);
		}
		goodsWeight.setText(product.getWeight() + " g");

		// 设置规则选择抽屉
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		if (goodsSpecFragment == null) {
			goodsSpecFragment = new GoodsSpecFragment();
			goodsSpecFragment.setProduct(product);
			goodsSpecFragment.setGoodsActivity(GoodsActivity.this);
		}
		fragmentTransaction.replace(R.id.right_frame, goodsSpecFragment);
		fragmentTransaction.commit();

		progressDialog.dismiss();
	}

	/**
	 * 载入商品详细信息
	 */
	private class DetailTask extends AsyncTask<Integer, Integer, JSONObject> {
		@Override
		protected JSONObject doInBackground(Integer... params) {
			String json = HttpUtils.getJson("/api/mobile/goods!detail.do?id="
					+ goods_id + "&act_id=" + act_id + "&groupbuy_id="
					+ groupbuy_id);
			try {
				JSONObject object = new JSONObject(json).getJSONObject("data");
				product = Product.toProduct(object);
				return object;
			} catch (Exception ex) {
				Log.e("LoadGoodsDetail", ex.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONObject data) {
			if (data != null) {
				try {
					goodsCommentPercent.setText(data
							.getString("comment_percent"));
					goodsCommentCount.setText(data.getString("comment_count")
							+ "人评价");
					initProduct(product, 1);

					// 是否已收藏
					setFavoriteState(data.getBoolean("favorited"));

				} catch (JSONException ex) {
					ex.printStackTrace();
				}
			}
			super.onPostExecute(data);
		}
	}

	/**
	 * 载入商品相册
	 */
	private class GalleryTask extends AsyncTask<Integer, Integer, JSONArray> {
		@Override
		protected JSONArray doInBackground(Integer... params) {
			String json = HttpUtils.getJson("/api/mobile/goods!gallery.do?id="
					+ goods_id);
			try {
				return new JSONObject(json).getJSONArray("data");
			} catch (Exception ex) {
				Log.e("Load Gallery", ex.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONArray array) {
			if (array != null && array.length() > 0) {
				try {
					for (int i = 0; i < array.length(); i++) {
						JSONObject data = array.getJSONObject(i);
						galleryList.add(data.getString("big"));
					}
				} catch (JSONException ex) {
					ex.printStackTrace();
				}
				imagePager.setAdapter(new GoodsViewPagerAdapter(galleryList,
						GoodsActivity.this));
				imagePagerIndex.setText("1/" + galleryList.size());
			}
			super.onPostExecute(array);
		}
	}
}
