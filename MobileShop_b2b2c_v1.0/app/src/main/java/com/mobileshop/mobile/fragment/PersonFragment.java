package com.mobileshop.mobile.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileshop.mobile.AddressActivity;
import com.mobileshop.mobile.Constants;
import com.mobileshop.mobile.LoginActivity;
import com.mobileshop.mobile.MainActivity;
import com.mobileshop.mobile.MyAccountActivity;
import com.mobileshop.mobile.MyFavoriteActivity;
import com.mobileshop.mobile.MyOrderActivity;
import com.mobileshop.mobile.R;
import com.mobileshop.mobile.utils.HttpUtils;
import com.mobileshop.mobile.utils.StringUtils;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonFragment extends Fragment {

    private final int MY_FAVORITE = 1;
    private final int MY_ORDER = 2;
    private final int MY_ADDRESS = 3;
    private final int MY_ACCOUNT_BEFORE = 4;
    private final int MY_ACCOUNT_AFTER = 5;


    private MainActivity mainActivity;
    private ProgressDialog progressDialog;

    private Button loginBtn;

    private RelativeLayout loginedLayout;
    private RelativeLayout unLoginLayout;

    private TextView usernameTv;
    private TextView levelTv;
    private ImageView faceIv;

    private RelativeLayout myOrderLayout;
    private RelativeLayout myFavoriteLayout;
    private RelativeLayout myAddressLayout;
    private RelativeLayout myAccountLayout;
    private RelativeLayout logoutLayout;

    public PersonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        mainActivity = (MainActivity)this.getActivity();

        loginBtn = (Button)view.findViewById(R.id.personal_login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonFragment.this.getActivity(), LoginActivity.class));
            }
        });

        loginedLayout = (RelativeLayout)view.findViewById(R.id.personal_for_login);
        unLoginLayout = (RelativeLayout)view.findViewById(R.id.personal_for_not_login);
        usernameTv = (TextView)view.findViewById(R.id.user_name);
        levelTv = (TextView)view.findViewById(R.id.user_level);
        faceIv = (ImageView)view.findViewById(R.id.user_img_view);


        //我的订单
        myOrderLayout = (RelativeLayout)view.findViewById(R.id.person_my_order);
        myOrderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mainActivity.isLogin()) {
                    startActivity(new Intent(mainActivity, MyOrderActivity.class));
                    return;
                }
                Intent intent = new Intent(mainActivity, LoginActivity.class);
                startActivityForResult(intent, MY_ORDER);
            }
        });

        //我的收藏
        myFavoriteLayout = (RelativeLayout)view.findViewById(R.id.my_collect);
        myFavoriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mainActivity.isLogin()) {
                    startActivity(new Intent(mainActivity, MyFavoriteActivity.class));
                    return;
                }
                Intent intent = new Intent(mainActivity, LoginActivity.class);
                startActivityForResult(intent, MY_FAVORITE);
            }
        });

        //我的收货地址
        myAddressLayout = (RelativeLayout)view.findViewById(R.id.my_address);
        myAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mainActivity.isLogin()) {
                    Intent intent = new Intent(mainActivity, AddressActivity.class);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                    return;
                }
                Intent intent = new Intent(mainActivity, LoginActivity.class);
                startActivityForResult(intent, MY_ADDRESS);
            }
        });

        //修改密码
        myAccountLayout = (RelativeLayout)view.findViewById(R.id.my_account);
        myAccountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mainActivity.isLogin()) {
                    Intent intent = new Intent(mainActivity, MyAccountActivity.class);
                    startActivityForResult(intent, MY_ACCOUNT_AFTER);
                    return;
                }
                Intent intent = new Intent(mainActivity, LoginActivity.class);
                startActivityForResult(intent, MY_ACCOUNT_BEFORE);
            }
        });

        //退出登录
        logoutLayout = (RelativeLayout)view.findViewById(R.id.person_logout_layout);
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mainActivity).setTitle("退出登录").setMessage("您确认要退出登录吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
            }
        });

        init();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case MY_ORDER:
                if(resultCode == Activity.RESULT_OK && data.getBooleanExtra("logined", false)){
                    startActivity(new Intent(mainActivity, MyOrderActivity.class));
                }
                break;
            case MY_FAVORITE:
                if(resultCode == Activity.RESULT_OK && data.getBooleanExtra("logined", false)){
                    startActivity(new Intent(mainActivity, MyFavoriteActivity.class));
                }
                break;
            case MY_ADDRESS:
                if(resultCode == Activity.RESULT_OK && data.getBooleanExtra("logined", false)){
                    Intent intent = new Intent(mainActivity, AddressActivity.class);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                }
                break;
            case MY_ACCOUNT_BEFORE:
                if(resultCode == Activity.RESULT_OK && data.getBooleanExtra("logined", false)){
                    Intent intent = new Intent(mainActivity, MyAccountActivity.class);
                    startActivityForResult(intent, MY_ACCOUNT_AFTER);
                }
                break;
            case MY_ACCOUNT_AFTER:
                if(resultCode == Activity.RESULT_OK){
                    startActivity(new Intent(mainActivity, LoginActivity.class));
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 初始化
     */
    private void init(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", 0);
        String username = sharedPreferences.getString("username", "");
        if(StringUtils.isEmpty(username)){
            loginedLayout.setVisibility(View.GONE);
            unLoginLayout.setVisibility(View.VISIBLE);
            logoutLayout.setVisibility(View.GONE);
        }else{
            loginedLayout.setVisibility(View.VISIBLE);
            logoutLayout.setVisibility(View.VISIBLE);
            unLoginLayout.setVisibility(View.GONE);
            usernameTv.setText(username);
            levelTv.setText(sharedPreferences.getString("level", ""));

            String face = sharedPreferences.getString("face", "");
            if(!StringUtils.isEmpty(face)){
                Constants.imageLoader.displayImage(face, faceIv, Constants.displayImageOptions);
            }
        }
    }

    /**
     * 退出登录
     */
    private void logout(){
        progressDialog = ProgressDialog.show(mainActivity, null, "退出登录中…");
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progressDialog.dismiss();
                switch (msg.what){
                    case 1:
                        SharedPreferences.Editor localEditor = mainActivity.getSharedPreferences("user", 0).edit();
                        localEditor.remove("username");
                        localEditor.remove("face");
                        localEditor.remove("level");
                        localEditor.commit();
                        init();
                        Toast.makeText(mainActivity, "退出登录成功！", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(mainActivity, "退出登录失败！", Toast.LENGTH_SHORT).show();
                        break;

                }
                super.handleMessage(msg);
            }
        };

        new Thread() {
            @Override
            public void run() {
                String json = HttpUtils.getJson("/api/mobile/member!logout.do");
                if("".equals(json)){
                    handler.sendEmptyMessage(-1);
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if(jsonObject == null){
                        handler.sendEmptyMessage(-1);
                        return;
                    }

                    handler.sendEmptyMessage(jsonObject.getInt("result"));
                    return;
                }catch(Exception ex){
                    Log.e("Logout", ex.getMessage());
                }
                handler.sendEmptyMessage(-1);
            }
        }.start();
    }

    @Override
    public void onResume() {
        init();
        super.onResume();
    }
}
