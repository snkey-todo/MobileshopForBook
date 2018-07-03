package com.mobileshop.mobile.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.mobileshop.mobile.Constants;
import com.mobileshop.mobile.GoodsActivity;
import com.mobileshop.mobile.GoodsListActivity;
import com.mobileshop.mobile.LoginActivity;
import com.mobileshop.mobile.MainActivity;
import com.mobileshop.mobile.MyOrderActivity;
import com.mobileshop.mobile.R;
import com.mobileshop.mobile.SearchActivity;
import com.mobileshop.mobile.widget.JWebViewClient;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
public class HomeFragment extends Fragment {

    private MainActivity mainActivity;

    private final int SEARCH_ACTIVITY = 1;

    private PullToRefreshWebView refreshWebView;

    private WebView webView;
    private Button testBtn;

    private TextView searchTV;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mainActivity = (MainActivity)getActivity();

        //搜索
        searchTV = (TextView)view.findViewById(R.id.home_search);
        searchTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, SearchActivity.class);
                startActivityForResult(intent, SEARCH_ACTIVITY);
            }
        });

        //设置下拉刷新模式
        refreshWebView = (PullToRefreshWebView)view.findViewById(R.id.homeWebView);
        refreshWebView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

        webView = refreshWebView.getRefreshableView();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.setWebViewClient(new JWebViewClient());
        webView.addJavascriptInterface(this, "app");

        //加载Url
        webView.loadUrl(Constants.baseUrl+"/mobile/index.html");
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SEARCH_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                String keyword = data.getStringExtra("keyword");
                Intent intent = new Intent(this.getActivity(), GoodsListActivity.class);
                intent.putExtra("keyword", keyword);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        }
    }

    /**
     * 切换最下方的tab
     * @param index
     */
    @JavascriptInterface
    public void changeTab(final int index){
        mainActivity.runOnUiThread(new Runnable() {
            public void run() {
                mainActivity.changeTab(index);
            }
        });
    }

    @JavascriptInterface
    public void showGoods(final int goodsId){
        mainActivity.runOnUiThread(new Runnable() {
            public void run() {
                mainActivity.showGoods(goodsId);
            }
        });
    }

    /**
     * 显示团购商品详情
     * @param goodsId
     * @param groupbuy_id    团购活动id
     */
    @JavascriptInterface
    public void showGroupbuy(final int goodsId, final int groupbuy_id){
        mainActivity.runOnUiThread(new Runnable() {
            public void run() {
                Intent intent = new Intent(mainActivity, GoodsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("goods_id", goodsId);
                intent.putExtra("groupbuy_id", groupbuy_id);
                startActivity(intent);
            }
        });
    }

    /**
     * 显示秒杀商品详情
     * @param goodsId
     * @param actId     秒杀活动id
     */
    @JavascriptInterface
    public void showSeckill(final int goodsId, final int actId){
        mainActivity.runOnUiThread(new Runnable() {
            public void run() {
                Intent intent = new Intent(mainActivity, GoodsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("goods_id", goodsId);
                intent.putExtra("act_id", actId);
                startActivity(intent);
            }
        });
    }

    /**
     * 显示分类下的商品列表
     * @param cid
     */
    @JavascriptInterface
    public void showList(final int cid){
        mainActivity.runOnUiThread(new Runnable() {
            public void run() {
                mainActivity.showGoodsList(cid, 0);
            }
        });
    }

    /**
     * 显示秒杀商品列表
     */
    @JavascriptInterface
    public void showSeckillList(){
        mainActivity.runOnUiThread(new Runnable() {
            public void run() {
                mainActivity.showSeckillList();
            }
        });
    }

    /**
     * 显示品牌下的商品列表
     * @param brand
     */
    @JavascriptInterface
    public void showBrand(final int brand){
        mainActivity.runOnUiThread(new Runnable() {
            public void run() {
                mainActivity.showGoodsList(0, brand);
            }
        });
    }

    @JavascriptInterface
    public void myorder(){
        if(mainActivity.isLogin()){
            startActivity(new Intent(mainActivity, MyOrderActivity.class));
            return;
        }
        startActivity(new Intent(mainActivity, LoginActivity.class));
        Toast.makeText(mainActivity, "请先登录再进行此项操作！", Toast.LENGTH_SHORT).show();
    }
}
