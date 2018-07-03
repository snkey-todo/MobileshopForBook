package com.mobileshop.mobile.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileshop.mobile.Constants;
import com.mobileshop.mobile.R;
import com.mobileshop.mobile.fragment.CartFragment;
import com.mobileshop.mobile.model.CartItem;
import com.mobileshop.mobile.utils.HttpUtils;
import com.mobileshop.mobile.utils.StringUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dawei on 4/20/15.
 */
public class CartListAdapter extends BaseAdapter {

    private List<CartItem> cartItemList;
    private Activity context;
    private CartFragment cartFragment;
    public boolean showSelect = false;

    public CartListAdapter(CartFragment cartFragment, List<CartItem> cartItemList) {
        this.cartFragment = cartFragment;
        this.context = cartFragment.getActivity();
        this.cartItemList = cartItemList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return cartItemList.get(position);
    }

    @Override
    public int getCount() {
        return cartItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = context.getLayoutInflater().inflate(R.layout.fragment_cart_list_item, null);
        } else {
            view = convertView;
        }

        final CartItem cartItem = (CartItem)getItem(position);

        TextView cartNameTV = (TextView)view.findViewById(R.id.cart_name);
        cartNameTV.setText(cartItem.getName());



        TextView cartPriceTV = (TextView)view.findViewById(R.id.cart_price);
        cartPriceTV.setText("￥" + String.format("%.2f", cartItem.getCoupPrice()));

        ImageView cartImageIV = (ImageView)view.findViewById(R.id.cart_image);
        Constants.imageLoader.displayImage(cartItem.getImage_default(), cartImageIV, Constants.displayImageOptions);

        TextView subtotalTV = (TextView)view.findViewById(R.id.cart_subtotal);
        subtotalTV.setText("小计：￥" + String.format("%.2f", cartItem.getSubtotal()));

        if(!StringUtils.isEmpty(cartItem.getSpecs())) {
            TextView specTV = (TextView) view.findViewById(R.id.cart_spec);
            specTV.setText("规格：" + cartItem.getSpecs());
        }

        CheckBox checkBox = (CheckBox)view.findViewById(R.id.cart_cb);
        checkBox.setChecked(cartItem.isChecked());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cartItem.setChecked(isChecked);
            }
        });

        //商品数量
        final TextView productNumberTV = (TextView)view.findViewById(R.id.cart_single_product_et_num);
        productNumberTV.setText("" + cartItem.getNum());

        //更改数量
        ImageButton reduceIB = (ImageButton)view.findViewById(R.id.cart_single_product_num_reduce);
        reduceIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNumber(cartItem, productNumberTV, -1);
            }
        });

        ImageButton addIB = (ImageButton)view.findViewById(R.id.cart_single_product_num_add);
        addIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNumber(cartItem, productNumberTV, 1);
            }
        });

        //是否显示选择框
        RelativeLayout selectLayout = (RelativeLayout)view.findViewById(R.id.cart_single_product_select);
        if(showSelect){
            selectLayout.setVisibility(View.VISIBLE);
        }else{
            selectLayout.setVisibility(View.GONE);
        }

        return view;
    }

    private void updateNumber(final CartItem cartItem, final TextView productNumberTV, final int addNumber){
        final int currentNumber = Integer.parseInt(productNumberTV.getText().toString());
        if(currentNumber + addNumber < 1)
            return;

        final ProgressDialog progressDialog = ProgressDialog.show(context, null, "正在更新…");
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progressDialog.dismiss();
                switch (msg.what){
                    case 0:
                        if(msg.obj == null){
                            Toast.makeText(context, "更新商品数量失败！", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1:
                        cartFragment.changeNumber();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        new Thread() {
            @Override
            public void run() {
                String json = HttpUtils.getJson("/api/mobile/cart!updateNumApp.do?cartid=" + cartItem.getId() + "&num=" + (currentNumber + addNumber) + "&productid=" + cartItem.getProduct_id());
                if("".equals(json)){
                    handler.sendEmptyMessage(0);
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if(jsonObject == null){
                        handler.sendEmptyMessage(0);
                        return;
                    }

                    Message msg = Message.obtain();
                    msg.what = jsonObject.getInt("result");
                    msg.obj = jsonObject.getString("message");
                    handler.sendMessage(msg);

                }catch(Exception ex){
                    Log.e("AddToCart", ex.getMessage());
                }
            }
        }.start();
    }

    /**
     * 获取选中的购物车项列表
     * @return
     */
    public List<CartItem> getCheckedList(){
        List<CartItem> checkedCartItemList = new ArrayList<>();
        for(CartItem cartItem : cartItemList){
            if(cartItem.isChecked())
                checkedCartItemList.add(cartItem);
        }
        return checkedCartItemList;
    }

}
