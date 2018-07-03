package com.mobileshop.mobile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.mobileshop.mobile.utils.StringUtils;

import org.json.JSONObject;

/**
 * Created by Dawei on 12/24/15.
 */
public class FindPasswordActivity1 extends Activity {
    private ImageView backBtn;

    private EditText mobileEdit;

    private Button nextBtn;

    private ProgressDialog progressDialog;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            unregisterReceiver(this);
            ((Activity) context).finish();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("FIND_PASSWORD_CLOSE");
        registerReceiver(this.broadcastReceiver, filter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password_1);

        backBtn = (ImageView)findViewById(R.id.title_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        //手机号码输入框
        mobileEdit = (EditText)findViewById(R.id.mobile_edit);

        nextBtn = (Button)findViewById(R.id.next_button);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mobile = mobileEdit.getText().toString().trim();
                if ("".equals(mobile) || !StringUtils.isMobile(mobile)) {
                    Toast.makeText(FindPasswordActivity1.this, "请输入正确的手机号码！", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog = ProgressDialog.show(FindPasswordActivity1.this, null, "正在发送验证码…");

                //处理发送验证码结果
                final Handler smsHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        progressDialog.dismiss();
                        switch (msg.what) {
                            case -1:
                                Toast.makeText(FindPasswordActivity1.this, "发送验证码失败，请您重试！", Toast.LENGTH_SHORT).show();
                                break;
                            case 0:
                                Toast.makeText(FindPasswordActivity1.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Intent intent = new Intent(FindPasswordActivity1.this, FindPasswordActivity2.class);
                                intent.putExtra("mobile", mobile);
                                startActivity(intent);
                                break;
                        }
                        super.handleMessage(msg);
                    }
                };

                //发送请求
                new Thread() {
                    @Override
                    public void run() {
                        String json = HttpUtils.getJson("/api/mobile/member!sendFindPassCode.do?mobile=" + mobile);
                        if ("".equals(json)) {
                            smsHandler.sendEmptyMessage(-1);
                            return;
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if (jsonObject == null) {
                                smsHandler.sendEmptyMessage(-1);
                                return;
                            }

                            int result = jsonObject.getInt("result");
                            Message msg = Message.obtain();
                            if (result == 0) {
                                msg.what = 0;
                                msg.obj = jsonObject.getString("message");
                            } else {
                                msg.what = 1;
                            }
                            smsHandler.sendMessage(msg);
                            return;
                        } catch (Exception ex) {
                            Log.e("find_pass", ex.getMessage());
                        }
                        smsHandler.sendEmptyMessage(-1);
                    }
                }.start();

            }
        });
    }
}
