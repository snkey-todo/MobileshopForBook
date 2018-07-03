package com.mobileshop.mobile.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobileshop.mobile.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoodsStockFragment extends Fragment {


    public GoodsStockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_stock, container, false);
        return view;
    }


}
