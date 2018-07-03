package com.mobileshop.mobile.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobileshop.mobile.R;

import java.util.List;

/**
 * Created by Dawei on 5/20/15.
 */
public class SearchHistoryAdapter extends BaseAdapter {
    private List<String> historyList;
    private Activity context;

    public SearchHistoryAdapter(Activity context, List<String> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return historyList.get(position);
    }

    @Override
    public int getCount() {
        return historyList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = context.getLayoutInflater().inflate(R.layout.activity_search_history_item, null);
        } else {
            view = convertView;
        }

        TextView keywordTV = (TextView)view.findViewById(R.id.keyword);
        keywordTV.setText(getItem(position).toString());

        return view;
    }
}
