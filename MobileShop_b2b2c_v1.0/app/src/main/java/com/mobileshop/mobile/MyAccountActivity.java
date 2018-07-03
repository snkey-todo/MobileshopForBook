package com.mobileshop.mobile;

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
 * Created by Dawei on 5/26/15.
 */
public class MyAccountActivity extends BaseActivity {

    private ProgressDialog progressDialog;

    private ImageView backIV;

    private EditText oldPassET;
    private EditText newPassET;
    private EditText rePassET;
    private Button changeBtn;

    public MyAccountActivity() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccount);

        backIV = (ImageView)findViewById(R.id.title_back);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        oldPassET = (EditText)findViewById(R.id.password_input_oldpass);
        newPassET = (EditText)findViewById(R.id.password_input_newpass);
        rePassET = (EditText)findViewById(R.id.password_input_repass);
        changeBtn = (Button)findViewById(R.id.change_button);

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String oldpass = oldPassET.getText().toString().trim();
                final String newpass = newPassET.getText().toString().trim();
                final String repass = rePassET.getText().toString().trim();

                if("".equals(oldpass)){
                    Toast.makeText(MyAccountActivity.this, "旧密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if("".equals(newpass)){
                    Toast.makeText(MyAccountActivity.this, "新密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!repass.equals(newpass)){
                    Toast.makeText(MyAccountActivity.this, "两次输入密码不一致！", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog = ProgressDialog.show(MyAccountActivity.this, null, "正在修改密码…");

                //处理数据
                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        progressDialog.dismiss();
                        switch (msg.what){
                            case -1:
                                Toast.makeText(MyAccountActivity.this, "修改密码失败！", Toast.LENGTH_SHORT).show();
                                break;
                            case 0:
                                Toast.makeText(MyAccountActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(MyAccountActivity.this, "修改密码成功,请您重新登录！", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor localEditor = getSharedPreferences("user", 0).edit();
                                localEditor.remove("userName");
                                localEditor.commit();
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("changepass", true);
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
                        String json = HttpUtils.getJson("/api/mobile/member!changePassword.do?oldpassword=" + oldpass + "&password=" + newpass + "&re_passwd=" + repass);
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
                            Log.e("Change Password", ex.getMessage());
                        }
                        handler.sendEmptyMessage(-1);
                    }
                }.start();
            }
        });




    }

}
