package com.mobileshop.mobile;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mobileshop.mobile.model.Payment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentDeliveryActivity extends Activity {

    private ImageView backIV;

    private RadioGroup paymentRadioGroup;
    private TextView paymentNoticeTV;


    private Button okBtn;

    private Drawable checkedRadioDrawableLeft;


    //支付方式列表
    private List<Payment> paymentList = new ArrayList<>();

    //选择的支付方式
    private Payment payment;


    public PaymentDeliveryActivity() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_delivery);

        backIV = (ImageView)findViewById(R.id.title_back);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        checkedRadioDrawableLeft = getResources().getDrawable(R.drawable.order_pickup_butn_seleted_icon);
        checkedRadioDrawableLeft.setBounds(0, 0, checkedRadioDrawableLeft.getMinimumWidth(), checkedRadioDrawableLeft.getMinimumHeight());

        paymentNoticeTV = (TextView)findViewById(R.id.tv_payment_notice);
        paymentRadioGroup = (RadioGroup)findViewById(R.id.payment_radiogroup);
        paymentRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                for(int i = 0; i < group.getChildCount(); i++){
                    RadioButton rb = (RadioButton)group.getChildAt(i);
                    rb.setCompoundDrawables(null,null,null,null);
                }

                RadioButton radioButton = (RadioButton)findViewById(checkedId);

                //设置选中项的样式
                radioButton.setCompoundDrawables(checkedRadioDrawableLeft,null,null,null);

                payment = (Payment)radioButton.getTag();
                paymentNoticeTV.setText(payment.getBiref());
            }
        });


        //确定
        okBtn = (Button)findViewById(R.id.select_delivery_type_button);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("payment", payment);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        Intent intent = getIntent();
        payment = (Payment)intent.getSerializableExtra("payment");

        //载入支付及配送列表
        paymentList = (List<Payment>)intent.getSerializableExtra("paymentList");
        createPaymentButton();
    }

    /**
     * 动态创建支付方式的RadioButton
     */
    private void createPaymentButton() {
        for (int i = 0; i < paymentList.size(); i++) {
            Payment p = (Payment)paymentList.get(i);

            RadioButton radio = (RadioButton) getLayoutInflater().inflate(R.layout.activity_payment_delivery_radiobutton, null);
            radio.setText(p.getName());
            radio.setTag(p);

            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,0,0,3);
            paymentRadioGroup.addView(radio, layoutParams);

            //选中第一项
            if(payment != null){
                if(payment.getId().equals(p.getId())){
                    radio.setChecked(true);
                    paymentNoticeTV.setText(payment.getBiref());
                }
            }else {
                if (i == 0) {
                    payment = p;
                    radio.setChecked(true);
                    paymentNoticeTV.setText(payment.getBiref());
                }
            }

        }
    }

}
