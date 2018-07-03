package com.mobileshop.mobile.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mobileshop.mobile.Constants;
import com.mobileshop.mobile.MainActivity;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    	api = WXAPIFactory.createWXAPI(this, Constants.WECHAT_APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			switch (resp.errCode){
				case 0:
					paymentCallback(1, "订单支付成功！");
					break;
				case -2:
					paymentCallback(0, "取消支付！");
					break;
				default:
					paymentCallback(0, "支付失败，请您重试！");
					break;
			}
		}
	}

	/**
	 * 处理支付结果
	 * @param result
	 * @param msg
	 */
	protected void paymentCallback(int result, String msg){
		switch (result){
			case 0:
				Toast.makeText(WXPayEntryActivity.this, msg,
						Toast.LENGTH_SHORT).show();
				finish();
				break;
			case 1:
				Toast.makeText(WXPayEntryActivity.this, "支付成功",
						Toast.LENGTH_SHORT).show();
				//关闭这个窗口并跳到首页
				finish();
				Intent intent = new Intent(WXPayEntryActivity.this, MainActivity.class);
				intent.putExtra("action", "toIndex");
				startActivity(intent);
				break;
			case 2:
				Toast.makeText(WXPayEntryActivity.this, "订单正在处理中，请您稍后查询订单状态！",
						Toast.LENGTH_SHORT).show();
				//关闭这个窗口并跳到首页
				finish();
				Intent intent2 = new Intent(WXPayEntryActivity.this, MainActivity.class);
				intent2.putExtra("action", "toIndex");
				startActivity(intent2);
				break;
		}
	}
}