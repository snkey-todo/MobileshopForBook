package com.mobileshop.mobile.model;

import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by Dawei on 5/22/15.
 */
public class OrderItem implements java.io.Serializable {

    private Integer item_id;
    private Integer order_id;
    private Integer goods_id;
    private Integer product_id;

    private Integer num;
    private Integer ship_num;
    private String name;
    private String sn;
    private String image;
    private Integer store; //对应货品的库存
    private String addon;
    private Integer cat_id; // 此商品的分类
    private Double price;
    private int gainedpoint;
    private int state;//订单货物状态
    private String change_goods_name;//换到的货物名称
    private Integer change_goods_id;//换到的货物ID

    private String unit;

    private String depotId; //库房Id 2014-6-8 李奋龙



    private String other;

    public OrderItem() {
    }

    public Integer getItem_id() {
        return item_id;
    }

    public void setItem_id(Integer item_id) {
        this.item_id = item_id;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public Integer getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(Integer goods_id) {
        this.goods_id = goods_id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getShip_num() {
        return ship_num;
    }

    public void setShip_num(Integer ship_num) {
        this.ship_num = ship_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    public String getAddon() {
        return addon;
    }

    public void setAddon(String addon) {
        this.addon = addon;
    }

    public Integer getCat_id() {
        return cat_id;
    }

    public void setCat_id(Integer cat_id) {
        this.cat_id = cat_id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getGainedpoint() {
        return gainedpoint;
    }

    public void setGainedpoint(int gainedpoint) {
        this.gainedpoint = gainedpoint;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getChange_goods_name() {
        return change_goods_name;
    }

    public void setChange_goods_name(String change_goods_name) {
        this.change_goods_name = change_goods_name;
    }

    public Integer getChange_goods_id() {
        return change_goods_id;
    }

    public void setChange_goods_id(Integer change_goods_id) {
        this.change_goods_id = change_goods_id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public static OrderItem toOrderItem(JSONObject jsonObject){
        if(jsonObject == null)
            return null;
        return new Gson().fromJson(jsonObject.toString(), OrderItem.class);
    }
}
