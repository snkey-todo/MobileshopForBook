package com.ictuniv.dataanalysis.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/26.
 */

public class MonthEntity implements Serializable {

    /**
     * id : 226
     * username : tom
     * month : 01
     * payment : 20.0
     */

    private int id;
    private String username;
    private String month;
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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
}
