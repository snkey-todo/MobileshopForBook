package com.mobileshop.mobile.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileshop.mobile.BaseActivity;
import com.mobileshop.mobile.CheckoutActivity;
import com.mobileshop.mobile.LoginActivity;
import com.mobileshop.mobile.MainActivity;
import com.mobileshop.mobile.R;
import com.mobileshop.mobile.adapter.CartListAdapter;
import com.mobileshop.mobile.adapter.ShoppingAdapter;
import com.mobileshop.mobile.info.ShopCarGoodsInfo;
import com.mobileshop.mobile.info.StoryInfo;
import com.mobileshop.mobile.model.CartItem;
import com.mobileshop.mobile.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("NewApi")
public class CartFragment extends Fragment implements Callback {

    // private PullToRefreshListView cartPullRefreshListView;
    private RelativeLayout shopCartBottom;
    private LinearLayout noData;
    private ImageView backIV;
    private ListView cartListView;

    private CartListAdapter cartListAdapter;
    private List<CartItem> cartItemList = new ArrayList<>();

    private Button loginButton;
    private Button forwardIndexBtn;

    private TextView totalTV;

    private RelativeLayout notLoginLayout;

    private CheckBox selectAllCB;
    private TextView editTV;

    private LinearLayout cartShowView;
    private RelativeLayout cartEditView;
    private RelativeLayout cartSelectAllLayout;
    private TextView cartSelectAllTV;

    private RelativeLayout deleteLayout;
    private RelativeLayout favoriteLayout;

    private BaseActivity activity;

    private ProgressDialog progressDialog;

    private TextView checkoutTV;

    private FragmentManager fragmentManager;

    // \\\\\\\\\\\\
    private Context ctx;
    private View view;
    private ExpandableListView list;
    private ArrayList<StoryInfo> groupData = new ArrayList<StoryInfo>();
    private ArrayList<ArrayList<ShopCarGoodsInfo>> childrenData = new ArrayList<ArrayList<ShopCarGoodsInfo>>();
    private ShoppingAdapter adapter = null;
    private String vcard_id = "";
    private TextView checkOutTV, shopTotalPrice;
    private Double order_total = 0.0;
    private ImageView noThingIV;
    private RelativeLayout noThingRL;
    private RelativeLayout buttomRL, bottom;
    private ImageButton message;

    private ArrayList<ArrayList<ShopCarGoodsInfo>> childLists = new ArrayList<ArrayList<ShopCarGoodsInfo>>();
    private ArrayList<StoryInfo> groupLists = new ArrayList<StoryInfo>();
    //
    private ArrayList<ArrayList<String>> childList = new ArrayList<ArrayList<String>>();
    private ArrayList<String> groupList = new ArrayList<String>();
    final Handler handler = new Handler(this);
    private int id, produce_id, num;

