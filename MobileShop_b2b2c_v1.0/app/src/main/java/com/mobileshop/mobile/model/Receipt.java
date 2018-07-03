package com.mobileshop.mobile.model;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Dawei on 5/16/15.
 */
public class Receipt implements Serializable {
    private int type;
    private String title;
    private String content;

    public Receipt() {
    }

    public Receipt(int type, String title, String content) {
        this.type = type;
        this.title = title;
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static Receipt toReceipt(JSONObject jsonObject){
        if(jsonObject == null)
            return null;
        return new Gson().fromJson(jsonObject.toString(), Receipt.class);
    }
}
