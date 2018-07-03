package com.mobileshop.mobile.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobileshop.mobile.Constants;
import com.mobileshop.mobile.R;
import com.mobileshop.mobile.model.Order;
import com.mobileshop.mobile.model.OrderItem;

import java.util.List;

/**
 * Created by Dawei on 5/21/15.
 */
public class MyOrderAdapter extends BaseAdapter {

    private List<Order> orderList;
    private Activity context;

    public MyOrderAdapter(Activity context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = context.getLayoutInflater().inflate(R.layout.activity_myorder_item, null);
        } else {
            view = convertView;
        }

//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("惦记了");
//            }
//        });

        Order order = (Order)getItem(position);

        //已完成的戳
        ImageView finishIV = (ImageView)view.findViewById(R.id.order_item_finish_singet_iv);
        finishIV.setVisibility(order.getStatus().intValue() == 7 ? View.VISIBLE : View.GONE);

        TextView orderStatussTV = (TextView)view.findViewById(R.id.order_item_order_status_tv);
        orderStatussTV.setText(order.getOrderStatus());

        TextView snTV = (TextView)view.findViewById(R.id.order_item_order_number_tv);
        snTV.setText(order.getSn());

        TextView paymentTV = (TextView)view.findViewById(R.id.order_item_order_payment_tv);
        paymentTV.setText("实付款:" + String.format("%.2f", order.getNeed_pay_money()));


        //载入商品
        LinearLayout goodsContainerLayout = (LinearLayout)view.findViewById(R.id.goods_container);
        goodsContainerLayout.removeAllViewsInLayout();

        for(OrderItem orderItem : order.getItemList()){
            RelativeLayout goodsLayout = (RelativeLayout) context.getLayoutInflater().inflate(R.layout.activity_checkout_goods_item, null);

            ImageView imageIV = (ImageView)goodsLayout.findViewById(R.id.product_image);
            Constants.imageLoader.displayImage(orderItem.getImage(), imageIV, Constants.displayImageOptions);

            TextView nameTV = (TextView)goodsLayout.findViewById(R.id.product_name);
            nameTV.setText(orderItem.getName());

            TextView numberTV = (TextView)goodsLayout.findViewById(R.id.product_num);
            numberTV.setText("x" + orderItem.getNum());

            TextView priceTV = (TextView)goodsLayout.findViewById(R.id.product_price);
            priceTV.setText("￥" + String.format("%.2f", orderItem.getPrice()));

            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            goodsContainerLayout.addView(goodsLayout, layoutParams);
        }


        return view;
    }

}
