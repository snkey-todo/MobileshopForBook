package com.mobileshop.mobile.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dawei on 4/13/15.
 */
public class Product {
    private int cost;
    private int enable_store;
    private int goods_id;
    private String name;
    private double price;
    private int product_id;
    private String sn;
    private String specs;
    private Integer[] specsvIds = new Integer[]{};
    private int store;
    private double weight;
    private String thumbnail;
    private int is_seckill;
    private int store_id;

    public int getStore_id() {
		return store_id;
	}

	public void setStore_id(int store_id) {
		this.store_id = store_id;
	}

	public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getEnable_store() {
        return enable_store;
    }

    public void setEnable_store(int enable_store) {
        this.enable_store = enable_store;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public Integer[] getSpecsvIds() {
        return specsvIds;
    }

    public void setSpecsvIds(Integer[] specsvIds) {
        this.specsvIds = specsvIds;
    }

    public int getStore() {
        return store;
    }

    public void setStore(int store) {
        this.store = store;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }


    public static Product toProduct(JSONObject object){
        if(object == null)
            return null;
        Product product = new Product();
        try{
            product.setCost(object.getInt("cost"));
            product.setEnable_store(object.getInt("enable_store"));
            product.setGoods_id(object.getInt("goods_id"));
            product.setName(object.getString("name"));
            product.setPrice(object.getDouble("price"));
            product.setProduct_id(object.getInt("product_id"));
            product.setSn(object.getString("sn"));
            product.setSpecs(object.getString("specs"));
            product.setStore(object.getInt("store"));
            product.setWeight(object.getDouble("weight"));
            product.setStore_id(object.getInt("store_id"));
            if(object.has("thumbnail")){
                product.setThumbnail(object.getString("thumbnail"));
            }
            if(object.has("is_seckill")){
                product.setIs_seckill(object.getInt("is_seckill"));
            }

            JSONArray array = object.getJSONArray("specList");
            if(array != null && array.length() > 0) {
                Integer[] specids = new Integer[array.length()];
                for (int i = 0; i < array.length(); i++) {
                    specids[i] = array.getJSONObject(i).getInt("spec_value_id");
                }
                product.setSpecsvIds(specids);
            }
        }catch(JSONException ex){
            Log.d("Product:", ex.getMessage() + "\r\n" + ex.getStackTrace());
        }
        return product;
    }

    public int getIs_seckill() {
        return is_seckill;
    }

    public void setIs_seckill(int is_seckill) {
        this.is_seckill = is_seckill;
    }
}
