package com.mobileshop.mobile;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mobileshop.mobile.widget.JWebViewClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoodsDetailActivity extends Activity {

    private int goods_id;

    private WebView webView;
    private RelativeLayout goodsIntro;
    private RelativeLayout goodsAttr;

    private ImageView back;


    public GoodsDetailActivity() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);

        Intent intent = getIntent();
        this.goods_id = intent.getIntExtra("goods_id", 0);

        back = (ImageView)findViewById(R.id.title_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        webView = (WebView)findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        webView.setWebViewClient(new JWebViewClient());
        webView.loadUrl(Constants.baseUrl + "/mobile/goods-" + goods_id + ".html");

        goodsIntro = (RelativeLayout)findViewById(R.id.goods_intro);
        goodsAttr = (RelativeLayout)findViewById(R.id.goods_attr);

        goodsIntro.setSelected(true);

        goodsIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl(Constants.baseUrl + "/mobile/goods-" + goods_id + ".html");
                goodsIntro.setSelected(true);
                goodsAttr.setSelected(false);
            }
        });

        goodsAttr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl(Constants.baseUrl + "/mobile/goodsattr-" + goods_id + ".html");
                goodsIntro.setSelected(false);
                goodsAttr.setSelected(true);
            }
        });
    }
}
