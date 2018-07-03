package com.mobileshop.mobile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.mobileshop.mobile.adapter.GoodsListAdapter;
import com.mobileshop.mobile.model.Goods;
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
public class MyFavoriteActivity extends Activity {

    private ProgressDialog progressDialog;

    private ImageView backIV;

    private LinearLayout favoriteEmptyLayout;
    private Button favoriteIndexBtn;

    private PullToRefreshListView pullToRefreshListView;
    private ListView productListLV;
    private List<Goods> goodsList = new ArrayList<>();
    private GoodsListAdapter goodsListAdapter;

    //当前页码
    private int currentPage = 1;

    public MyFavoriteActivity() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfavorite);

        backIV = (ImageView)findViewById(R.id.title_back);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        favoriteEmptyLayout = (LinearLayout)findViewById(R.id.favorite_empty);
        favoriteIndexBtn = (Button)findViewById(R.id.favorite_index_btn);
        favoriteIndexBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyFavoriteActivity.this, MainActivity.class);
                intent.putExtra("action", "toIndex");
                startActivity(intent);
                finish();
            }
        });

        pullToRefreshListView = (PullToRefreshListView)findViewById(R.id.product_list);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
        productListLV = pullToRefreshListView.getRefreshableView();
        goodsListAdapter = new GoodsListAdapter(this, goodsList);
        productListLV.setAdapter(goodsListAdapter);
        productListLV.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("选择操作");
                menu.add(0, 0, 0, "移除收藏");
            }
        });
        productListLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Goods goods = (Goods) parent.getAdapter().getItem(position);
                Intent intent = new Intent(MyFavoriteActivity.this, GoodsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("goods_id", goods.getGoods_id());
                startActivity(intent);
            }
        });

        //加载下一页
        this.pullToRefreshListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                currentPage++;
                new LoadMyFavoriteTask().execute();
            }
        });

        //载入收藏列表
        new LoadMyFavoriteTask().execute();

    }

    //给菜单项添加事件
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case 0:
                remove(menuInfo.position - 1);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * 移除收藏
     * @param position
     */
    private void remove(int position){
        progressDialog = ProgressDialog.show(this, null, "正在移除...");
        final Goods goods = goodsList.get(position);
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progressDialog.dismiss();
                switch (msg.what) {
                    case -1:
                        Toast.makeText(MyFavoriteActivity.this, "移除失败，请您重试！", Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        Toast.makeText(MyFavoriteActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        goodsList.remove(goods);
                        goodsListAdapter.notifyDataSetChanged();
                        if(goodsList.size() == 0){
                            favoriteEmptyLayout.setVisibility(View.VISIBLE);
                            pullToRefreshListView.setVisibility(View.GONE);
                        }else{
                            favoriteEmptyLayout.setVisibility(View.GONE);
                            pullToRefreshListView.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(MyFavoriteActivity.this, "移除收藏成功！", Toast.LENGTH_SHORT).show();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        new Thread() {
            @Override
            public void run() {
                String json = HttpUtils.getJson("/api/mobile/favorite!delete.do?favoriteid=" + goods.getFavorite_id());
                if ("".equals(json)) {
                    handler.sendEmptyMessage(-1);
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject == null) {
                        handler.sendEmptyMessage(-1);
                        return;
                    }

                    Message message = Message.obtain();
                    message.what = jsonObject.getInt("result");
                    message.obj = jsonObject.getString("message");
                    handler.sendMessage(message);
                } catch (Exception ex) {
                    Log.e("Remove Favorite", ex.getMessage());
                }
            }
        }.start();
    }

    private class LoadMyFavoriteTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... params) {
            String json = HttpUtils.getJson("/api/mobile/favorite!list.do?page=" + currentPage);
            return json;
        }

        @Override
        protected void onPostExecute(String json) {
            try {

                JSONObject jsonObject = new JSONObject(json);
                if(!jsonObject.has("result") || jsonObject.getInt("result") != 1){
                    Toast.makeText(MyFavoriteActivity.this, "您没有登录或登录已过期！", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONArray array = jsonObject.getJSONArray("data");
                for(int i = 0; i < array.length(); i++){
                    goodsList.add(Goods.toGoods(array.getJSONObject(i)));
                }

                if(array.length() > 0) {
                    goodsListAdapter.notifyDataSetChanged();
                    pullToRefreshListView.onRefreshComplete();
                }
                if(goodsList.size() == 0){
                    favoriteEmptyLayout.setVisibility(View.VISIBLE);
                    pullToRefreshListView.setVisibility(View.GONE);
                }else{
                    favoriteEmptyLayout.setVisibility(View.GONE);
                    pullToRefreshListView.setVisibility(View.VISIBLE);
                }
            }catch(Exception ex){
                Log.e("Load MyFavorite", ex.getMessage());
            }
            super.onPostExecute(json);
        }
    }

}
