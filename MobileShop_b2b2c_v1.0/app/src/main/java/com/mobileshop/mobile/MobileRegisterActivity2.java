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
import android.widget.TextView;
import android.widget.Toast;

import com.mobileshop.mobile.utils.HttpUtils;
import com.mobileshop.mobile.utils.StringUtils;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dawei on 12/23/15.
 */
public class MobileRegisterActivity2 extends Activity {

    private ImageView backBtn;

    private Timer timer;

    private ProgressDialog progressDialog;

    private String mobile = "";

    private EditText mobileCodeEdit;
    private Button resendBtn;

    private Button nextBtn;

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
        filter.addAction("REGISTER_CLOSE");
        registerReceiver(this.broadcastReceiver, filter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_register_2);

        mobile = getIntent().getStringExtra("mobile");

        TextView messageTv = (TextView)findViewById(R.id.message_tv);
        messageTv.setText("验证码已发送至" + mobile);

        backBtn = (ImageView)findViewById(R.id.title_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mobileCodeEdit = (EditText)findViewById(R.id.mobile_code_edit);
        resendBtn = (Button)findViewById(R.id.resend_btn);
        resendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode();
            }
        });

        nextBtn = (Button)findViewById(R.id.next_button);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

        startTimer();
    }

    /**
     * 发送验证码
     */
    private void sendCode(){
        //验证手机号码
        if(StringUtils.isEmpty(mobile) || !StringUtils.isMobile(mobile)){
            Toast.makeText(MobileRegisterActivity2.this, "请输入正确的手机号码！", Toast.LENGTH_SHORT).show();
            return;
        }

        resendBtn.setEnabled(false);
        progressDialog = ProgressDialog.show(MobileRegisterActivity2.this, null, "正在发送验证码…");
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progressDialog.dismiss();
                switch (msg.what) {
                    case -1:
                        Toast.makeText(MobileRegisterActivity2.this, "发送验证码失败！", Toast.LENGTH_SHORT).show();
                        resendBtn.setEnabled(true);
                        break;
                    case 0:
                        Toast.makeText(MobileRegisterActivity2.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        resendBtn.setEnabled(true);
                        break;
                    case 1:
                        startTimer();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        //发送验证码
        new Thread() {
            @Override
            public void run() {
                String json = HttpUtils.getJson("/api/mobile/member!sendRegisterCode.do?mobile=" + mobile);
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
                    Log.e("sendcode", ex.getMessage());
                }
                handler.sendEmptyMessage(-1);
            }
        }.start();
    }

    /**
     * 启用验证码倒计时
     */
    private void startTimer(){
        //发送验证码handler
        final Handler timerHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                resendBtn.setText(msg.what + "秒后重新发送");
                if (msg.what == 0) {
                    resendBtn.setText("再次发送验证码");
                    resendBtn.setEnabled(true);
                }
            }
        };

        //启用倒计时
        resendBtn.setEnabled(false);
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            int i = 60;
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = i--;
                timerHandler.sendMessage(msg);
                if (i < 0) {
                    cancel();
                }
            }
        };
        timer.schedule(timerTask, 1, 1000);
    }

    /**
     * 下一步
     */
    private void next(){
        final String mobileCode = mobileCodeEdit.getText().toString();
        progressDialog = ProgressDialog.show(MobileRegisterActivity2.this, null, "正在加载…");
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progressDialog.dismiss();
                switch (msg.what) {
                    case -1:
                        Toast.makeText(MobileRegisterActivity2.this, "验证码检验失败！", Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        Toast.makeText(MobileRegisterActivity2.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Intent intent = new Intent(MobileRegisterActivity2.this, MobileRegisterActivity3.class);
                        intent.putExtra("mobile", mobile);
                        intent.putExtra("mobileCode", mobileCodeEdit.getText().toString());
                        startActivity(intent);
                        break;
                }
                super.handleMessage(msg);
            }
        };

        //检测验证码
        new Thread() {
            @Override
            public void run() {
                String json = HttpUtils.getJson("/api/mobile/member!validMobile.do?mobile=" + mobile + "&mobilecode=" + mobileCode);
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
                    Log.e("validcode", ex.getMessage());
                }
                handler.sendEmptyMessage(-1);
            }
        }.start();
    }
}
