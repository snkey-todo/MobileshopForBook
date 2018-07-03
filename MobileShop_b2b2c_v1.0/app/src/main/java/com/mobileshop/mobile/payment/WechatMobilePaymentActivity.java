package com.mobileshop.mobile.payment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.View;

import com.mobileshop.mobile.Constants;
import com.mobileshop.mobile.utils.CurrencyUtil;
import com.mobileshop.mobile.utils.StringUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.wechat.MD5;
import com.tencent.wechat.Util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 微信支付
 */
public class WechatMobilePaymentActivity extends PaymentActivity{

    private static final String TAG = "WechatMobilePaymentActivity";

    private static final String SHARED_PREFERENCE_NAME = "WechatPay";

    //appid
    private String appId = "";

    //商户号
    public String mchId = "";

    //  API密钥，在商户平台设置
    public String apiKey = "";

    private final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);

    private PayReq req;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alipayLayout.setVisibility(View.GONE);
    }

    //处理支付
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(progressDialog != null)
                progressDialog.dismiss();

            Map<String,String> unifiedorder = (Map<String,String>)msg.obj;
            if(unifiedorder != null && unifiedorder.containsKey("prepay_id") && !StringUtils.isEmpty(unifiedorder.get("prepay_id"))) {
                SharedPreferences.Editor localEditor = getSharedPreferences(SHARED_PREFERENCE_NAME, 0).edit();
                localEditor.putString(order.getSn(), unifiedorder.get("prepay_id"));
                localEditor.commit();
            }

            req = new PayReq();
            genPayReq(unifiedorder.get("prepay_id"));
            sendPayReq();
            super.handleMessage(msg);
        }
    };

    /**
     * 调用SDK支付
     */
    public void pay() {

        //解析PayCfg中的参数
        JSONObject paramObject = payment.getConfigJson();
        try {
            appId = paramObject.getString("appid");
            mchId = paramObject.getString("mchid");
            apiKey = paramObject.getString("key");
        }catch(JSONException ex){
            ex.printStackTrace();
        }

        progressDialog = ProgressDialog.show(this, null, "正在支付...");

        //向微信注册此app
        msgApi.registerApp(appId);

        //是否已经支付过
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        if(sharedPreferences.contains(order.getSn())){
            if(progressDialog != null)
                progressDialog.dismiss();

            req = new PayReq();
            genPayReq(sharedPreferences.getString(order.getSn(), ""));
            sendPayReq();
            return;
        }

        //生成prepay_id进行支付
        new Thread() {
            @Override
            public void run() {

                String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
                String entity = genProductArgs();

                Log.e("orion",entity);

                byte[] buf = Util.httpPost(url, entity);

                String content = new String(buf);
                Log.e("orion", content);
                Map<String,String> unifiedorder = decodeXml(content);

                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = unifiedorder;
                handler.sendMessage(msg);
            }
        }.start();

    }

    public Map<String,String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName=parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if("xml".equals(nodeName)==false){
                            //实例化student对象
                            xml.put(nodeName,parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("orion",e.toString());
        }
        return null;

    }

    /**
     生成签名
     */
    private String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(apiKey);

        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion",packageSign);
        return packageSign;
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(apiKey);

        System.out.println("sign str\n"+sb.toString()+"\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion",appSign);
        return appSign;
    }

    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<"+params.get(i).getName()+">");


            sb.append(params.get(i).getValue());
            sb.append("</"+params.get(i).getName()+">");
        }
        sb.append("</xml>");

        Log.e("orion",sb.toString());
        return sb.toString();
    }


    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }


    /**
     * 生成订单参数
     * @return
     */
    private String genProductArgs() {
        StringBuffer xml = new StringBuffer();

        try {
            String	nonceStr = genNonceStr();


            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid", appId));
            packageParams.add(new BasicNameValuePair("body", "" + order.getSn()));
            packageParams.add(new BasicNameValuePair("mch_id", mchId));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", Constants.baseUrl + "/api/shop/s_wechatMobilePlugin_payment-callback.do"));
            packageParams.add(new BasicNameValuePair("out_trade_no",order.getSn()));
            packageParams.add(new BasicNameValuePair("spbill_create_ip","127.0.0.1"));
            packageParams.add(new BasicNameValuePair("total_fee", toFen(order.getNeed_pay_money())));
//            packageParams.add(new BasicNameValuePair("total_fee", "1"));
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));


            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));


            String xmlstring =toXml(packageParams);

            return xmlstring;

        } catch (Exception e) {
            Log.e(TAG, "genProductArgs fail," + e.getMessage());
            return null;
        }


    }
    private void genPayReq(String prepayId) {
        req.appId = appId;
        req.partnerId = mchId;
        req.prepayId = prepayId;
        req.packageValue = "Sign=WXPay";
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());


        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);

        System.out.println("sign\n"+req.sign+"\n\n");

        Log.e("orion", signParams.toString());

    }

    private void sendPayReq() {
        msgApi.registerApp(appId);
        msgApi.sendReq(req);
    }

    private String toFen(Double money) {
        money = CurrencyUtil.mul(money, 100);
        String str = String.valueOf(money);
        str = str.substring(0, str.indexOf("."));
        return str;
    }
}
