package com.mobileshop.mobile.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dawei on 3/28/15.
 */
public class Goods {

    private int goods_id;
    private String name;
    private String thumbnail;
    private double price;
    private double mktprice;
    private int view_count;
    private int buy_count;
    private String goods_type;
    private String sn;
    private int favorite_id;

    public Goods() {
    }

    public Goods(String name, String thumbnail, double price, double mktprice, int view_count, int buy_count, String goods_type) {
        this.name = name;
        this.thumbnail = thumbnail;
        this.price = price;
        this.mktprice = mktprice;
        this.view_count = view_count;
        this.buy_count = buy_count;
        this.goods_type = goods_type;
    }

    public Integer getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(Integer goods_id) {
        this.goods_id = goods_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getMktprice() {
        return mktprice;
    }

    public void setMktprice(Double mktprice) {
        this.mktprice = mktprice;
    }

    public Integer getView_count() {
        return view_count;
    }

    public void setView_count(Integer view_count) {
        this.view_count = view_count;
    }

    public Integer getBuy_count() {
        return buy_count;
    }

    public void setBuy_count(Integer buy_count) {
        this.buy_count = buy_count;
    }

    public String getGoods_type() {
        return goods_type;
    }

    public void setGoods_type(String goods_type) {
        this.goods_type = goods_type;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public int getFavorite_id() {
        return favorite_id;
    }

    public void setFavorite_id(int favorite_id) {
        this.favorite_id = favorite_id;
    }

    /**
     * 将jsonobject对象转换为Goods对象
     * @param jsonObject
     * @return
     */
    public static Goods toGoods(JSONObject jsonObject){
        if(jsonObject == null)
            return null;
        try {
            Goods goods = new Goods();
            goods.setGoods_id(jsonObject.getInt("goods_id"));
            goods.setName(jsonObject.getString("name"));
            goods.setThumbnail(jsonObject.getString("thumbnail"));
            goods.setPrice(jsonObject.getDouble("price"));
            goods.setMktprice(jsonObject.getDouble("mktprice"));
            goods.setView_count(jsonObject.getInt("view_count"));
            goods.setBuy_count(jsonObject.getInt("buy_count"));
            if(jsonObject.has("sn")) {
                goods.setSn(jsonObject.getString("sn"));
            }
            if(jsonObject.has("favorite_id")){
                goods.setFavorite_id(jsonObject.getInt("favorite_id"));
            }
            return goods;
        }catch(JSONException ex){
            return null;
        }
    }
}
