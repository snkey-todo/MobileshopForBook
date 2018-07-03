package com.mobileshop.mobile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.mobileshop.mobile.adapter.SearchHistoryAdapter;
import com.mobileshop.mobile.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dawei on 5/20/15.
 */
public class SearchActivity extends Activity{

    private ListView historyLV;
    private List<String> historyList = new ArrayList<>();
    private SearchHistoryAdapter searchHistoryAdapter;

    private LinearLayout searchHistoryTitleLayout;

    private ImageView backIV;
    private AutoCompleteTextView keywordTV;
    private Button searchBtn;
    private Button delHistoryBtn;

    public SearchActivity() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        final SharedPreferences sharedPreferences = getSharedPreferences("user", 0);

        searchHistoryTitleLayout = (LinearLayout)findViewById(R.id.search_history_title);
        delHistoryBtn = (Button)findViewById(R.id.del_history_button);
        delHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int historySize = sharedPreferences.getInt("history_size", 0);
                SharedPreferences.Editor localEditor = sharedPreferences.edit();
                for(int i = 0; i < historySize; i++) {
                    localEditor.remove("history_" + i);
                }
                localEditor.putInt("history_size", 0);
                localEditor.commit();
                historyList.clear();
                searchHistoryAdapter.notifyDataSetChanged();
                Toast.makeText(SearchActivity.this, "删除历史记录成功！", Toast.LENGTH_SHORT).show();
            }
        });

        //载入历史记录
        historyLV = (ListView)findViewById(R.id.search_history_list);
        historyLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String keyword = (String) parent.getAdapter().getItem(position);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("keyword", keyword);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
        int historySize = sharedPreferences.getInt("history_size", 0);
        for(int i = 0; i < historySize; i++) {
            historyList.add(sharedPreferences.getString("history_" + i, ""));
        }
        if(historyList.size() > 0){
            searchHistoryAdapter = new SearchHistoryAdapter(this, historyList);
            historyLV.setAdapter(searchHistoryAdapter);
            searchHistoryTitleLayout.setVisibility(View.VISIBLE);
            delHistoryBtn.setVisibility(View.VISIBLE);
        }else{
            searchHistoryTitleLayout.setVisibility(View.GONE);
            delHistoryBtn.setVisibility(View.GONE);
        }

        //后退
        backIV = (ImageView)findViewById(R.id.title_back);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        keywordTV = (AutoCompleteTextView)findViewById(R.id.search_keyword);

        searchBtn = (Button)findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = keywordTV.getText().toString();
                if(StringUtils.isEmpty(keyword)){
                    Toast.makeText(SearchActivity.this, "请输入要搜索的关键词！", Toast.LENGTH_SHORT).show();
                    return;
                }

                //写入历史记录
                int historySize = sharedPreferences.getInt("history_size", 0);
                SharedPreferences.Editor localEditor = sharedPreferences.edit();

                List<String> tempList = new ArrayList<>();
                for(int i = 0; i < historySize; i++){
                    if(tempList.size() < 9) {
                        tempList.add(sharedPreferences.getString("history_" + i, ""));
                    }
                    localEditor.remove("history_" + i);
                }

                tempList.add(0, keyword);
                for(int i = 0; i < tempList.size(); i++){
                    localEditor.putString("history_" + i, tempList.get(i));
                }
                localEditor.putInt("history_size", tempList.size());
                localEditor.commit();

                //开始搜索
                Intent returnIntent = new Intent();
                returnIntent.putExtra("keyword", keyword);
                setResult(RESULT_OK, returnIntent);
                finish();

            }
        });


    }
}
