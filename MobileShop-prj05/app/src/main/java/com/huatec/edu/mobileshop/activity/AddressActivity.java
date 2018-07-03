package com.huatec.edu.mobileshop.activity;

import android.os.Bundle;

import com.huatec.edu.mobileshop.R;

import butterknife.ButterKnife;

public class AddressActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);


    }


}
