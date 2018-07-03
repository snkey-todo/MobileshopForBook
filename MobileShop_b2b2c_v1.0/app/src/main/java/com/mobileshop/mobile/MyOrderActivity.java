package com.mobileshop.mobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.mobileshop.mobile.adapter.MyOrderAdapter;
import com.mobileshop.mobile.model.Order;
import com.mobileshop.mobile.utils.HttpUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dawei on 5/21/15.
 */
public class MyOrderActivity extends BaseActivity {

    private ImageView backIV;

    private PullToRefreshListView pullToRefreshListView;
    private ListView myOrderLV;
    private MyOrderAdapter myOrderAdapter;
    private List<Order> orderList = new ArrayList<>();

    //当前页码
    private int currentPage = 1;

    public MyOrderActivity() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorder);

        backIV = (ImageView)findViewById(R.id.title_back);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pullToRefreshListView = (PullToRefreshListView)findViewById(R.id.listview_myorder);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
        myOrderLV = pullToRefreshListView.getRefreshableView();
        myOrderLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order order = (Order) parent.getAdapter().getItem(position);
                Intent intent = new Intent(MyOrderActivity.this, MyOrderDetailActivity.class);
                intent.putExtra("orderid", order.getOrder_id());
                startActivityForResult(intent, 1);
            }
        });
        myOrderAdapter = new MyOrderAdapter(this, orderList);
        myOrderLV.setAdapter(myOrderAdapter);

        //加载下一页
        this.pullToRefreshListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                currentPage++;
                new LoadMyOrderTask().execute();
            }
        });

        //载入订单列表
        new LoadMyOrderTask().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 1) {
            currentPage = 1;
            orderList.clear();
            new LoadMyOrderTask().execute();
        }
    }

    private class LoadMyOrderTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... params) {
            String json = HttpUtils.getJson("/api/mobile/order!listapp.do?page=" + currentPage);
            return json;
        }

        @Override
        protected void onPostExecute(String json) {
            try {

                JSONObject jsonObject = new JSONObject(json);
                if(!jsonObject.has("result") || jsonObject.getInt("result") != 1){
                    Toast.makeText(MyOrderActivity.this, "您没有登录或登录已过期！", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONArray array = jsonObject.getJSONArray("data");
                for(int i = 0; i < array.length(); i++){
                    JSONObject orderObject = array.getJSONObject(i);
                    Order order = Order.toOrder(orderObject);
                    if(order == null)
                        continue;
                    orderList.add(order);
                }

                if(array.length() > 0) {
                    myOrderAdapter.notifyDataSetChanged();
                    pullToRefreshListView.onRefreshComplete();
                }
            }catch(Exception ex){
                Log.e("Load MyOrders", ex.getMessage());
            }
            super.onPostExecute(json);
        }
    }

}
