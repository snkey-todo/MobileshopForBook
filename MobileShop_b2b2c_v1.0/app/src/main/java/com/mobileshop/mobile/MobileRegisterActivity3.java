package com.mobileshop.mobile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mobileshop.mobile.utils.HttpUtils;

import org.json.JSONObject;

/**
 * Created by Dawei on 12/23/15.
 */
public class MobileRegisterActivity3 extends Activity {

    private EditText passwordEdit;

    private String mobile;
    private String mobileCode;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_register_3);

        ImageView backBtn = (ImageView)findViewById(R.id.title_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mobile = getIntent().getStringExtra("mobile");
        mobileCode = getIntent().getStringExtra("mobileCode");

        passwordEdit = (EditText)findViewById(R.id.password_edit);

        Button okBtn = (Button)findViewById(R.id.ok_button);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    /**
     * 注册
     */
    private void register(){
        final String password = passwordEdit.getText().toString();
        if("".equals(password)){
            Toast.makeText(MobileRegisterActivity3.this, "请输入您要设置的密码！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length() < 6 || password.length() > 12){
            Toast.makeText(MobileRegisterActivity3.this, "密码长度为6到12位！", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog = ProgressDialog.show(MobileRegisterActivity3.this, null, "正在注册…");
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progressDialog.dismiss();
                switch (msg.what) {
                    case -1:
                        Toast.makeText(MobileRegisterActivity3.this, "注册失败，请您重试！", Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        Toast.makeText(MobileRegisterActivity3.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(MobileRegisterActivity3.this, "注册成功！", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor localEditor = getSharedPreferences("user", 0).edit();
                        localEditor.putString("userName", mobile);
                        localEditor.commit();

                        Intent intent = new Intent();
                        intent.setAction("REGISTER_CLOSE");
                        sendBroadcast(intent);
                        finish();

                        break;
                }
                super.handleMessage(msg);
            }
        };

        //发送验证码
        new Thread() {
            @Override
            public void run() {
                String json = HttpUtils.getJson("/api/mobile/member!mobileRegister.do?mobile=" + mobile
                        + "&mobilecode=" + mobileCode + "&password=" + password);
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

                    int result = jsonObject.getInt("result");
                    Message msg = Message.obtain();
                    if(result == 0) {
                        msg.what = 0;
                        msg.obj = jsonObject.getString("message");
                    }else{
                        msg.what = 1;
                    }
                    handler.sendMessage(msg);
                    return;
                }catch(Exception ex){
                    Log.e("registermobile", ex.getMessage());
                }
                handler.sendEmptyMessage(-1);
            }
        }.start();
    }
}
