package com.mobileshop.mobile.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mobileshop.mobile.Constants;
import com.mobileshop.mobile.R;
import com.mobileshop.mobile.model.Comment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Dawei on 4/16/15.
 */
public class GoodsCommentAdapter  extends BaseAdapter {

    private List<Comment> commentList;
    private Activity context;

    public GoodsCommentAdapter(Activity context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return commentList.get(position);
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = context.getLayoutInflater().inflate(R.layout.activity_goods_comment_item, null);
        } else {
            view = convertView;
        }

        Comment comment = commentList.get(position);

        TextView uname = (TextView)view.findViewById(R.id.comment_uname);
        uname.setText(comment.getUname());

        TextView commentTime = (TextView)view.findViewById(R.id.comment_time);
        commentTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(((long)comment.getDateline()) * 1000)));

        TextView commentContent = (TextView)view.findViewById(R.id.comment_content);
        commentContent.setText(comment.getContent());

        RatingBar grade = (RatingBar)view.findViewById(R.id.comment_grade);
        grade.setNumStars(comment.getGrade());

        ImageView face = (ImageView)view.findViewById(R.id.comment_face);
        Constants.imageLoader.displayImage(comment.getFace(), face, Constants.displayImageOptions);

        return view;
    }
}