    private Double total = 0.00;
    private int count;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (null != view) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.fragment_cart, null);

            findView();
            inits();
            listener();
        }
        return view;

    }

    private void findView() {

        cartShowView = (LinearLayout) view
                .findViewById(R.id.shopping_cart_show_view);
        cartEditView = (RelativeLayout) view
                .findViewById(R.id.shopping_cart_edit_view);
        cartSelectAllLayout = (RelativeLayout) view
                .findViewById(R.id.cart_select_all_layout);
        cartSelectAllTV = (TextView) view
                .findViewById(R.id.shopping_cart_show_all_select);
        backIV = (ImageView) view.findViewById(R.id.title_back);
        deleteLayout = (RelativeLayout) view
                .findViewById(R.id.shopping_cart_edit_to_delete);
        favoriteLayout = (RelativeLayout) view
                .findViewById(R.id.shopping_cart_edit_to_collect);

        shopCartBottom = (RelativeLayout) view
                .findViewById(R.id.shoping_cart_bottom);
        noData = (LinearLayout) view.findViewById(R.id.nodata);

        totalTV = (TextView) view.findViewById(R.id.cart_total);

        notLoginLayout = (RelativeLayout) view
                .findViewById(R.id.shoping_cart_user_no_login);
        selectAllCB = (CheckBox) view.findViewById(R.id.cart_select_all_button);
        // 编辑
        editTV = (TextView) view.findViewById(R.id.shopping_cart_text);
        loginButton = (Button) view.findViewById(R.id.cart_no_login_but);
        // 去首页
        forwardIndexBtn = (Button) view.findViewById(R.id.cart_forward_index);
        // 去结算
        checkoutTV = (TextView) view.findViewById(R.id.cart_checkout);
        list = (ExpandableListView) view.findViewById(R.id.list);
        list.setGroupIndicator(null);
    }

    private void inits() {
        fragmentManager = this.getFragmentManager();

//		 notLoginLayout.setVisibility(activity.isLogin() ? View.GONE
//		 : View.VISIBLE);
//		 new LoadCartTask().execute();

        activity = (BaseActivity) getActivity();
        ctx = activity.getApplicationContext();
        if (!(activity instanceof MainActivity)) {
            backIV.setVisibility(View.VISIBLE);
        } else {
            backIV.setVisibility(View.GONE);
        }
        // shopCartBottom.setVisibility(View.GONE);		//
        // list.setVisibility(View.GONE);				//
        noData.setVisibility(View.GONE);
        // getData();
        new LoadCartTask().execute();

    }

    private void listener() {
        // 后退

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        selectAllCB
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        for (int i = 0; i < cartItemList.size(); i++) {
                            cartItemList.get(i).setChecked(isChecked);
                        }
                        cartListAdapter.notifyDataSetChanged();
                    }
                });

        editTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTV.getText().equals("编辑")) {
                    editTV.setText("长按删除");
                }
            }
        });

        // 删除
        deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<CartItem> checkedItems = cartListAdapter
                        .getCheckedList();
                if (checkedItems.size() == 0) {
                    Toast.makeText(activity, "您还没有选择商品哦！", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                new AlertDialog.Builder(activity)
                        .setTitle("删除购物车")
                        .setMessage("您确认要删除这" + checkedItems.size() + "种商品吗？")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        progressDialog = ProgressDialog.show(
                                                activity, null, "正在删除…");
                                        final Handler handler = new Handler() {
                                            @Override
                                            public void handleMessage(
                                                    Message msg) {
                                                progressDialog.dismiss();
                                                new LoadCartTask().execute();
                                                activity.setCartCount(msg.what);
                                                if (activity instanceof MainActivity) {
                                                    ((MainActivity) activity)
                                                            .updateCartBadge();
                                                }
                                                Toast.makeText(activity,
                                                        "删除购物车成功！",
                                                        Toast.LENGTH_SHORT)
                                                        .show();
                                                super.handleMessage(msg);
                                            }
                                        };

                                        // 发送删除购物车请求
                                        new Thread() {
                                            @Override
                                            public void run() {
                                                for (CartItem cartItem : checkedItems) {
                                                    HttpUtils.getJson("/api/mobile/cart!delete.do?cartid="
                                                            + cartItem.getId());
                                                }

                                                // 删除后获取购物车商品数量
                                                String json = HttpUtils
                                                        .getJson("/api/mobile/cart!count.do");
                                                if ("".equals(json)) {
                                                    handler.sendEmptyMessage(0);
                                                    return;
                                                }
                                                try {
                                                    JSONObject jsonObject = new JSONObject(
                                                            json);
                                                    if (jsonObject == null) {
                                                        handler.sendEmptyMessage(0);
                                                        return;
                                                    }
                                                    if (!jsonObject
                                                            .has("count")) {
                                                        handler.sendEmptyMessage(0);
                                                        return;
                                                    }
                                                    handler.sendEmptyMessage(jsonObject
                                                            .getInt("count"));
                                                } catch (Exception ex) {
                                                    Log.e("delete cart item ",
                                                            ex.getMessage());
                                                }
                                            }
                                        }.start();
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                }).create().show();
            }
        });

        // 移入关注
        favoriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<CartItem> checkedItems = cartListAdapter
                        .getCheckedList();
                if (checkedItems.size() == 0) {
                    Toast.makeText(activity, "您还没有选择商品哦！", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if (!activity.isLogin()) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    Toast.makeText(activity, "未登录或登录已过期，请重新登录！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog = ProgressDialog.show(activity, null, "正在移入关注…");
                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        progressDialog.dismiss();
                        switch (msg.what) {
                            case -1:
                                startActivity(new Intent(getActivity(),
                                        LoginActivity.class));
                                Toast.makeText(activity, "未登录或登录已过期，请重新登录！",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case 0:
                                Toast.makeText(activity, "移入关注失败，请您重试！",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(activity, "移入关注成功！",
                                        Toast.LENGTH_SHORT).show();
                                break;
                        }
                        super.handleMessage(msg);
                    }
                };

                new Thread() {
                    @Override
                    public void run() {
                        for (CartItem cartItem : checkedItems) {
                            String json = HttpUtils
                                    .getJson("/api/mobile/favorite!add.do?id="
                                            + cartItem.getGoods_id());
                            if ("".equals(json)) {
                                handler.sendEmptyMessage(0);
                                return;
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                if (jsonObject == null) {
                                    handler.sendEmptyMessage(0);
                                    return;
                                }

                                int result = jsonObject.getInt("result");
                                if (result == -1) {
                                    handler.sendEmptyMessage(-1);
                                    return;
                                }
                            } catch (Exception ex) {
                                Log.e("AddToCart", ex.getMessage());
                            }
                        }
                        handler.sendEmptyMessage(1);
                    }
                }.start();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, LoginActivity.class));
            }
        });

        // 去首页
        forwardIndexBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MainActivity.class);
                intent.putExtra("action", "toIndex");
                startActivity(intent);
            }
        });

        checkoutTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!activity.isLogin()) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    Toast.makeText(activity, "未登录或登录已过期，请重新登录！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(getActivity(), CheckoutActivity.class));
            }
        });

    }

    /**
     * 购物车商品数量改变
     */
    public void changeNumber() {
        new LoadCartTask().execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        inits();
    }

    // 删除
    private void deleteCargoods() {
        // final List<CartItem> checkedItems = cartListAdapter.getCheckedList();
        // if (checkedItems.size() == 0) {
        // Toast.makeText(activity, "您还没有选择商品哦！", Toast.LENGTH_SHORT).show();
        // return;
        // }
        new AlertDialog.Builder(activity)
                .setTitle("删除购物车")
                .setMessage("您确认要删除这种商品吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog = ProgressDialog.show(activity, null,
                                "正在删除…");
                        final Handler handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                progressDialog.dismiss();
                                new LoadCartTask().execute();
                                activity.setCartCount(msg.what);
                                if (activity instanceof MainActivity) {
                                    ((MainActivity) activity).updateCartBadge();
                                }
                                new LoadCartTask().execute();
                                Toast.makeText(activity, "删除购物车成功！",
                                        Toast.LENGTH_SHORT).show();
                                super.handleMessage(msg);
                            }
                        };

                        // 发送删除购物车请求
                        new Thread() {
                            @Override
                            public void run() {
                                // for (CartItem cartItem : checkedItems) {
                                HttpUtils
                                        .getJson("/api/mobile/cart!delete.do?cartid="
                                                + id);
                                // }

                                // 删除后获取购物车商品数量
                                String json = HttpUtils
                                        .getJson("/api/mobile/cart!count.do");
                                if ("".equals(json)) {
                                    handler.sendEmptyMessage(0);
                                    return;
                                }
                                try {
                                    JSONObject jsonObject = new JSONObject(json);
                                    if (jsonObject == null) {
                                        handler.sendEmptyMessage(0);
                                        return;
                                    }
                                    if (!jsonObject.has("count")) {
                                        handler.sendEmptyMessage(0);
                                        return;
                                    }
                                    handler.sendEmptyMessage(jsonObject
                                            .getInt("count"));
                                } catch (Exception ex) {
                                    Log.e("delete cart item ", ex.getMessage());
                                }
                            }
                        }.start();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    // 数量增加
    private void addcargoodsnum() {
        progressDialog = ProgressDialog.show(activity, null, "正在添加…");
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progressDialog.dismiss();
                switch (msg.what) {
                    case -1:
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        Toast.makeText(activity, "未登录或登录已过期，请重新登录！",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        Toast.makeText(activity, "添加失败，请您重试！", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    default:
                        new LoadCartTask().execute();
                        Toast.makeText(activity, "修改成功！", Toast.LENGTH_SHORT)
                                .show();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        new Thread() {
            @Override
            public void run() {
                String json = HttpUtils
                        .getJson("/api/mobile/cart!updateNum.do?cartid=" + id
                                + "&productid=" + produce_id + "&num=" + num);
                if ("".equals(json)) {
                    handler.sendEmptyMessage(0);
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject == null) {
                        handler.sendEmptyMessage(0);
                        return;
                    }

                    int result = jsonObject.getInt("result");
                    if (result == -1) {
                        handler.sendEmptyMessage(-1);
                        return;
                    }
                } catch (Exception ex) {
                    Log.e("AddToCart", ex.getMessage());
                }
                handler.sendEmptyMessage(1);
            }
        }.start();
    }

    /**
     * 载入购物车商品
     */
    private class LoadCartTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... params) {
            String json = HttpUtils.getJson("/api/mobile/cart!list.do");
            return json;
        }

        @Override
        protected void onPostExecute(String json) {
            try {
                groupLists.clear();
                childLists.clear();
                // list.expandGroup(groupPos)
                JSONObject root = new JSONObject(json);
                JSONArray data = root.getJSONArray("data");
//				total = data.getDouble("");
                total = 0.00;
                for (int i = 0; i < data.length(); i++) {
                    JSONObject obj = data.getJSONObject(i);
                    StoryInfo storyInfo = new StoryInfo();
                    storyInfo.setStore_name(obj.getString("store_name"));
                    storyInfo.setStore_id(obj.getInt("store_id"));
                    JSONObject obj2 = obj.getJSONObject("storeprice");
                    storyInfo.setNeedPayMoney(obj2.getDouble("needPayMoney"));
                    total += obj2.getDouble("needPayMoney");
                    storyInfo.setOrderPrice(obj2.getDouble("orderPrice"));
                    storyInfo.setShippingPrice(obj2.getDouble("shippingPrice"));
                    storyInfo.setWeight(obj2.getDouble("weight"));
                    groupLists.add(storyInfo);
                    JSONArray goodslist = obj.getJSONArray("goodslist");
                    ArrayList<ShopCarGoodsInfo> shopCarGoodsInfos = new ArrayList<ShopCarGoodsInfo>();
                    for (int j = 0; j < goodslist.length(); j++) {
                        JSONObject obj3 = goodslist.getJSONObject(j);
                        ShopCarGoodsInfo shopCarGoodsInfo = new ShopCarGoodsInfo();
                        shopCarGoodsInfo.setCoupPrice(obj3
                                .getDouble("coupPrice"));
                        shopCarGoodsInfo.setGoods_id(obj3.getInt("goods_id"));
                        shopCarGoodsInfo.setId(obj3.getInt("id"));
                        shopCarGoodsInfo.setImage_default(obj3
                                .getString("image_default"));
                        shopCarGoodsInfo
                                .setMktprice(obj3.getDouble("mktprice"));
                        shopCarGoodsInfo.setName(obj3.getString("name"));
                        shopCarGoodsInfo.setNum(obj3.getInt("num"));
                        shopCarGoodsInfo.setPrice(obj3.getDouble("price"));
                        shopCarGoodsInfo.setProduct_id(obj3
                                .getInt("product_id"));
                        shopCarGoodsInfo.setSn(obj3.getString("sn"));
                        shopCarGoodsInfo.setSpecs(obj3.getString("specs"));
                        shopCarGoodsInfo.setStore_id(obj3.getInt("store_id"));
                        shopCarGoodsInfo.setStore_name(obj3
                                .getString("store_name"));
                        shopCarGoodsInfo
                                .setSubtotal(obj3.getDouble("subtotal"));
                        shopCarGoodsInfo.setWeight(obj3.getDouble("weight"));
                        shopCarGoodsInfos.add(shopCarGoodsInfo);
                    }
                    childLists.add(shopCarGoodsInfos);
                }
                adapter = new ShoppingAdapter(ctx, groupLists, childLists,
                        handler);
                list.setAdapter(adapter);
                for (int i = 0; i < groupLists.size(); i++) {
                    list.expandGroup(i);
                }

                // 合计
                totalTV.setText("合计:￥"
                        + String.format("%.2f", total));
            } catch (Exception ex) {
                Log.e("LoadCart", ex.getMessage());
            }
            super.onPostExecute(json);
        }
    }

    @Override
    public boolean handleMessage(Message message) {
        Bundle data = message.getData();
        switch (message.what) {
            case 001:
                id = data.getInt("id");
                deleteCargoods();
                break;
            case 002:
                id = data.getInt("id");
                produce_id = data.getInt("produceid");
                num = data.getInt("num") + 1;
                addcargoodsnum();
                break;
            case 003:
                id = data.getInt("id");
                produce_id = data.getInt("produceid");
                num = data.getInt("num");
                addcargoodsnum();
                break;
        }
        return false;

    }

}
