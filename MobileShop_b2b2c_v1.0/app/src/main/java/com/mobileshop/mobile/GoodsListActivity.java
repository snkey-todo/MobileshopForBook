package com.mobileshop.mobile;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobileshop.mobile.adapter.GoodsListAdapter;
import com.mobileshop.mobile.model.Goods;
import com.mobileshop.mobile.utils.HttpUtils;
import com.mobileshop.mobile.utils.StringUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class GoodsListActivity extends BaseActivity {
    private static  final String TAG="GoodsListActivity";
    private PullToRefreshListView pullToRefreshListView;
    private ListView goodsListView;
    private TextView nodataTextView;

    //按销量排序
    private RelativeLayout orderbySalesLayout;
    private TextView orderbySalesText;

    //按评价排序
    private RelativeLayout orderbyGradeLayout;
    private TextView orderbyGradeText;

    //按价格排序
    private RelativeLayout orderbyPriceLayout;
    private TextView orderbyPriceText;
    private ImageView orderbyPriceImg;

    //按新品排序
    private RelativeLayout orderbyNewGoodsLayout;
    private TextView orderbyNewGoodsText;


    //列表以及适配器
    private List<Goods> goodsList = new ArrayList<Goods>();
    private GoodsListAdapter goodsListAdapter = null;

    private int cid;
    private int brand;
    private String name;
    private String keyword;
    private int seckill;

    //当前页码
    private int currentPage = 1;

    //排序
    private String sort = "buynum_desc";    //默认是销量排序

    //搜索
    private TextView searchTV;

    public GoodsListActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_list);

        Intent intent = getIntent();
        this.cid = intent.getIntExtra("cid", 0);
        this.brand = intent.getIntExtra("brand", 0);
        this.name = intent.getStringExtra("name");
        this.keyword = intent.getStringExtra("keyword");
        this.seckill = intent.getIntExtra("seckill", 0);

        //后退
        findViewById(R.id.title_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initViews();

        setOrderbySelection();

        TextView searchKeyWordTv = (TextView)findViewById(R.id.search_keyword);
        if(!StringUtils.isEmpty(name)) {
            searchKeyWordTv.setText(name);
        }
        if(!StringUtils.isEmpty(keyword)){
            searchKeyWordTv.setText(keyword);
        }

        pullToRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
        goodsListView = pullToRefreshListView.getRefreshableView();
        goodsListView.setDivider(null);

        goodsListAdapter = new GoodsListAdapter(this, goodsList);
        goodsListView.setAdapter(goodsListAdapter);

        new LoadGoodsListTask().execute();

        //按销量排序
        orderbySalesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort = "buynum_desc";
                setOrderbySelection();
                new LoadGoodsListTask().execute(cid);
            }
        });

        //按评价排序
        orderbyGradeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort = "grade_desc";
                setOrderbySelection();
                new LoadGoodsListTask().execute(cid);
            }
        });

        //按价格排序
        orderbyPriceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sort.equals("price_asc")) {
                    sort = "price_desc";
                }else{
                    sort = "price_asc";
                }
                setOrderbySelection();
                new LoadGoodsListTask().execute(cid);
            }
        });

        //按新品排序
        orderbyNewGoodsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort = "buynum_asc";
                setOrderbySelection();
                new LoadGoodsListTask().execute(cid);
            }
        });

        //加载下一页
        this.pullToRefreshListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                currentPage++;
                new LoadGoodsListTask().execute(cid);
            }
        });

        //点击商品
        goodsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Goods goods = (Goods)parent.getAdapter().getItem(position);
