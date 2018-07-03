package com.huatec.edu.mobileshop.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

/**
 * Created by Dawei on 5/25/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 购物车商品数量
     */
    private static int cartCount = 0;

    /**
     * 是否登录了
     *
     * @return
     */
    public boolean isLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", 0);
        return !TextUtils.isEmpty(sharedPreferences.getString("username", ""));
    }

    /**
     * 显示商品
     * @param goodsid
     */
    public void showGoods(int goodsid){
        Intent intent = new Intent(this, GoodsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("goods_id", goodsid);
        startActivity(intent);
    }


    public int getCartCount() {
        return cartCount;
    }

    public void setCartCount(int cartCount) {
        this.cartCount = cartCount;
    }

}
