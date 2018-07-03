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
import android.widget.TextView;
import android.widget.Toast;

import com.mobileshop.mobile.utils.HttpUtils;

import org.json.JSONObject;

/**
 * Created by Dawei on 4/23/15.
 */
public class LoginActivity extends Activity {

    private ImageView backBtn;
    private TextView registerBtn;
    private TextView findPasswordText;

    private EditText usernameEt;
    private EditText passwordEt;
    private Button loginBtn;

    //正在登录对话框
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        backBtn = (ImageView)findViewById(R.id.title_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        registerBtn = (TextView)findViewById(R.id.register_link);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constants.MOBILE_VALIDATION) {
                    startActivity(new Intent(LoginActivity.this, MobileRegisterActivity1.class));
                } else {
                    startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), 1);
                }
            }
        });

        //找回密码
        findPasswordText = (TextView)findViewById(R.id.find_password_text);
        if(Constants.MOBILE_VALIDATION){
            findPasswordText.setVisibility(View.VISIBLE);
            findPasswordText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, FindPasswordActivity1.class));
                }
            });
        }

        usernameEt = (EditText)findViewById(R.id.login_input_name);
        passwordEt = (EditText)findViewById(R.id.login_input_password);

        loginBtn = (Button)findViewById(R.id.login_button);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameEt.getText().toString().trim();
                final String password = passwordEt.getText().toString().trim();
                if("".equals(username)){
                    Toast.makeText(LoginActivity.this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if("".equals(password)){
                    Toast.makeText(LoginActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog = ProgressDialog.show(LoginActivity.this, null, "登录中…");

                //处理登录数据
                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) { //处理登陆过程中的不同情况
                        progressDialog.dismiss();//得到请求结果，取消对话框
                        switch (msg.what){
                            case -1:
                                Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                                break;
                            case 0:
                                Toast.makeText(LoginActivity.this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                                //使用SharedPreferences将用户信息保存到本地
                                JSONObject jsonObject = (JSONObject)msg.obj;
                                SharedPreferences.Editor localEditor = getSharedPreferences("user", 0).edit();
                                localEditor.putString("username", username);
                                try {
                                    localEditor.putString("face", jsonObject.getString("face"));
                                    localEditor.putString("level", jsonObject.getString("level"));
                                }catch(Exception ex){
                                }
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

                //发送登录请求
                new Thread() {
                    @Override
                    public void run() {
                        //String json = HttpUtils.getJson("/api/mobile/member!loginApp.do?username=" + username + "&password=" + password);
                        String json = HttpUtils.loginByGet(username,password);
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
                            if(result == 0) {
                                handler.sendEmptyMessage(jsonObject.getInt("result"));
                            }else{
                                Message msg = Message.obtain();
                                msg.what = 1;
                                msg.obj = jsonObject.getJSONObject("data");
                                handler.sendMessage(msg);
                            }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            Intent returnIntent = new Intent();
            returnIntent.putExtra("logined", true);
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }
}
