package com.mobileshop.mobile.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dawei on 4/16/15.
 */
public class Comment {

    private int lv_id;
    private int sex;
    private String uname;
    private String face;
    private String levelname;
    private int comment_id;
    private int goods_id;
    private int member_id;
    private String content;
    private int dateline;
    private String reply;
    private int replytime;
    private int grade;

    public int getLv_id() {
        return lv_id;
    }

    public void setLv_id(int lv_id) {
        this.lv_id = lv_id;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getLevelname() {
        return levelname;
    }

    public void setLevelname(String levelname) {
        this.levelname = levelname;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDateline() {
        return dateline;
    }

    public void setDateline(int dateline) {
        this.dateline = dateline;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public int getReplytime() {
        return replytime;
    }

    public void setReplytime(int replytime) {
        this.replytime = replytime;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public static Comment toComment(JSONObject object){
        Comment comment = new Comment();
        try{
            comment.setLv_id(object.getInt("lv_id"));
            comment.setSex(object.getInt("sex"));
            comment.setUname(object.getString("uname"));
            comment.setFace(object.getString("face"));
            comment.setLevelname(object.getString("levelname"));
            comment.setComment_id(object.getInt("comment_id"));
            comment.setGoods_id(object.getInt("goods_id"));
            comment.setMember_id(object.getInt("member_id"));
            comment.setContent(object.getString("content"));
            comment.setDateline(object.getInt("dateline"));
            comment.setReply(object.getString("reply"));
            comment.setReplytime(object.getInt("replytime"));
            comment.setGrade(object.getInt("grade"));
        }catch(JSONException ex){

        }
        return comment;
    }

}
