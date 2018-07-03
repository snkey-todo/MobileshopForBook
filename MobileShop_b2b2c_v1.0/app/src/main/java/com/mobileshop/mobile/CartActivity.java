package com.mobileshop.mobile;

import android.os.Bundle;

import com.mobileshop.mobile.fragment.CartFragment;

/**
 * Created by Dawei on 5/25/15.
 */
public class CartActivity extends BaseActivity {

    private CartFragment cartFragment;

    public CartActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (cartFragment == null) {
            cartFragment = new CartFragment();
        }
        getFragmentManager().beginTransaction()
                .add(R.id.main_frame, cartFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
