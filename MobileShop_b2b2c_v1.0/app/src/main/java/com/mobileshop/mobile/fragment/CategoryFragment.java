package com.mobileshop.mobile.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobileshop.mobile.GoodsListActivity;
import com.mobileshop.mobile.MainActivity;
import com.mobileshop.mobile.R;
import com.mobileshop.mobile.SearchActivity;
import com.mobileshop.mobile.adapter.CategoryLeftListAdapter;
import com.mobileshop.mobile.adapter.CategoryRightListAdapter;
import com.mobileshop.mobile.model.Category;
import com.mobileshop.mobile.utils.AndroidUtils;
import com.mobileshop.mobile.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    private static final String TAG = "CategoryFragment";
    private MainActivity mainActivity;

    private ListView leftListView;
    private ListView rightListView;

    private List<Category> leftList = new ArrayList<>();
    private List<Category[]> rightList = new ArrayList<>();

    private CategoryLeftListAdapter leftListAdapter;
    private CategoryRightListAdapter rightListAdapter;

    private TextView searchTV;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        mainActivity = (MainActivity) getActivity();

        //调整搜索栏布局
        view.findViewById(R.id.title_back).setVisibility(View.GONE);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, AndroidUtils.dp2px(this.getActivity(), 30));
        layoutParams.setMargins(10, AndroidUtils.dp2px(this.getActivity(), 3), 10, 0);
        view.findViewById(R.id.search_layout).setLayoutParams(layoutParams);

        leftListView = (ListView) view.findViewById(R.id.left_list);
        rightListView = (ListView) view.findViewById(R.id.right_list);

        leftListAdapter = new CategoryLeftListAdapter(this.getActivity(), leftList);
        leftListView.setAdapter(leftListAdapter);

        rightListAdapter = new CategoryRightListAdapter(this.getActivity(), rightList, this);
        rightListView.setAdapter(rightListAdapter);

        loadCategories();

        leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //view就是item,parent就是listview
                Category category = (Category) parent.getAdapter().getItem(position);

                leftListAdapter.setSelection(category.getCat_id());
                //滚动到最上面
                leftListView.smoothScrollBy(view.getTop(), 700);
                loadRightList(category);
            }
        });

        searchTV = (TextView) view.findViewById(R.id.search_keyword);
        searchTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, SearchActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String keyword = data.getStringExtra("keyword");
                Intent intent = new Intent(this.getActivity(), GoodsListActivity.class);
                intent.putExtra("keyword", keyword);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        }
    }

    /**
     * 载入分类
     */
    private void loadCategories() {
        final Handler handler = new Handler() {
            @Override
            // 当有消息发送出来的时候就执行Handler的这个方法
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (leftList.size() > 0) {
                    //默认加载第一个left item
                    Category category = leftList.get(0);
                    leftListAdapter.setSelection(category.getCat_id());
                    loadRightList(category);
                }
            }
        };

        //从api加载分类数据
        new Thread() {
            @Override
            public void run() {
                String json = HttpUtils.getJson("/api/mobile/goodscat!list.do");
                Log.i(TAG, "URL" + json);
                try {
                    JSONArray array = new JSONObject(json).getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        Category category = Category.toCategory(jsonObject);
                        leftList.add(category);
                    }
                } catch (Exception ex) {
                    Log.e("loadCategories", ex.getMessage());
                }
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    /**
     * 载入右侧分类列表
     *
     * @param category
     */
    private void loadRightList(Category category) {
        rightList.clear();
        for (Category cat2 : category.getChildren()) {

            rightList.add(new Category[]{cat2});
            //Log.i(TAG,"SIZE:"+cat2.toString());

            for (int i = 0; i < cat2.getChildren().size(); i++) {
                //Log.i(TAG,"SIZE:"+cat2.getChildren().size());
                if (i % 3 == 0) {
                    Category[] categories = new Category[3];
                    categories[0] = cat2.getChildren().get(i);
                    // categories[0].setImage(HttpUtils.replaceBaseUrl(getActivity(),categories[0].getImage()));
                    if (i + 1 < cat2.getChildren().size()) {
                        categories[1] = cat2.getChildren().get(i + 1);
                    }
                    if (i + 2 < cat2.getChildren().size()) {
                        categories[2] = cat2.getChildren().get(i + 2);
                    }
                    rightList.add(categories);
                }
            }
        }
        rightListAdapter.notifyDataSetChanged();
    }

    /**
     * 显示商品列表
     *
     * @param category
     */
    public void go2GoodsList(Category category) {
        Intent intent = new Intent(this.getActivity(), GoodsListActivity.class);
        intent.putExtra("cid", category.getCat_id());
        intent.putExtra("name", category.getName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
