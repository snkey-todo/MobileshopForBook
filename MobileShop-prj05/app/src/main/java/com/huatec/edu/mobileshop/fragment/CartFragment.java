package com.huatec.edu.mobileshop.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.huatec.edu.mobileshop.R;
import com.huatec.edu.mobileshop.activity.BaseActivity;
import com.huatec.edu.mobileshop.activity.MainActivity;
import com.huatec.edu.mobileshop.entity.HttpResult;
import com.huatec.edu.mobileshop.http.ProgressDialogSubscriber;
import com.huatec.edu.mobileshop.http.presenter.GoodsPresenter;
import com.huatec.edu.mobileshop.utils.NetworkUtils;
import com.huatec.edu.mobileshop.view.RecyclerItemClickListener;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class CartFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, null);
        ButterKnife.bind(this, view);

        return view;
    }



}
