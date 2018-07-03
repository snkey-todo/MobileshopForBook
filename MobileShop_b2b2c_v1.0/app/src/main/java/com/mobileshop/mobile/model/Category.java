package com.mobileshop.mobile.model;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dawei on 3/24/15.
 */
public class Category {
    private Integer cat_id;
    private String name;
    private Integer parent_id;
    private String image;
    private int level = 1;
    //子分类
    private List<Category> children = new ArrayList<>();

    public Category() {
    }

    public Category(Integer cat_id, String name, Integer parent_id, String image, int level) {
        this.cat_id = cat_id;
        this.name = name;
        this.parent_id = parent_id;
        this.image = image;
        this.level = level;
    }

    public Integer getCat_id() {
        return cat_id;
    }

    public void setCat_id(Integer cat_id) {
        this.cat_id = cat_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }

    public static Category toCategory(JSONObject jsonObject){
        if(jsonObject == null)
            return null;
        return new Gson().fromJson(jsonObject.toString(), Category.class);
    }
}
