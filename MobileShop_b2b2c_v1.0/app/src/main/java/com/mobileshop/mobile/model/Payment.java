package com.mobileshop.mobile.model;

import android.text.Html;

import com.mobileshop.mobile.utils.StringUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Dawei on 5/15/15.
 */
public class Payment implements Serializable{

    private Integer id;

    private String name;

    private String config;

    private String biref;

    private Double pay_fee;

    private String type;

    public Payment() {
    }

    public Payment(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.biref = "";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getBiref() {
        if(!StringUtils.isEmpty(biref)) {
            return Html.fromHtml(biref).toString().replace("\t","").replace("\n","");
        }
        return "";
    }

    public void setBiref(String biref) {
        if(!StringUtils.isEmpty(biref)) {
            this.biref = Html.fromHtml(biref).toString();
        }else{
            this.biref = "";
        }
    }

    public Double getPay_fee() {
        return pay_fee;
    }

    public void setPay_fee(Double pay_fee) {
        this.pay_fee = pay_fee;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JSONObject getConfigJson(){
        if(StringUtils.isEmpty(getConfig()))
            return null;
        try {
            return new JSONObject(getConfig());
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }


    public static Payment toPayment(JSONObject jsonObject){
        if(jsonObject == null)
            return null;
        Payment payment = new Gson().fromJson(jsonObject.toString(), Payment.class);
        return payment;
    }
}
