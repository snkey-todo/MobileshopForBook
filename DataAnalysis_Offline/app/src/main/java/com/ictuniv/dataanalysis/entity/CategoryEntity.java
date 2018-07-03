package com.ictuniv.dataanalysis.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/26.
 */

public class CategoryEntity implements Serializable {


    /**
     * id : 299
     * username : tom
     * category : clothes
     * payment : 180.0
     */

    private int id;
    private String username;
    private String category;
    private String payment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
}
