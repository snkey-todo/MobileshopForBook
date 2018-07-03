package com.mobileshop.mobile.fragment;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobileshop.mobile.Constants;
import com.mobileshop.mobile.GoodsActivity;
import com.mobileshop.mobile.R;
import com.mobileshop.mobile.model.Product;
import com.mobileshop.mobile.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoodsSpecFragment extends Fragment implements View.OnClickListener{

    private Product product = null;
    private HashMap<String, Product> productMap = null;
    private HashMap<Integer, Integer> selectedMap = new HashMap<>();

    private TextView goodsSn;
    private ImageView goodsImage;
    private TextView goodsPrice;

    private LinearLayout specItemLayout;
    private LayoutInflater inflater;
    private ViewGroup container;

    private ImageButton countPlus;
    private ImageButton countMinus;
    private EditText goodsCount;

    private GoodsActivity goodsActivity;

    private TextView addToCartTv;


    public GoodsSpecFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;

        View view = inflater.inflate(R.layout.fragment_goods_spec, container, false);

        //sn
        goodsSn = (TextView)view.findViewById(R.id.goods_sn);
        goodsSn.setText(product.getSn());

        //image
        goodsImage = (ImageView)view.findViewById(R.id.goods_image);
        Constants.imageLoader.displayImage(product.getThumbnail(), goodsImage, Constants.displayImageOptions);

        //price
        goodsPrice = (TextView)view.findViewById(R.id.goods_price);
        goodsPrice.setText("￥" + String.format("%.2f", product.getPrice()));

        //数量
        countPlus = (ImageButton)view.findViewById(R.id.countPlus);
        countMinus = (ImageButton)view.findViewById(R.id.countMinus);
        goodsCount = (EditText)view.findViewById(R.id.goods_count);

        countPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodsCount.setText(""+(Integer.parseInt(goodsCount.getText().toString()) + 1));
                changeSpec();
            }
        });
        countMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(goodsCount.getText().toString());
                if(count > 1){
                    goodsCount.setText(""+(count - 1));
                }else{
                    goodsCount.setText("1");
                }
                changeSpec();
            }
        });

        //添加到购物车
        addToCartTv = (TextView)view.findViewById(R.id.add_2_cart);
        addToCartTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodsActivity.addToCart();
            }
        });

        specItemLayout = (LinearLayout)view.findViewById(R.id.goods_spec_list_layout);

        new SpecTask().execute();
        return view;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setGoodsActivity(GoodsActivity goodsActivity) {
        this.goodsActivity = goodsActivity;
    }

    @Override
    public void onClick(View v) {
        //取消所有选中项
        LinearLayout layout = (LinearLayout)v.getParent();
        for(int i = 0; i < layout.getChildCount(); i++){
            TextView textView = (TextView)layout.getChildAt(i);
            if(textView.isSelected()) {
                textView.setSelected(false);
                selectedMap.remove(textView.getTag());
            }
        }

        //选中当前项
        TextView specTextView = (TextView)v;
        specTextView.setSelected(true);

        Integer key = (Integer)layout.getTag();
        Integer value = (Integer)specTextView.getTag();
        selectedMap.put(key, value);
        changeSpec();
    }

    /**
     * 选择spec
     */
    private void changeSpec(){
        Integer[] specsvIds = new Integer[selectedMap.size()];
        selectedMap.values().toArray(specsvIds);
        Arrays.sort(specsvIds);
        String key = TextUtils.join("_", specsvIds);
        if(productMap.containsKey(key)){
            product = productMap.get(key);
            goodsPrice.setText("￥" + String.format("%.2f", product.getPrice()));
            goodsSn.setText(product.getSn());
        }
        //更新上级Fragment信息
        goodsActivity.initProduct(product, Integer.parseInt(goodsCount.getText().toString()));
    }

    /**
     * 载入商品规格
     */
    private class SpecTask extends AsyncTask<Integer, Integer, JSONObject> {
        @Override
        protected JSONObject doInBackground(Integer... params) {
            String json = HttpUtils.getJson("/api/mobile/goods!spec.do?id=" + product.getGoods_id());
            try {
                JSONObject object = new JSONObject(json).getJSONObject("data");
                return object;
            }catch(Exception ex){
                Log.e("loadCategories", ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject data){
            if(data != null) {
                try {
                    productMap = new HashMap<String, Product>();
                    if(data.getInt("have_spec") == 1){
                        JSONArray productList = data.getJSONArray("productList");
                        for(int i = 0; i < productList.length(); i++){
                            Product product = Product.toProduct(productList.getJSONObject(i));
                            if(product != null) {
                                Integer[] specsvIds = product.getSpecsvIds();
                                Arrays.sort(specsvIds);
                                productMap.put(TextUtils.join("_", specsvIds), product);
                            }
                        }

                        JSONArray specArray = data.getJSONArray("specList");
                        for(int i = 0; i < specArray.length(); i++) {
                            View itemView = inflater.inflate(R.layout.fragment_goods_spec_item, container, false);
                            TextView specName = (TextView)itemView.findViewById(R.id.spec_name);
                            specName.setText(specArray.getJSONObject(i).getString("spec_name"));

                            LinearLayout specValueLayout = (LinearLayout)itemView.findViewById(R.id.spec_value_layout);
                            specValueLayout.setTag(specArray.getJSONObject(i).getInt("spec_id"));
                            JSONArray specValueArray = specArray.getJSONObject(i).getJSONArray("valueList");
                            for(int j = 0; j < specValueArray.length(); j++) {
                                TextView textView = new TextView(itemView.getContext());
                                textView.setTextSize(13.0f);
                                textView.setTextColor(getResources().getColor(R.color.pd_black_68));
                                textView.setEllipsize(TextUtils.TruncateAt.END);
                                textView.setGravity(Gravity.CENTER);
                                textView.setBackgroundResource(R.drawable.pd_style_c_bg);
                                textView.setPadding(15, 5, 15, 5);
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                layoutParams.setMargins(10,5,10,5);
                                textView.setLayoutParams(layoutParams);
                                textView.setMinWidth(64);
                                textView.setSingleLine(true);
                                textView.setText(specValueArray.getJSONObject(j).getString("spec_value"));
                                textView.setTag(specValueArray.getJSONObject(j).getInt("spec_value_id"));
                                textView.setOnClickListener(GoodsSpecFragment.this);

                                //设置默认选中项
                                if(j == 0){
                                    textView.setSelected(true);
                                    selectedMap.put(specArray.getJSONObject(i).getInt("spec_id"), specValueArray.getJSONObject(j).getInt("spec_value_id"));
                                }
                                specValueLayout.addView(textView, j);
                            }
                            specItemLayout.addView(itemView, i);
                        }
                        changeSpec();
                    }else{

                    }
                }catch(JSONException ex){
                    ex.printStackTrace();
                }
            }
            super.onPostExecute(data);
        }
    }
}
