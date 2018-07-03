package com.mobileshop.mobile.widget;

import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by Dawei on 3/24/15.
 */
public class JWebChromeClient extends WebChromeClient {
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        Log.d("JWebChromeClient", "" + newProgress);
    }
}
