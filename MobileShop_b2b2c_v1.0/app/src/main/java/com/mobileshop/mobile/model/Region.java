package com.mobileshop.mobile.model;

import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by Dawei on 5/12/15.
 */
public class Region implements java.io.Serializable{

    private Integer region_id;
    private Integer p_region_id;
    private String region_path;
    private Integer region_grade;
    private String local_name;
    private String zipcode;
    private Integer cod;


    public Region() {
    }

    public Region(String local_name, Integer region_id) {
        this.local_name = local_name;
        this.region_id = region_id;
    }

    public Integer getRegion_id() {
        return region_id;
    }

    public void setRegion_id(Integer region_id) {
        this.region_id = region_id;
    }

    public Integer getP_region_id() {
        return p_region_id;
    }

    public void setP_region_id(Integer p_region_id) {
        this.p_region_id = p_region_id;
    }

    public String getRegion_path() {
        return region_path;
    }

    public void setRegion_path(String region_path) {
        this.region_path = region_path;
    }

    public Integer getRegion_grade() {
        return region_grade;
    }

    public void setRegion_grade(Integer region_grade) {
        this.region_grade = region_grade;
    }

    public String getLocal_name() {
        return local_name;
    }

    public void setLocal_name(String local_name) {
        this.local_name = local_name;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public static Region toRegion(JSONObject jsonObject){
        if(jsonObject == null)
            return null;
        return new Gson().fromJson(jsonObject.toString(), Region.class);
    }
}
