package com.mobileshop.mobile.model;

import android.text.Html;

import com.mobileshop.mobile.utils.StringUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Dawei on 5/15/15.
 */
public class Shipping implements Serializable{

    private Integer type_id;

    private String name;

    private Integer protect;
    private Float protect_rate;
    private Float min_price;

    private Integer has_cod;

    private Integer corp_id;
    private Integer ordernum;
    private Integer disabled;
    private Integer is_same;
    private String detail;
    private String config;
    private String  expressions;
    private Double price; //需要对订单增加的价格

    public Shipping() {
    }

    public Integer getType_id() {
        return type_id;
    }

    public void setType_id(Integer type_id) {
        this.type_id = type_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProtect() {
        return protect;
    }

    public void setProtect(Integer protect) {
        this.protect = protect;
    }

    public Float getProtect_rate() {
        return protect_rate;
    }

    public void setProtect_rate(Float protect_rate) {
        this.protect_rate = protect_rate;
    }

    public Float getMin_price() {
        return min_price;
    }

    public void setMin_price(Float min_price) {
        this.min_price = min_price;
    }

    public Integer getHas_cod() {
        return has_cod;
    }

    public void setHas_cod(Integer has_cod) {
        this.has_cod = has_cod;
    }

    public Integer getCorp_id() {
        return corp_id;
    }

    public void setCorp_id(Integer corp_id) {
        this.corp_id = corp_id;
    }

    public Integer getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(Integer ordernum) {
        this.ordernum = ordernum;
    }

    public Integer getDisabled() {
        return disabled;
    }

    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    public Integer getIs_same() {
        return is_same;
    }

    public void setIs_same(Integer is_same) {
        this.is_same = is_same;
    }

    public String getDetail() {
        if(!StringUtils.isEmpty(detail)) {
            return Html.fromHtml(detail).toString().replace("\t", "").replace("\n", "");
        }
        return "";
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getExpressions() {
        return expressions;
    }

    public void setExpressions(String expressions) {
        this.expressions = expressions;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public static Shipping toShipping(JSONObject jsonObject){
        if(jsonObject == null)
            return null;
        return new Gson().fromJson(jsonObject.toString(), Shipping.class);
    }
}
