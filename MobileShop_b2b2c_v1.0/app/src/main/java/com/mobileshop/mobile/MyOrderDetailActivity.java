package com.mobileshop.mobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileshop.mobile.model.Order;
import com.mobileshop.mobile.model.OrderItem;
import com.mobileshop.mobile.model.Receipt;
import com.mobileshop.mobile.payment.AlipayMobilePaymentActivity;
import com.mobileshop.mobile.utils.DateUtils;
import com.mobileshop.mobile.utils.HttpUtils;
import com.mobileshop.mobile.utils.StringUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import org.json.JSONObject;

/**
 * Created by Dawei on 5/22/15.
 */
public class MyOrderDetailActivity extends Activity implements View.OnClickListener{

    private ProgressDialog progressDialog;

    private PullToRefreshScrollView pullToRefreshScrollView;

    private ImageView backIV;

    //订单id及对象
    private int orderid = 0;
    private Order order;
    private Receipt receipt;

    //订单状态
    private TextView orderStatusTV;
    private TextView orderNumberTV;

    //收货人
    private TextView receiverMobileTV;
    private TextView receiverNameTV;
    private TextView receiverAddressTV;

    //商品列表
    private LinearLayout goodsContainerLayout;

    //支付及配送
    private TextView paymentTV;
    private TextView shippingTV;
    private LinearLayout shipNumberLayout;
    private TextView shipNumberTV;

    //发票信息
    private LinearLayout receiptLayout;
    private TextView receiptTypeTV;
    private TextView receiptTitleTV;
    private TextView receiptContentTV;

    //支付金额信息
    private TextView goodsAmountTV;
    private TextView shippingAmountTV;
    private TextView orderAmountTV;

    private TextView createTimeTV;

    //底部操作栏
    private LinearLayout bottomLayout;
    private LinearLayout cancenLayout;
    private LinearLayout handleLayout;
    private LinearLayout paymentLayout;
    private Button cancelBtn;
    private Button handleBtn;
    private Button paymentBtn;

    //是否改变了订单状态
    private boolean orderChanged = false;