//                MainActivity mainActivity = (MainActivity)getActivity();
//                mainActivity.showGoods(goods.getGoods_id());
                Intent intent = new Intent(GoodsListActivity.this, GoodsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("goods_id", goods.getGoods_id());
                startActivity(intent);
            }
        });

        searchTV = (TextView)findViewById(R.id.search_keyword);
        searchTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoodsListActivity.this, SearchActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String keyword = data.getStringExtra("keyword");

                Intent intent = new Intent(this, GoodsListActivity.class);
                intent.putExtra("keyword", keyword);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        }
    }

    /**
     * 初始化控件
     */
    private void initViews(){
        nodataTextView = (TextView)findViewById(R.id.goodslist_nodata);

        orderbySalesLayout = (RelativeLayout)findViewById(R.id.goodslist_orderby_sales);
        orderbySalesText = (TextView)findViewById(R.id.goodslist_orderby_sales_text);

        orderbyGradeLayout = (RelativeLayout)findViewById(R.id.goodslist_orderby_grade);
        orderbyGradeText = (TextView)findViewById(R.id.goodslist_orderby_grade_text);

        orderbyPriceLayout = (RelativeLayout)findViewById(R.id.goodslist_orderby_price);
        orderbyPriceText = (TextView)findViewById(R.id.goodslist_orderby_price_text);
        orderbyPriceImg = (ImageView)findViewById(R.id.goodslist_orderby_price_img);

        orderbyNewGoodsLayout = (RelativeLayout)findViewById(R.id.goodslist_orderby_newgoods);
        orderbyNewGoodsText = (TextView)findViewById(R.id.goodslist_orderby_newgoods_text);

        pullToRefreshListView = (PullToRefreshListView)findViewById(R.id.goodslist_listview);
    }

    /**
     * 设置orderby条件的选中项
     */
    private void setOrderbySelection(){
        currentPage = 1;
        goodsList.clear();
        //默认都设置为白色
        orderbySalesText.setTextColor(getResources().getColorStateList(R.color.light));
        orderbyGradeText.setTextColor(getResources().getColorStateList(R.color.light));
        orderbyPriceText.setTextColor(getResources().getColorStateList(R.color.light));
        orderbyPriceImg.setImageResource(R.drawable.sort_button_price_none);
        orderbyNewGoodsText.setTextColor(getResources().getColorStateList(R.color.light));

        //不同的排序条件，界面的变化
        if(sort.equals("buynum_desc")){
            orderbySalesText.setTextColor(getResources().getColorStateList(R.color.red));
        }else if(sort.contains("grade")){
            orderbyGradeText.setTextColor(getResources().getColorStateList(R.color.red));
        }else if(sort.equals("price_desc")){
            orderbyPriceText.setTextColor(getResources().getColorStateList(R.color.red));
            orderbyPriceImg.setImageResource(R.drawable.sort_button_price_down);
        }else if(sort.equals("price_asc")){
            orderbyPriceText.setTextColor(getResources().getColorStateList(R.color.red));
            orderbyPriceImg.setImageResource(R.drawable.sort_button_price_up);
        }else if(sort.equals("buynum_asc")){
            orderbyNewGoodsText.setTextColor(getResources().getColorStateList(R.color.red));
        }

    }

    private class LoadGoodsListTask extends AsyncTask<Integer, Integer, String>{

        @Override
        protected String doInBackground(Integer... params) {
            String param = "";
            if(cid > 0){
                param += "&cat=" + cid;
            }
            if(brand > 0){
                param += "&brand=" + brand;
            }
            if(!StringUtils.isEmpty(keyword)){
                try {
                    param += "&keyword=" + java.net.URLEncoder.encode(keyword, "utf-8");
                }catch(UnsupportedEncodingException ex){

                }
            }
            if(seckill > 0){
                param += "&seckill=1";
            }
            String json = HttpUtils.getJson("/api/mobile/goods!listgoods.do?sort="
                    + sort + "&page=" + currentPage + param);
            Log.i(TAG,"GOODS:-->"+json);
            return json;
        }

        @Override
        protected void onPostExecute(String json) {
            try {
                JSONArray array = new JSONObject(json)
                        .getJSONArray("data");
                for(int i = 0; i < array.length(); i++){
                    JSONObject jsonObject = array.getJSONObject(i);
                    goodsList.add(Goods.toGoods(jsonObject));
                }

                if(goodsList.size() > 0) {
                    goodsListAdapter.notifyDataSetChanged();
                    pullToRefreshListView.onRefreshComplete();
                }else{
                    nodataTextView.setVisibility(View.VISIBLE);
                }
            }catch(Exception ex){
                Log.e("loadCategories", ex.getMessage());
            }
            super.onPostExecute(json);
        }
    }


}
