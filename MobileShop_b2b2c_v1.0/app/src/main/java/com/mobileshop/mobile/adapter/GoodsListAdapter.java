package com.mobileshop.mobile.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobileshop.mobile.Constants;
import com.mobileshop.mobile.R;
import com.mobileshop.mobile.model.Goods;

import java.util.List;

/**
 * Created by Dawei on 3/28/15.
 */
public class GoodsListAdapter extends BaseAdapter {

    private List<Goods> goodsList;
    private Activity context;

    public GoodsListAdapter(Activity context, List<Goods> goodsList) {
        this.context = context;
        this.goodsList = goodsList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return goodsList.get(position);
    }

    @Override
    public int getCount() {
        return goodsList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = context.getLayoutInflater().inflate(R.layout.goods_list_item, null);
        } else {
            view = convertView;
        }
        view.setBackgroundDrawable(view.getResources().getDrawable(R.drawable.goods_list_item_bg));

        ImageView goodsImg = (ImageView)view.findViewById(R.id.goodslist_img);
        TextView goodsName = (TextView)view.findViewById(R.id.goodslist_name);
        TextView goodsPrice = (TextView)view.findViewById(R.id.goodslist_price);
        TextView goodsComments = (TextView)view.findViewById(R.id.goodslist_comments);
        TextView goodsType = (TextView)view.findViewById(R.id.goodslist_goodstype);

        Goods goods = goodsList.get(position);

        Constants.imageLoader.displayImage(goods.getThumbnail(), goodsImg, Constants.displayImageOptions);
        goodsName.setText(goods.getName());
        goodsPrice.setText("￥"+ String.format("%.2f", goods.getPrice()));
        goodsComments.setText("" + goods.getBuy_count() + " 人已购买");

        if(goods.getGoods_type() == null || goods.getGoods_type().equals("")){
            goodsType.setVisibility(View.INVISIBLE);
        }else{
            goodsType.setVisibility(View.VISIBLE);
            goodsType.setText(goods.getGoods_type());
        }

        return view;
    }
}
