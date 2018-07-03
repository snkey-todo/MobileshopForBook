package com.mobileshop.mobile.payment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileshop.mobile.MainActivity;
import com.mobileshop.mobile.R;
import com.mobileshop.mobile.model.Order;
import com.mobileshop.mobile.model.Payment;
import com.mobileshop.mobile.utils.HttpUtils;

import org.json.JSONObject;

/**
 * Created by Dawei on 6/16/15.
 */
public abstract class PaymentActivity extends Activity {

    /**
     * 订单信息
     */
    protected Order order;

    /**
     * 支付方式
     */
    protected Payment payment;

    /**
     * 支付方式id
     */
    private int paymentid;

    private ProgressDialog progressDialog;

    protected RelativeLayout alipayLayout;
    protected RelativeLayout wechatLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        order = (Order)getIntent().getSerializableExtra("order");
        paymentid = getIntent().getIntExtra("paymentid", 0);

        TextView moneyTV = (TextView)findViewById(R.id.order_amount_money);
        moneyTV.setText(String.format("%.2f",order.getOrder_amount()) + "元");

        ImageView backIV = (ImageView)findViewById(R.id.title_back);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

        alipayLayout = (RelativeLayout)findViewById(R.id.alipay);
        alipayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay();
            }
        });

        wechatLayout = (RelativeLayout)findViewById(R.id.wechat);
        wechatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay();
            }
        });

        loadPayment();

    }

    /**
     * 载入数据
     */
    private void loadPayment(){
        progressDialog = ProgressDialog.show(PaymentActivity.this, null, "载入中…");
        //处理数据
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progressDialog.dismiss();
                switch (msg.what){
                    case -1:
                        Toast.makeText(PaymentActivity.this, "载入失败！", Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        Toast.makeText(PaymentActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        JSONObject jsonObject = (JSONObject)msg.obj;
                        try {
                            payment = Payment.toPayment(jsonObject);
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                        break;

                }
                super.handleMessage(msg);
            }
        };
        new Thread() {
            @Override
            public void run() {
                String json = HttpUtils.getJson("/api/mobile/order!payment.do?id=" + paymentid);
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
                    Log.e("Load Payment Detail:", ex.getMessage());
                }
            }
        }.start();
    }

    /**
     * 支付
     */
    public abstract void pay();

    /**
     * 处理支付结果
     * @param result
     * @param msg
     */
    protected void paymentCallback(int result, String msg){
        switch (result){
            case 0:
                Toast.makeText(PaymentActivity.this, msg,
                        Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(PaymentActivity.this, "支付成功",
                        Toast.LENGTH_SHORT).show();
                //关闭这个窗口并跳到首页
                finish();
                Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                intent.putExtra("action", "toIndex");
                startActivity(intent);
                break;
            case 2:
                Toast.makeText(PaymentActivity.this, "订单正在处理中，请您稍后查询订单状态！",
                        Toast.LENGTH_SHORT).show();
                //关闭这个窗口并跳到首页
                finish();
                Intent intent2 = new Intent(PaymentActivity.this, MainActivity.class);
                intent2.putExtra("action", "toIndex");
                startActivity(intent2);
                break;
        }
    }
}
