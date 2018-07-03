package com.mobileshop.mobile;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileshop.mobile.model.Receipt;
import com.mobileshop.mobile.utils.StringUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReceiptActivity extends Activity {

    private ImageView backIV;

    private RadioGroup invoiceTypeRadioGroup;

    private LinearLayout invoiceGeneralLayout;
    //选中的发票内容RadioButton
    private RadioButton invoiceGeneralRB;
    private TextView invoiceGeneralTV;

    //发票抬头
    private RelativeLayout invoiceTitleLayout;
    private EditText companyET;

    private LinearLayout invoiceContentLayout;

    private Button okBtn;

    //选中的发票信息
    private Receipt receipt = null;

    public ReceiptActivity() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        Intent intent = getIntent();
        receipt = (Receipt)intent.getSerializableExtra("receipt");

        backIV = (ImageView)findViewById(R.id.title_back);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        invoiceTypeRadioGroup = (RadioGroup)findViewById(R.id.invoice_type_raidogroup);
        invoiceTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setInvoiceType();
            }
        });

        invoiceTitleLayout = (RelativeLayout)findViewById(R.id.invoice_title_layout);
        companyET = (EditText)findViewById(R.id.company_name);
        companyET.setText(receipt.getTitle());

        invoiceGeneralLayout = (LinearLayout)findViewById(R.id.invoice_general_layout);
        for(int i = 0; i < invoiceGeneralLayout.getChildCount(); i++){
            final LinearLayout layout = (LinearLayout)invoiceGeneralLayout.getChildAt(i);
            final TextView textView = (TextView)layout.getChildAt(1);
            final RadioButton radioButton = (RadioButton)layout.getChildAt(0);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    invoiceGeneralRB.setChecked(false);
                    radioButton.setChecked(!radioButton.isChecked());
                    invoiceGeneralRB = radioButton;
                    invoiceGeneralTV = textView;
                }
            });
            if(!StringUtils.isEmpty(receipt.getContent())) {
                if (receipt.getContent().equals(textView.getText())) {
                    invoiceGeneralRB = radioButton;
                    invoiceGeneralTV = textView;
                    radioButton.setChecked(true);
                }
            }else{
                if(i == 0){
                    invoiceGeneralRB = radioButton;
                    invoiceGeneralTV = textView;
                    radioButton.setChecked(true);
                }
            }
        }

        invoiceContentLayout = (LinearLayout)findViewById(R.id.invoice_content_layout);

        //确定
        okBtn = (Button)findViewById(R.id.btn_comfirm);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //验证发票抬头
                RadioButton typeRadioButton = (RadioButton)findViewById(invoiceTypeRadioGroup.getCheckedRadioButtonId());
                if(!typeRadioButton.getTag().toString().equals("0")){
                    if(StringUtils.isEmpty(companyET.getText().toString())){
                        Toast.makeText(ReceiptActivity.this, "请输入发票抬头！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                //确定
                Intent returnIntent = new Intent();
                Receipt receipt = new Receipt(
                        Integer.parseInt(typeRadioButton.getTag().toString()),
                        companyET.getText().toString(),
                        invoiceGeneralTV.getText().toString());
                returnIntent.putExtra("receipt", receipt);
                setResult(RESULT_OK, returnIntent);
                finish();

            }
        });

        //设置默认发票
        for(int i = 0; i < invoiceTypeRadioGroup.getChildCount(); i++){
            RadioButton radioButton = (RadioButton)invoiceTypeRadioGroup.getChildAt(i);
            if (Integer.parseInt(radioButton.getTag().toString()) == receipt.getType()) {
                radioButton.setChecked(true);
                break;
            }
        }
        setInvoiceType();
    }

    private void setInvoiceType(){
        RadioButton typeRadioButton = (RadioButton)findViewById(invoiceTypeRadioGroup.getCheckedRadioButtonId());
        if(typeRadioButton.getTag().toString().equals("0")){
            invoiceContentLayout.setVisibility(View.GONE);
            invoiceTitleLayout.setVisibility(View.GONE);
            return;
        }
        invoiceContentLayout.setVisibility(View.VISIBLE);
        invoiceTitleLayout.setVisibility(View.VISIBLE);
    }


}
