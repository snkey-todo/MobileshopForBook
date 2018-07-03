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
 * Created by Dawei on 4/23/15.
 */
public class RegisterActivity extends Activity {

    private ImageView backBtn;

    private EditText usernameEt;
    private EditText passwordEt;
    private EditText repasswordEt;
    private Button registerBtn;

    //正在注册对话框
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        backBtn = (ImageView)findViewById(R.id.title_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });


        usernameEt = (EditText)findViewById(R.id.login_input_name);
        passwordEt = (EditText)findViewById(R.id.login_input_password);
        repasswordEt = (EditText)findViewById(R.id.login_input_repassword);

        registerBtn = (Button)findViewById(R.id.register_button);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameEt.getText().toString().trim();
                final String password = passwordEt.getText().toString().trim();
                final String repassword = repasswordEt.getText().toString().trim();
                if("".equals(username)){
                    Toast.makeText(RegisterActivity.this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (username.length() < 4 || username.length() > 20) {
                    Toast.makeText(RegisterActivity.this, "用户名的长度为4-20个字符！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (username.contains("@")) {
                    Toast.makeText(RegisterActivity.this, "用户名中不能包含@等特殊字符！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if("".equals(password)){
                    Toast.makeText(RegisterActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!password.equals(repassword)){
                    Toast.makeText(RegisterActivity.this, "两次输入密码不一致！", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog = ProgressDialog.show(RegisterActivity.this, null, "正在注册…");

                //处理数据
                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        progressDialog.dismiss();
                        switch (msg.what){
                            case -1:
                                Toast.makeText(RegisterActivity.this, "注册失败！", Toast.LENGTH_SHORT).show();
                                break;
                            case 0:
                                Toast.makeText(RegisterActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor localEditor = getSharedPreferences("user", 0).edit();
                                localEditor.putString("username", username);
                                try {
                                    localEditor.putString("face", "");
                                    localEditor.putString("level", "普通会员");
                                }catch(Exception ex){}
                                localEditor.commit();

                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("logined", true);
                                setResult(RESULT_OK, returnIntent);
                                finish();
                                break;
                        }
                        super.handleMessage(msg);
                    }
                };

                //发送请求
                new Thread() {
                    @Override
                    public void run() {
                        String json = HttpUtils.getJson("/api/mobile/member!register.do?username=" + username + "&password=" + password);
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
                            Log.e("loadCategories", ex.getMessage());
                        }
                        handler.sendEmptyMessage(-1);
                    }
                }.start();

            }
        });

    }
}
