package com.mobileshop.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mobileshop.mobile.model.Order;
import com.mobileshop.mobile.model.Payment;

/**
 * Created by Dawei on 5/19/15.
 */
public class OrderSuccessActivity extends Activity {

    private TextView orderNumberTV;
    private TextView moneyTV;
    private TextView paymentTV;

    private Button submitBtn;

    public OrderSuccessActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        Order order = (Order)getIntent().getSerializableExtra("order");
        Payment payment = (Payment)getIntent().getSerializableExtra("payment");

        orderNumberTV = (TextView)findViewById(R.id.order_no_content);
        orderNumberTV.setText(order.getSn());

        moneyTV = (TextView)findViewById(R.id.pay_money_content);
        moneyTV.setText(String.format("%.2f", order.getOrder_amount()));

        paymentTV = (TextView)findViewById(R.id.pay_way_content);
        paymentTV.setText(payment.getName());

        //确定
        submitBtn = (Button)findViewById(R.id.submit_completed_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderCompleted();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        orderCompleted();
    }

    private void orderCompleted(){
        finish();
        Intent intent = new Intent(OrderSuccessActivity.this, MainActivity.class);
        intent.putExtra("action", "toIndex");
        startActivity(intent);
    }
}
