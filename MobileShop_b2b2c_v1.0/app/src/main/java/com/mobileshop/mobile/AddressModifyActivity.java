package com.mobileshop.mobile;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileshop.mobile.model.Address;
import com.mobileshop.mobile.model.Region;
import com.mobileshop.mobile.utils.HttpUtils;
import com.mobileshop.mobile.utils.StringUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddressModifyActivity extends Activity {

    private ImageView backIV;

    private RelativeLayout selectAddressLayout;
    private TextView regionTV;

    private EditText nameET;
    private EditText mobileET;
    private EditText addressET;
    private Button saveBtn;

    private Region province;
    private Region city;
    private Region county;

    private ProgressDialog progressDialog;

    //添加成功的地址
    private Address createdAddress;

    public AddressModifyActivity() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_modify);

        createdAddress = (Address)getIntent().getSerializableExtra("address");

        backIV = (ImageView)findViewById(R.id.title_back);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        regionTV = (TextView)findViewById(R.id.order_address_region_content);
        //选择地区
        selectAddressLayout = (RelativeLayout)findViewById(R.id.order_address_region_layout);
        selectAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddressModifyActivity.this, SelectAddressActivity.class), 1);
            }
        });

        nameET = (EditText)findViewById(R.id.order_address_name_content);
        mobileET = (EditText)findViewById(R.id.order_address_mobile_content);
        addressET = (EditText)findViewById(R.id.customer_addr_content);
        saveBtn = (Button)findViewById(R.id.address_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameET.getText().toString();
                if(StringUtils.isEmpty(name)){
                    Toast.makeText(AddressModifyActivity.this, "请输入收货人姓名！", Toast.LENGTH_SHORT).show();
                    return;
                }

                String mobile = mobileET.getText().toString();
                if(StringUtils.isEmpty(mobile)){
                    Toast.makeText(AddressModifyActivity.this, "请输入手机号码！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(province == null || city == null || county == null){
                    Toast.makeText(AddressModifyActivity.this, "请选择所在地区！", Toast.LENGTH_SHORT).show();
                    return;
                }

                String address = addressET.getText().toString();
                if(StringUtils.isEmpty(address)){
                    Toast.makeText(AddressModifyActivity.this, "请输入详细地址！", Toast.LENGTH_SHORT).show();
                    return;
                }
                save();
            }
        });

        //填充数据
        if(createdAddress != null && createdAddress.getAddr_id() != null){
            nameET.setText(createdAddress.getName());
            mobileET.setText(createdAddress.getMobile());
            addressET.setText(createdAddress.getAddr());
            province = new Region(createdAddress.getProvince(), createdAddress.getProvince_id());
            city = new Region(createdAddress.getCity(), createdAddress.getCity_id());
            county = new Region(createdAddress.getRegion(), createdAddress.getRegion_id());
            regionTV.setText(createdAddress.getProvince() + createdAddress.getCity() + createdAddress.getRegion());
            ((TextView)findViewById(R.id.title_textview)).setText("编辑收货地址");
        }
    }

    private void save(){
        progressDialog = ProgressDialog.show(this, null, "正在保存...");
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progressDialog.dismiss();
                switch (msg.what) {
                    case -1:
                        Toast.makeText(AddressModifyActivity.this, "保存失败，请您重试！", Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        Toast.makeText(AddressModifyActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("address", createdAddress);
                        setResult(RESULT_OK, returnIntent);
                        finish();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        new Thread() {
            @Override
            public void run() {

                Map<String,String> params = new HashMap<String, String>();
                params.put("name", nameET.getText().toString());
                params.put("province_id", province.getRegion_id().toString());
                params.put("city_id", city.getRegion_id().toString());
                params.put("region_id", county.getRegion_id().toString());
                params.put("addr", addressET.getText().toString());
                params.put("mobile", mobileET.getText().toString());
                String json = "";
                if(createdAddress == null || createdAddress.getAddr_id() == null) {
                    json = HttpUtils.post("/api/mobile/address!add.do", params);
                }else{
                    params.put("addr_id", createdAddress.getAddr_id().toString());
                    json = HttpUtils.post("/api/mobile/address!edit.do", params);
                }
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

                    Message message = Message.obtain();
                    message.what = jsonObject.getInt("result");
                    if(message.what == 1) {
                        createdAddress = Address.toAddress(jsonObject.getJSONObject("data").getJSONObject("address"));
                    }else{
                        message.obj = jsonObject.getString("message");
                    }
                    handler.sendMessage(message);
                } catch (Exception ex) {
                    Log.e("Save Member Address", ex.getMessage());
                }
            }
        }.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                province = (Region)data.getSerializableExtra("province");
                city = (Region)data.getSerializableExtra("city");
                county = (Region)data.getSerializableExtra("county");
                String region = province.getLocal_name();
                region += city != null ? city.getLocal_name(): "";
                region += county != null ? county.getLocal_name(): "";
                regionTV.setText(region);
            }
        }
    }


}