    public MyOrderDetailActivity() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorder_detail);

        Intent intent = getIntent();
        orderid = intent.getIntExtra("orderid", 0);

        backIV = (ImageView)findViewById(R.id.title_back);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(orderChanged){
                    setResult(1);
                }
                finish();
            }
        });

        pullToRefreshScrollView = (PullToRefreshScrollView)findViewById(R.id.order_detail_scrollView);
        pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });

        orderStatusTV = (TextView)findViewById(R.id.detail_order_status);
        orderNumberTV = (TextView)findViewById(R.id.detail_order_number);

        receiverMobileTV = (TextView)findViewById(R.id.textview_receiver_mobile);
        receiverNameTV = (TextView)findViewById(R.id.textview_receiver_name);
        receiverAddressTV = (TextView)findViewById(R.id.textview_receiver_address);

        goodsContainerLayout = (LinearLayout)findViewById(R.id.goods_container);

        paymentTV = (TextView)findViewById(R.id.order_detail_payment_tv);
        shippingTV = (TextView)findViewById(R.id.order_detail_delivery_tv);
        shipNumberLayout = (LinearLayout)findViewById(R.id.order_detail_ship_number_layout);
        shipNumberTV = (TextView)findViewById(R.id.order_detail_ship_number);

        receiptLayout = (LinearLayout)findViewById(R.id.order_receipt_layout);
        receiptTypeTV = (TextView)findViewById(R.id.order_detail_receipt_type_tv);
        receiptTitleTV = (TextView)findViewById(R.id.order_receipt_title);
        receiptContentTV = (TextView)findViewById(R.id.order_receipt_content_info);

        goodsAmountTV = (TextView)findViewById(R.id.order_detail_goods_amount_tv);
        shippingAmountTV = (TextView)findViewById(R.id.order_detail_shipping_amount_tv);
        orderAmountTV = (TextView)findViewById(R.id.order_amount_content);

        createTimeTV = (TextView)findViewById(R.id.order_create_time);

        bottomLayout = (LinearLayout)findViewById(R.id.order_detail_bottom);
        cancenLayout = (LinearLayout)findViewById(R.id.order_detail_cancel_layout);
        handleLayout = (LinearLayout)findViewById(R.id.order_detail_handle_layout);
        cancelBtn = (Button)findViewById(R.id.order_detail_cancel_btn);
        cancelBtn.setOnClickListener(this);
        handleBtn = (Button)findViewById(R.id.order_detail_handle_btn);
        handleBtn.setOnClickListener(this);
        paymentLayout = (LinearLayout)findViewById(R.id.order_detail_payment_layout);
        paymentBtn = (Button)findViewById(R.id.order_detail_payment_btn);
        paymentBtn.setOnClickListener(this);

        progressDialog = ProgressDialog.show(this, null, "载入中…");
        loadData();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.order_detail_cancel_btn:
                new AlertDialog.Builder(this).setTitle("取消订单").setMessage("您确认要取消此订单吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancel();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();

                break;
            case R.id.order_detail_handle_btn:
                new AlertDialog.Builder(this).setTitle("确认收货").setMessage("请您确认收到货确再进行此操作，否则会有可能财货两空？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rogConfirm();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                break;
            case R.id.order_detail_payment_btn:
                Intent intent = new Intent(MyOrderDetailActivity.this, AlipayMobilePaymentActivity.class);
                intent.putExtra("order", order);
                intent.putExtra("paymentid", order.getPayment_id());
                startActivity(intent);

        }
    }

    /**
     * 取消订单
     */
    private void cancel(){
        progressDialog = ProgressDialog.show(this, null, "取消中…");
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progressDialog.dismiss();
                switch (msg.what){
                    case -1:
                        Toast.makeText(MyOrderDetailActivity.this, "取消失败！", Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        Toast.makeText(MyOrderDetailActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        progressDialog = ProgressDialog.show(MyOrderDetailActivity.this, null, "载入中…");
                        orderChanged = true;
                        loadData();
                        Toast.makeText(MyOrderDetailActivity.this, "取消订单成功", Toast.LENGTH_SHORT).show();
                        break;

                }
                super.handleMessage(msg);
            }
        };
        new Thread() {
            @Override
            public void run() {
                String json = HttpUtils.getJson("/api/mobile/order!cancel.do?sn=" + order.getSn() + "&reason=");
                if("".equals(json)){
                    handler.sendEmptyMessage(-1);
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if(jsonObject == null){
                        handler.sendEmptyMessage(-1);
                        return;
                    }

                    Message msg = Message.obtain();
                    msg.what = jsonObject.getInt("result");
                    msg.obj = jsonObject.getString("message");
                    handler.sendMessage(msg);
                    return;
                }catch(Exception ex){
                    Log.e("Cancel Order:", ex.getMessage());
                }
            }
        }.start();
    }

    /**
     * 确认收货
     */
    private void rogConfirm(){
        progressDialog = ProgressDialog.show(this, null, "操作中…");
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progressDialog.dismiss();
                switch (msg.what){
                    case -1:
                        Toast.makeText(MyOrderDetailActivity.this, "确认收货失败！", Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        Toast.makeText(MyOrderDetailActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        progressDialog = ProgressDialog.show(MyOrderDetailActivity.this, null, "载入中…");
                        orderChanged = true;
                        loadData();
                        Toast.makeText(MyOrderDetailActivity.this, "确认收货成功", Toast.LENGTH_SHORT).show();
                        break;

                }
                super.handleMessage(msg);
            }
        };
        new Thread() {
            @Override
            public void run() {
                String json = HttpUtils.getJson("/api/mobile/order!rogConfirm.do?orderid=" + order.getOrder_id());
                if("".equals(json)){
                    handler.sendEmptyMessage(-1);
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if(jsonObject == null){
                        handler.sendEmptyMessage(-1);
                        return;
                    }

                    Message msg = Message.obtain();
                    msg.what = jsonObject.getInt("result");
                    msg.obj = jsonObject.getString("message");
                    handler.sendMessage(msg);
                    return;
                }catch(Exception ex){
                    Log.e("rogConfirm Order:", ex.getMessage());
                }
            }
        }.start();
    }

    /**
     * 载入数据
     */
    private void loadData(){
        //处理数据
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case -1:
                        Toast.makeText(MyOrderDetailActivity.this, "载入失败！", Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        Toast.makeText(MyOrderDetailActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        JSONObject jsonObject = (JSONObject)msg.obj;
                        try {
                            order = Order.toOrder(jsonObject.getJSONObject("order"));
                            JSONObject receiptJson = null;
                            try{
                                receiptJson = jsonObject.getJSONObject("receipt");
                            }catch(Exception ex){
                                receiptJson = null;
                            }
                            receipt = Receipt.toReceipt(receiptJson);
                            if(receipt == null) {
                                receipt = new Receipt(0, "", "");
                            }
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                        initData();
                        break;

                }
                super.handleMessage(msg);
            }
        };
        new Thread() {
            @Override
            public void run() {
                String json = HttpUtils.getJson("/api/mobile/order!detail.do?orderid=" + orderid);
                if("".equals(json)){
                    handler.sendEmptyMessage(-1);
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if(jsonObject == null){
                        handler.sendEmptyMessage(-1);
                        return;
                    }

                    Message msg = Message.obtain();
                    msg.what = jsonObject.getInt("result");
                    if(msg.what == 0) {
                        msg.obj = jsonObject.getString("message");
                    }else{
                        msg.obj = jsonObject.getJSONObject("data");
                    }
                    handler.sendMessage(msg);
                    return;
                }catch(Exception ex){
                    Log.e("Load Order Detail:", ex.getMessage());
                }
            }
        }.start();
    }

    /**
     * 初始化数据到相应的控件
     */
    private void initData(){
        orderStatusTV.setText(order.getOrderStatus());
        orderNumberTV.setText(order.getSn());

        receiverMobileTV.setText(order.getShip_mobile());
        receiverNameTV.setText(order.getShip_name());
        receiverAddressTV.setText(order.getShipping_area() + " " + order.getShip_addr());

        goodsContainerLayout.removeAllViews();
        if(order.getItemList() != null){
            for(OrderItem orderItem : order.getItemList()){
                RelativeLayout goodsLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_checkout_goods_item, null);

                ImageView imageIV = (ImageView)goodsLayout.findViewById(R.id.product_image);
                Constants.imageLoader.displayImage(orderItem.getImage(), imageIV, Constants.displayImageOptions);

                TextView nameTV = (TextView)goodsLayout.findViewById(R.id.product_name);
                nameTV.setText(orderItem.getName());

                TextView numberTV = (TextView)goodsLayout.findViewById(R.id.product_num);
                numberTV.setText("x" + orderItem.getNum());

                TextView priceTV = (TextView)goodsLayout.findViewById(R.id.product_price);
                priceTV.setText("￥" + String.format("%.2f", orderItem.getPrice()));

                RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                goodsContainerLayout.addView(goodsLayout, layoutParams);
            }
        }

        paymentTV.setText(order.getPayment_name());
        shippingTV.setText(order.getShipping_type());
        if(StringUtils.isEmpty(order.getShip_no())){
            shipNumberLayout.setVisibility(View.GONE);
        }else{
            shipNumberLayout.setVisibility(View.VISIBLE);
            shipNumberTV.setText(order.getShip_no());
        }

        if(StringUtils.isEmpty(receipt.getTitle())){
            receiptTypeTV.setText("不开发票");
            receiptLayout.setVisibility(View.GONE);
        }else{
            receiptLayout.setVisibility(View.VISIBLE);
            receiptTypeTV.setText("纸质发票");
            receiptTitleTV.setText("发票抬头：" + receipt.getTitle());
            receiptContentTV.setText("发票内容：" + receipt.getContent());
        }

        goodsAmountTV.setText("￥" + String.format("%.2f", order.getGoods_amount()));
        shippingAmountTV.setText("￥" + String.format("%.2f", order.getShipping_amount()));
        orderAmountTV.setText("￥" + String.format("%.2f", order.getOrder_amount()));

        createTimeTV.setText("下单时间：" + DateUtils.toString(order.getCreate_time(), "yyyy-MM-dd HH:mm:ss"));

        //操作栏
        if(order.getStatus().intValue() == 0){
            cancenLayout.setVisibility(View.VISIBLE);
            handleLayout.setVisibility(View.GONE);
            bottomLayout.setVisibility(View.VISIBLE);
            if(order.getPayment_type().equals("alipayMobilePlugin")){
                paymentLayout.setVisibility(View.VISIBLE);
            }else{
                paymentLayout.setVisibility(View.GONE);
            }
        }else if(order.getStatus().intValue() == 5){
            cancenLayout.setVisibility(View.GONE);
            paymentLayout.setVisibility(View.GONE);
            handleLayout.setVisibility(View.VISIBLE);
            bottomLayout.setVisibility(View.VISIBLE);
        }else{
            bottomLayout.setVisibility(View.GONE);
        }

        if(progressDialog != null)
            progressDialog.dismiss();
        pullToRefreshScrollView.onRefreshComplete();

    }



}
