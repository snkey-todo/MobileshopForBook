package com.mobileshop.mobile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

import com.mobileshop.mobile.adapter.SettlementAdapter;
import com.mobileshop.mobile.adapter.ShipTypeChangeAdapter;
import com.mobileshop.mobile.info.SettlementGoodsListInfo;
import com.mobileshop.mobile.info.SettlementStoreInfo;
import com.mobileshop.mobile.info.ShiplistInfo;
import com.mobileshop.mobile.model.Address;
import com.mobileshop.mobile.model.Order;
import com.mobileshop.mobile.model.Payment;
import com.mobileshop.mobile.model.Receipt;
import com.mobileshop.mobile.model.Shipping;
import com.mobileshop.mobile.payment.AlipayMobilePaymentActivity;
import com.mobileshop.mobile.payment.WechatMobilePaymentActivity;
import com.mobileshop.mobile.utils.HttpUtils;
import com.mobileshop.mobile.utils.StringUtils;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("NewApi")
public class CheckoutActivity extends Activity implements View.OnClickListener,
		Callback {

	private final int ADD_ADDRESS = 1;
	private final int SELECT_ADDRESS = 2;
	private final int SELECT_PAYMENT = 3;
	private final int SELECT_RECEIPT = 4;
	private final int PAYMENT_BACK = 5;

	private ImageView backIV;

	private ProgressDialog progressDialog;

	// 收货地址
	private RelativeLayout addressEmptyLayout;
	private RelativeLayout addressInfoLayout;
	private TextView addressNameTV;
	private TextView addressMobileTV;
	private TextView addressContentTV;
	private TextView addressAddTV;

	// 支付
	private TextView paymentTV;
	private TextView shippingTV;
	private TextView shippingPriceTV;
	// 配送方式列表
	private List<Shipping> shippingList = new ArrayList<>();

	// 支付方式列表
	private List<Payment> paymentList = new ArrayList<>();

	// 发票
	private TextView receiptTypeTV;
	private TextView receiptTitleTV;
	private TextView receiptContentTV;

	private TextView totalTV;

	// 订单备注
	private EditText remarkET;

	// 提交按钮
	private Button submitBtn;

	// 支付及配送
	private RelativeLayout paymentLayout;

	// 发票
	private RelativeLayout receiptLayout;

	// 选择的收货地址
	private Address address;

	// 选择的支付方式
	private Payment payment;

	// 选择的配送方式
	private Shipping shipping;

	// 选择的发票信息
	private Receipt receipt = new Receipt(0, "", "");

	// 订单总价
	private double orderAmount = 0.00;

	// 快递费用
	private double shippingPrice = 0.00;

	// 物品展示
	private Context ctx;
	private ExpandableListView list;
	private SettlementAdapter adapter = null;

	private ArrayList<ArrayList<SettlementGoodsListInfo>> childLists = new ArrayList<ArrayList<SettlementGoodsListInfo>>();
	private ArrayList<SettlementStoreInfo> groupLists = new ArrayList<SettlementStoreInfo>();
	//
	final Handler handler1 = new Handler(this);
	private int id, produce_id, num, store_id, type_id, regionid;

	private Double total = 0.00;
	private ArrayList<ShiplistInfo> shiplistInfos = new ArrayList<ShiplistInfo>();
	private PopupWindow window = null;
	private PullToRefreshListView shiplist;
	private ShipTypeChangeAdapter shipTypeChangeAdapter;
	private RelativeLayout header;
	private int position;

	// 快递方式
	public static String shipname = "";

	public CheckoutActivity() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkout);

		backIV = (ImageView) findViewById(R.id.title_back);
		backIV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		initControls();
		initData();
	}

	/**
	 * 初始化控件
	 */
	private void initControls() {
		totalTV = (TextView) findViewById(R.id.order_money);
		list = (ExpandableListView) findViewById(R.id.list);
		header = (RelativeLayout) View.inflate(this, R.layout.head, null);

		addressEmptyLayout = (RelativeLayout) header
				.findViewById(R.id.layout_address_empty);
		addressInfoLayout = (RelativeLayout) header
				.findViewById(R.id.layout_address_info);
		addressInfoLayout.setOnClickListener(this);
		addressNameTV = (TextView) header
				.findViewById(R.id.textview_address_name_content);
		addressMobileTV = (TextView) header
				.findViewById(R.id.textview_address_mobile_content);
		addressContentTV = (TextView) header
				.findViewById(R.id.textview_address_address_content);
		addressAddTV = (TextView) header
				.findViewById(R.id.address_add_textview);
		addressAddTV.setOnClickListener(this);

		paymentLayout = (RelativeLayout) header
				.findViewById(R.id.layout_payment_info);
		paymentLayout.setOnClickListener(this);
		paymentTV = (TextView) header
				.findViewById(R.id.textview_payment_content);

		receiptLayout = (RelativeLayout) header
				.findViewById(R.id.layout_invoice_info);
		receiptLayout.setOnClickListener(this);
		receiptTypeTV = (TextView) header
				.findViewById(R.id.textview_receipt_type);
		receiptTitleTV = (TextView) header
				.findViewById(R.id.textview_receipt_title);
		receiptContentTV = (TextView) header
				.findViewById(R.id.textview_receipt_content);

		remarkET = (EditText) header.findViewById(R.id.edittext_remark_content);

		submitBtn = (Button) findViewById(R.id.submit_order);
		submitBtn.setOnClickListener(this);

		ctx = this;

		list.setGroupIndicator(null);
		list.addHeaderView(header, null, false);

	}

	/**
	 * 点击事件
	 * 
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.address_add_textview:
			startActivityForResult(
					new Intent(this, AddressModifyActivity.class), ADD_ADDRESS);
			break;
		case R.id.layout_address_info:
			startActivityForResult(new Intent(this, AddressActivity.class),
					SELECT_ADDRESS);
			break;
		case R.id.layout_payment_info:
			Intent paymentIntent = new Intent(this,
					PaymentDeliveryActivity.class);
			paymentIntent.putExtra("payment", payment);
			paymentIntent.putExtra("shipping", shipping);
			paymentIntent.putExtra("paymentList", (Serializable) paymentList);
			paymentIntent.putExtra("shippingList", (Serializable) shippingList);
			startActivityForResult(paymentIntent, SELECT_PAYMENT);
			break;
		case R.id.layout_invoice_info:
			Intent receiptIntent = new Intent(this, ReceiptActivity.class);
			receiptIntent.putExtra("receipt", receipt);
			startActivityForResult(receiptIntent, SELECT_RECEIPT);
			break;
		case R.id.submit_order:
			submitOrder();
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ADD_ADDRESS:
		case SELECT_ADDRESS:
			if (resultCode == Activity.RESULT_OK) {
				address = (Address) data.getSerializableExtra("address");
				initDefaultAddress();
			}
			break;
		case SELECT_PAYMENT:
			if (resultCode == Activity.RESULT_OK) {
				payment = (Payment) data.getSerializableExtra("payment");

				paymentTV.setText(payment.getName());

			}
			break;
		case SELECT_RECEIPT:
			if (resultCode == Activity.RESULT_OK) {
				receipt = (Receipt) data.getSerializableExtra("receipt");
				switch (receipt.getType()) {
				case 1:
					receiptTypeTV.setText("个人");
					break;
				case 2:
					receiptTypeTV.setText("单位");
					break;
				}
				receiptTitleTV.setText(receipt.getTitle());
				receiptContentTV.setText(receipt.getContent());
			}
			break;
		case PAYMENT_BACK:
			finish();
		}
	}

	private int loadTimes = 0;
	private int maxTimes = 3;

	/**
	 * 初始化数据
	 */
	private void initData() {
		// progressDialog = ProgressDialog.show(this, null, "载入中…");

		ctx = this;
		adapter = new SettlementAdapter(ctx, groupLists, childLists, handler1);
		// shipTypeChangeAdapter = new ShipTypeChangeAdapter(ctx, shiplistInfos,
		// handler1);
		list.setAdapter(adapter);
		new LoadCartTask().execute();

		// 载入默认收件地址
		loadDefaultAddress();

		// 载入商品列表
		// new LoadCartTask().execute();

		// 载入快递信息
		loadPaymentDelivery();

	}

	/**
	 * 初始化数据完成
	 */
	private void initCompleted() {
		loadTimes++;
		if (loadTimes >= maxTimes) {
			progressDialog.dismiss();
			// 合计
			totalTV.setText(String
					.format("%.2f", (orderAmount)));
		}
	}

	/**
	 * 载入默认收货地址
	 */
	private void loadDefaultAddress() {
		final Handler handler = new Handler() {
			@Override
			// 当有消息发送出来的时候就执行Handler的这个方法
			public void handleMessage(Message msg) {
				if (msg.obj != null) {
					address = (Address) msg.obj;
					regionid = address.getRegion_id();
					initDefaultAddress();
				} else {
					addressEmptyLayout.setVisibility(View.VISIBLE);
					addressInfoLayout.setVisibility(View.GONE);
				}
				initCompleted();
				super.handleMessage(msg);
			}
		};

		// 从api加载分类数据
		new Thread() {
			@Override
			public void run() {
				String json = HttpUtils
						.getJson("/api/mobile/address!defaultAddress.do");
				Message msg = Message.obtain();
				if ("".equals(json)) {
					handler.sendMessage(msg);
					return;
				}
				try {
					JSONObject jsonObject = new JSONObject(json);
					if (jsonObject == null) {
						handler.sendMessage(msg);
						return;
					}
					if (jsonObject.has("data")) {
						msg.obj = Address.toAddress(jsonObject.getJSONObject(
								"data").getJSONObject("defaultAddress"));
					}
					handler.sendMessage(msg);
				} catch (Exception ex) {
					Log.e("Load Default Address", ex.getMessage());
				}
			}
		}.start();
	}

	/**
	 * 显示默认地址
	 */
	private void initDefaultAddress() {
		addressEmptyLayout.setVisibility(View.GONE);
		addressInfoLayout.setVisibility(View.VISIBLE);
		addressNameTV.setText(address.getName());
		if (!StringUtils.isEmpty(address.getMobile())) {
			addressMobileTV.setText(address.getMobile());
		} else {
			addressMobileTV.setText(address.getTel());
		}
		addressContentTV.setText(address.getProvince() + " "
				+ address.getCity() + " " + address.getRegion() + " "
				+ address.getAddr());
	}

	/**
	 * 载入支付及配送信息
	 */
	private void loadPaymentDelivery() {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					if (paymentList.size() > 0) {
						payment = paymentList.get(0);
						paymentTV.setText(payment.getName());
					}

					break;
				default:
					Toast.makeText(CheckoutActivity.this, "载入支付信息失败，请您重试！",
							Toast.LENGTH_SHORT).show();
					break;
				}
				initCompleted();
				super.handleMessage(msg);
			}
		};

		new Thread() {
			@Override
			public void run() {
				String json = HttpUtils
						.getJson("/api/mobile/order!paymentShipping.do");
				if ("".equals(json)) {
					handler.sendEmptyMessage(-1);
					return;
				}
				try {
					JSONObject jsonObject = new JSONObject(json);
					if (jsonObject == null) {
						handler.sendEmptyMessage(0);
						return;
					}

					if (!jsonObject.has("data")) {
						handler.sendEmptyMessage(0);
						return;
					}
					JSONObject dataObject = jsonObject.getJSONObject("data");



					JSONArray paymentArray = dataObject.getJSONArray("payment");
					if (paymentArray == null) {
						handler.sendEmptyMessage(0);
						return;
					}



					paymentList.clear();
					for (int i = 0; i < paymentArray.length(); i++) {
						paymentList.add(Payment.toPayment(paymentArray
								.getJSONObject(i)));
					}
					handler.sendEmptyMessage(1);
				} catch (Exception ex) {
					Log.e("Load Payment", ex.getMessage());
				}
			}
		}.start();
	}

	/**
	 * 提交订单
	 */
	private void submitOrder() {
		// 验证信息
		if (address == null) {
			Toast.makeText(CheckoutActivity.this, "请选择收货地址！",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (payment == null) {
			Toast.makeText(CheckoutActivity.this, "请选择支付方式！",
					Toast.LENGTH_SHORT).show();
			return;
		}

		progressDialog = ProgressDialog.show(this, null, "正在提交订单…");

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				progressDialog.dismiss();
				switch (msg.what) {
				case 0:
					Toast.makeText(CheckoutActivity.this, msg.obj.toString(),
							Toast.LENGTH_SHORT).show();
					break;
				case 1:
					Intent intent = null;
					if (payment.getType().equals("cod")) {
						intent = new Intent(CheckoutActivity.this,
								OrderSuccessActivity.class);
						intent.putExtra("payment", payment);
					} else if (payment.getType().equals("alipayMobilePlugin")) {
						intent = new Intent(CheckoutActivity.this,
								AlipayMobilePaymentActivity.class);
						intent.putExtra("paymentid", payment.getId());
					} else if (payment.getType().equals("wechatMobilePlugin")) {
						intent = new Intent(CheckoutActivity.this,
								WechatMobilePaymentActivity.class);
						intent.putExtra("paymentid", payment.getId());
					} else {

						intent = new Intent(CheckoutActivity.this,
								AlipayMobilePaymentActivity.class);
						intent.putExtra("paymentid", payment.getId());
					}

					intent.putExtra("order", (Order) msg.obj);
					startActivityForResult(intent, PAYMENT_BACK);
					break;
				default:
					Toast.makeText(CheckoutActivity.this, "提交订单失败，请您重试！",
							Toast.LENGTH_SHORT).show();
					break;
				}
				super.handleMessage(msg);
			}
		};

		new Thread() {
			@Override
			public void run() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("paymentId", payment.getId().toString());
				params.put("addressId", address.getAddr_id().toString());
				params.put("remark", remarkET.getText().toString());

				String json = HttpUtils.post("/api/mobile/order!create.do",
						params);
				if ("".equals(json)) {
					handler.sendEmptyMessage(-1);
					return;
				}
				try {
					JSONObject jsonObject = new JSONObject(json);
					if (jsonObject == null) {
						handler.sendEmptyMessage(0);
						return;
					}

					if (!jsonObject.has("result")) {
						handler.sendEmptyMessage(0);
						return;
					}

					int result = jsonObject.getInt("result");
					Message msg = Message.obtain();
					msg.what = result;
					if (result == 1) {
						msg.obj = Order.toOrder(jsonObject
								.getJSONObject("order"));
					} else {
						msg.obj = jsonObject.getString("message");
					}
					handler.sendMessage(msg);
				} catch (Exception ex) {
					Log.e("Create Order", ex.getMessage());
				}
			}
		}.start();
	}

	/**
	 * 载入购物车商品
	 */
	// private class LoadCartTask extends AsyncTask<Integer, Integer, String> {
	//
	// @Override
	// protected String doInBackground(Integer... params) {
	// String json = HttpUtils.getJson("/api/mobile/cart!listApp.do");
	// return json;
	// }
	//
	// @Override
	// protected void onPostExecute(String json) {
	// try {
	// List<CartItem> cartItemList = new ArrayList<>();
	//
	// JSONObject dataObject = new JSONObject(json)
	// .getJSONObject("data");
	// JSONArray goodslist = dataObject.getJSONArray("goodslist");
	//
	// for (int j = 0; j < goodslist.length(); j++) {
	// JSONArray array = goodslist.getJSONObject(j).getJSONArray(
	// "goodslist");
	//
	// for (int i = 0; i < array.length(); i++) {
	// JSONObject jsonObject = array.getJSONObject(i);
	// cartItemList.add(CartItem.toCartItem(jsonObject));
	// }
	// }
	//
	// for (CartItem cartItem : cartItemList) {
	// RelativeLayout goodsLayout = (RelativeLayout) getLayoutInflater()
	// .inflate(R.layout.activity_checkout_goods_item,
	// null);
	//
	// ImageView imageIV = (ImageView) goodsLayout
	// .findViewById(R.id.product_image);
	// Constants.imageLoader.displayImage(
	// cartItem.getImage_default(), imageIV,
	// Constants.displayImageOptions);
	//
	// TextView nameTV = (TextView) goodsLayout
	// .findViewById(R.id.product_name);
	// nameTV.setText(cartItem.getName());
	//
	// TextView numberTV = (TextView) goodsLayout
	// .findViewById(R.id.product_num);
	// numberTV.setText("x" + cartItem.getNum());
	//
	// TextView priceTV = (TextView) goodsLayout
	// .findViewById(R.id.product_price);
	// priceTV.setText("￥"
	// + String.format("%.2f", cartItem.getCoupPrice()));
	//
	// RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
	// ViewGroup.LayoutParams.WRAP_CONTENT,
	// ViewGroup.LayoutParams.WRAP_CONTENT);
	// goodsContainerLayout.addView(goodsLayout, layoutParams);
	// }
	// orderAmount = dataObject.getDouble("total");
	// initCompleted();
	// } catch (Exception ex) {
	// Log.e("Load Cart Items", ex.getMessage());
	// }
	// super.onPostExecute(json);
	// }
	// }

	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha;
		getWindow().setAttributes(lp);
	}

	@SuppressWarnings("deprecation")
	private void showCusPopUp(View parent) {
		if (window == null) {
			View popView = LayoutInflater.from(ctx).inflate(
					R.layout.popwindow_shiplist, null);
			shiplist = (PullToRefreshListView) popView
					.findViewById(R.id.shiplist);
			Button cancel = (Button) popView.findViewById(R.id.cancel);
			window = new PopupWindow(popView, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			popView.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if (window != null && window.isShowing()) {
						window.dismiss();
						backgroundAlpha(1f);
					}
					return false;
				}
			});
			window.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					// TODO Auto-generated method stub
					backgroundAlpha(1f);
				}
			});
			cancel.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					window.dismiss();
					backgroundAlpha(1f);
				}
			});
		}
		window.setAnimationStyle(R.style.PopWindowAnim);
		window.setFocusable(true);
		window.setBackgroundDrawable(new BitmapDrawable());
		window.update();
		window.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		shipTypeChangeAdapter = new ShipTypeChangeAdapter(ctx, shiplistInfos,
				handler1);
		shiplist.setAdapter(shipTypeChangeAdapter);
		shipTypeChangeAdapter.notifyDataSetChanged();

	}

	@SuppressLint("NewApi")
	private class ChangeShipTypeDetailTask extends
			AsyncTask<Integer, Integer, String> {

		@Override
		protected String doInBackground(Integer... params) {
			String json = HttpUtils
					.getJson("/api/store/storeOrder!changeArgsType.do?store_id="
							+ store_id
							+ "&type_id="
							+ type_id
							+ "&regionid=" + regionid);
			return json;
		}

		@Override
		protected void onPostExecute(String json) {
			try {
				// JSONObject object = new
				// JSONObject(json).getJSONObject("data");
				// isCollect = object.getBoolean("isCollect");
				// JSONObject object2 = object.getJSONObject("store");
				// storetail_info.setGoods_num(object2.getInt("goods_num"));
				// storetail_info.setPraise_rate(object2.getInt("praise_rate"));
				// storetail_info.setStore_credit(object2.getInt("store_credit"));
				// count.setText(object2.getInt("goods_num") + "");

			} catch (Exception ex) {
				Log.e("loadCategories", ex.getMessage());
			}
			super.onPostExecute(json);
		}
	}

	/**
	 * 载入购物车商品
	 */
	@SuppressLint("NewApi")
	private class LoadCartTask extends AsyncTask<Integer, Integer, String> {

		@Override
		protected String doInBackground(Integer... params) {
			String json = HttpUtils
					.getJson("/api/mobile/order!storeCartGoods.do");
			return json;
		}

		@Override
		protected void onPostExecute(String json) {
			try {
				groupLists.clear();
				childLists.clear();
				JSONObject root = new JSONObject(json);
				JSONObject data = root.getJSONObject("data");
				JSONArray store_list = data.getJSONArray("store_list");
				// total = data.getDouble("");
				for (int i = 0; i < store_list.length(); i++) {
					JSONObject obj = store_list.getJSONObject(i);

					SettlementStoreInfo settlementStoreInfo = new SettlementStoreInfo();

					settlementStoreInfo.setShiptypeid(obj.getInt("shiptypeid"));
					settlementStoreInfo.setStore_name(obj
							.getString("store_name"));
					settlementStoreInfo.setStore_id(obj.getInt("store_id"));

					JSONArray shipArray = obj.getJSONArray("shiplist");
					ArrayList<ShiplistInfo> shiplistInfolist = new ArrayList<ShiplistInfo>();
					for (int j = 0; j < shipArray.length(); j++) {
						JSONObject shipObject = shipArray.getJSONObject(j);
						ShiplistInfo shiplistInfo = new ShiplistInfo();
						shiplistInfo.setName(shipObject.getString("name"));
						shiplistInfo.setShipPrice(shipObject
								.getDouble("shipPrice"));
						shiplistInfo.setType_id(shipObject.getInt("type_id"));
						shiplistInfolist.add(shiplistInfo);
					}
					settlementStoreInfo.setShiplist(shiplistInfolist);

					JSONObject obj2 = obj.getJSONObject("storeprice");
					settlementStoreInfo.setNeedPayMoney(obj2
							.getDouble("needPayMoney"));

					total += obj2.getDouble("needPayMoney");
					settlementStoreInfo.setOrderPrice(obj2
							.getDouble("orderPrice"));

					settlementStoreInfo.setGoodsPrice(obj2
							.getDouble("goodsPrice"));
					settlementStoreInfo.setShippingPrice(obj2
							.getDouble("shippingPrice"));
					settlementStoreInfo.setShipname("");

					groupLists.add(settlementStoreInfo);

					JSONArray goodslist = obj.getJSONArray("goodslist");
					ArrayList<SettlementGoodsListInfo> settlementGoodsListInfos = new ArrayList<SettlementGoodsListInfo>();
					for (int j = 0; j < goodslist.length(); j++) {
						JSONObject obj3 = goodslist.getJSONObject(j);
						SettlementGoodsListInfo settlementGoodsListInfo = new SettlementGoodsListInfo();
						settlementGoodsListInfo.setCoupPrice(obj3
								.getDouble("coupPrice"));
						settlementGoodsListInfo.setGoods_id(obj3
								.getInt("goods_id"));
						settlementGoodsListInfo.setId(obj3.getInt("id"));
						settlementGoodsListInfo.setImage_default(obj3
								.getString("image_default"));
						settlementGoodsListInfo.setMktprice(obj3
								.getDouble("mktprice"));
						settlementGoodsListInfo.setName(obj3.getString("name"));
						settlementGoodsListInfo.setNum(obj3.getInt("num"));
						settlementGoodsListInfo.setPrice(obj3
								.getDouble("price"));
						settlementGoodsListInfo.setProduct_id(obj3
								.getInt("product_id"));
						settlementGoodsListInfo.setSn(obj3.getString("sn"));
						settlementGoodsListInfo.setStore_id(obj3
								.getInt("store_id"));
						settlementGoodsListInfo.setStore_name(obj3
								.getString("store_name"));
						settlementGoodsListInfos.add(settlementGoodsListInfo);
					}
					childLists.add(settlementGoodsListInfos);
				}
				adapter.notifyDataSetChanged();
				// list.onRefreshComplete();
				// adapter = new SettlementAdapter(ctx, groupLists, childLists,
				// handler);
				// list.setAdapter(adapter);
				for (int i = 0; i < groupLists.size(); i++) {
					list.expandGroup(i);
				}

				// 合计
				totalTV.setText("" + String.format("%.2f", total));
			} catch (Exception ex) {
				Log.e("LoadCart", ex.getMessage());
			}
			super.onPostExecute(json);
		}
	}


	@Override
	public boolean handleMessage(Message message) {
		Bundle data = message.getData();
		switch (message.what) {
		case 004:
			ArrayList list = data.getParcelableArrayList("shiplist");
//			shiplistInfos.clear();
			shiplistInfos = (ArrayList<ShiplistInfo>) list.get(0);
			 position = data.getInt("position");
			store_id = data.getInt("store_id");
			showCusPopUp(submitBtn);
			backgroundAlpha(0.5f);
			break;
		case 005:
			type_id = data.getInt("type_id");
			shipname = data.getString("shipname");
			window.dismiss();
			new ChangeShipTypeDetailTask().execute();
			groupLists.get(position).setShipname(shipname);
			adapter.notifyDataSetChanged();
			break;
		}
		return false;

	}
}
