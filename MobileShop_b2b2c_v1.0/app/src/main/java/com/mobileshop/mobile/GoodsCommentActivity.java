package com.mobileshop.mobile;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mobileshop.mobile.adapter.GoodsCommentAdapter;
import com.mobileshop.mobile.model.Comment;
import com.mobileshop.mobile.utils.HttpUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoodsCommentActivity extends Activity {

    //商品id
    private int goods_id = 0;

    private PullToRefreshListView pullToRefreshListView;
    private ListView commentListView;
    private TextView nodata;

    private LinearLayout goodsComment;
    private LinearLayout goodsAsk;

    private ImageView back;

    //当前页码
    private int currentPage = 1;
    private List<Comment> commentList = new ArrayList<>();
    private GoodsCommentAdapter goodsCommentAdapter = null;

    private int type = 1;

    public GoodsCommentActivity() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_comment);

        Intent intent = getIntent();
        this.goods_id = intent.getIntExtra("goods_id", 0);

        back = (ImageView)findViewById(R.id.title_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        goodsComment = (LinearLayout)findViewById(R.id.goods_comment);
        goodsAsk = (LinearLayout)findViewById(R.id.goods_ask);
        goodsComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1;
                currentPage = 1;
                commentList.clear();
                new LoadCommentsTask().execute();
                goodsComment.setSelected(true);
                goodsAsk.setSelected(false);
            }
        });
        goodsAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 2;
                currentPage = 1;
                commentList.clear();
                new LoadCommentsTask().execute();
                goodsComment.setSelected(false);
                goodsAsk.setSelected(true);
            }
        });
        goodsComment.setSelected(true);

        pullToRefreshListView = (PullToRefreshListView)findViewById(R.id.goods_comment_list_view);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);

        nodata = (TextView)findViewById(R.id.goods_comment_nodata);

        commentListView = pullToRefreshListView.getRefreshableView();
        commentListView.setDivider(null);

        goodsCommentAdapter = new GoodsCommentAdapter(this, commentList);
        commentListView.setAdapter(goodsCommentAdapter);

        //加载下一页
        this.pullToRefreshListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener()
        {
            public void onLastItemVisible()
            {
                currentPage++;
                new LoadCommentsTask().execute();
            }
        });

        new LoadCommentsTask().execute();
    }

    /**
     * 载入评论
     */
    private class LoadCommentsTask extends AsyncTask<Integer, Integer, JSONArray> {

        @Override
        protected JSONArray doInBackground(Integer... params) {
            String json = HttpUtils.getJson("/api/mobile/goods!comment.do?id=" + goods_id
                    + "&type=" + type + "&page=" + currentPage);
            JSONArray array = null;
            try {
                array = new JSONObject(json).getJSONArray("data");
            }catch(Exception ex){
                Log.e("loadComments", ex.getMessage());
            }
            return array;
        }

        @Override
        protected void onPostExecute(JSONArray array) {
            try{
                for(int i = 0; i < array.length(); i++){
                    commentList.add(Comment.toComment(array.getJSONObject(i)));
                }
            }catch(JSONException ex){}
            GoodsCommentActivity.this.goodsCommentAdapter.notifyDataSetChanged();
            GoodsCommentActivity.this.pullToRefreshListView.onRefreshComplete();
            if(commentList.size() <= 0) {
                GoodsCommentActivity.this.nodata.setText(type == 1 ? "抱歉，暂时没有评论内容" : "抱歉，暂时没有咨询内容");
                GoodsCommentActivity.this.nodata.setVisibility(View.VISIBLE);
            }
        }
    }


}
