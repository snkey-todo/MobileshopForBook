package com.mobileshop.mobile.widget;

import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class JWebViewClient extends WebViewClient
{
    public void onReceivedError(WebView paramWebView, int paramInt, String paramString1, String paramString2)
    {
        paramWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        super.onReceivedError(paramWebView, paramInt, paramString1, paramString2);
        paramWebView.loadDataWithBaseURL("", "<div style='padding-top:200px;text-align:center;color:#666;'>未打开无线网络</div>", "text/html", "UTF-8", "");
    }

    public boolean shouldOverrideKeyEvent(WebView paramWebView, KeyEvent paramKeyEvent)
    {
        return super.shouldOverrideKeyEvent(paramWebView, paramKeyEvent);
    }

    public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
    {
        paramWebView.loadUrl(paramString);
        return true;
    }


    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }


}