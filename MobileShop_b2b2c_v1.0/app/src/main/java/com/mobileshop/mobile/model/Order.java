package com.mobileshop.mobile.model;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dawei on 5/19/15.
 */
public class Order implements Serializable {
    private Integer order_id;

    private String sn;

    private Integer member_id;

    private Integer status;

    private Integer pay_status;

    private Integer ship_status;

    //状态显示字串
    private String shipStatus;
    private String payStatus;
    private String orderStatus;


    //收货地区id三级省市的最后一级
    private Integer regionid;
    private Integer shipping_id;//配送方式

    private String shipping_type;

    private String shipping_area;

    private String goods;

    private Long create_time;

    private String ship_name;

    private String ship_addr;

    private String ship_zip;

    private String ship_email;

    private String ship_mobile;

    private String ship_tel;

    private String ship_day;

    private String ship_time;

    private Integer is_protect;

    private Double protect_price;

    private Double goods_amount;

    private Double shipping_amount;
    private Double discount; //优惠金额
    private Double order_amount;

    private Double weight;

    private Double paymoney;

    private String remark;

    private Integer disabled;

    private Integer payment_id;
    private String payment_name;
    private String payment_type;
    private Integer goods_num;
    private int gainedpoint;
    private int consumepoint;

    private Integer depotid; //发货仓库id

    private String cancel_reason;	//取消订单的原因

    private int sale_cmpl;
    private Integer sale_cmpl_time;

    private Integer ship_provinceid;
    private Integer ship_cityid;
    private Integer ship_regionid;
    private Integer signing_time;
    private String the_sign;
    private String ship_no;

    private Long allocation_time;


    private String admin_remark;
    private Integer address_id;//地址id

    private double need_pay_money;

    private String items_json;
    private List<OrderItem> itemList = new ArrayList<>();

    public Integer getStore_id() {
        return store_id;
    }

    public void setStore_id(Integer store_id) {
        this.store_id = store_id;
    }

    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }

    public String[] getStoreids() {
        return storeids;
    }

    public void setStoreids(String[] storeids) {
        this.storeids = storeids;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Integer getBill_status() {
        return bill_status;
    }

    public void setBill_status(Integer bill_status) {
        this.bill_status = bill_status;
    }

    public Integer getBill_sn() {
        return bill_sn;
    }

    public void setBill_sn(Integer bill_sn) {
        this.bill_sn = bill_sn;
    }

    private Integer store_id;//店铺Id
    private Integer parent_id;//订单父类Id
    private String [] storeids;
    private Double commission;	//订单佣金价格
    private Integer bill_status;	//订单结算状态
    private Integer bill_sn;	//结算订单编号
    public Order() {
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Integer getMember_id() {
        return member_id;
    }

    public void setMember_id(Integer member_id) {
        this.member_id = member_id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPay_status() {
        return pay_status;
    }

    public void setPay_status(Integer pay_status) {
        this.pay_status = pay_status;
    }

    public Integer getShip_status() {
        return ship_status;
    }

    public void setShip_status(Integer ship_status) {
        this.ship_status = ship_status;
    }

    public String getShipStatus() {
        return shipStatus;
    }

    public void setShipStatus(String shipStatus) {
        this.shipStatus = shipStatus;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getOrderStatus() {
        if(getStatus().intValue() != 0) {
            return orderStatus;
        }else{
            return payStatus;
        }
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getRegionid() {
        return regionid;
    }

    public void setRegionid(Integer regionid) {
        this.regionid = regionid;
    }

    public Integer getShipping_id() {
        return shipping_id;
    }

    public void setShipping_id(Integer shipping_id) {
        this.shipping_id = shipping_id;
    }

    public String getShipping_type() {
        return shipping_type;
    }

    public void setShipping_type(String shipping_type) {
        this.shipping_type = shipping_type;
    }

    public String getShipping_area() {
        return shipping_area;
    }

    public void setShipping_area(String shipping_area) {
        this.shipping_area = shipping_area;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    public String getShip_name() {
        return ship_name;
    }

    public void setShip_name(String ship_name) {
        this.ship_name = ship_name;
    }

    public String getShip_addr() {
        return ship_addr;
    }

    public void setShip_addr(String ship_addr) {
        this.ship_addr = ship_addr;
    }

    public String getShip_zip() {
        return ship_zip;
    }

    public void setShip_zip(String ship_zip) {
        this.ship_zip = ship_zip;
    }

    public String getShip_email() {
        return ship_email;
    }

    public void setShip_email(String ship_email) {
        this.ship_email = ship_email;
    }

    public String getShip_mobile() {
        return ship_mobile;
    }

    public void setShip_mobile(String ship_mobile) {
        this.ship_mobile = ship_mobile;
    }

    public String getShip_tel() {
        return ship_tel;
    }

    public void setShip_tel(String ship_tel) {
        this.ship_tel = ship_tel;
    }

    public String getShip_no() {
        return ship_no;
    }

    public void setShip_no(String ship_no) {
        this.ship_no = ship_no;
    }

    public String getShip_day() {
        return ship_day;
    }

    public void setShip_day(String ship_day) {
        this.ship_day = ship_day;
    }

    public String getShip_time() {
        return ship_time;
    }

    public void setShip_time(String ship_time) {
        this.ship_time = ship_time;
    }

    public Integer getIs_protect() {
        return is_protect;
    }

    public void setIs_protect(Integer is_protect) {
        this.is_protect = is_protect;
    }

    public Double getProtect_price() {
        return protect_price;
    }

    public void setProtect_price(Double protect_price) {
        this.protect_price = protect_price;
    }

    public Double getGoods_amount() {
        return goods_amount;
    }

    public void setGoods_amount(Double goods_amount) {
        this.goods_amount = goods_amount;
    }

    public Double getShipping_amount() {
        return shipping_amount;
    }

    public void setShipping_amount(Double shipping_amount) {
        this.shipping_amount = shipping_amount;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(Double order_amount) {
        this.order_amount = order_amount;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getPaymoney() {
        return paymoney;
    }

    public void setPaymoney(Double paymoney) {
        this.paymoney = paymoney;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getDisabled() {
        return disabled;
    }

    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    public Integer getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(Integer payment_id) {
        this.payment_id = payment_id;
    }

    public String getPayment_name() {
        return payment_name;
    }

    public void setPayment_name(String payment_name) {
        this.payment_name = payment_name;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public Integer getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(Integer goods_num) {
        this.goods_num = goods_num;
    }

    public int getGainedpoint() {
        return gainedpoint;
    }

    public void setGainedpoint(int gainedpoint) {
        this.gainedpoint = gainedpoint;
    }

    public int getConsumepoint() {
        return consumepoint;
    }

    public void setConsumepoint(int consumepoint) {
        this.consumepoint = consumepoint;
    }

    public Integer getDepotid() {
        return depotid;
    }

    public void setDepotid(Integer depotid) {
        this.depotid = depotid;
    }

    public String getCancel_reason() {
        return cancel_reason;
    }

    public void setCancel_reason(String cancel_reason) {
        this.cancel_reason = cancel_reason;
    }

    public int getSale_cmpl() {
        return sale_cmpl;
    }

    public void setSale_cmpl(int sale_cmpl) {
        this.sale_cmpl = sale_cmpl;
    }

    public Integer getSale_cmpl_time() {
        return sale_cmpl_time;
    }

    public void setSale_cmpl_time(Integer sale_cmpl_time) {
        this.sale_cmpl_time = sale_cmpl_time;
    }

    public Integer getShip_provinceid() {
        return ship_provinceid;
    }

    public void setShip_provinceid(Integer ship_provinceid) {
        this.ship_provinceid = ship_provinceid;
    }

    public Integer getShip_cityid() {
        return ship_cityid;
    }

    public void setShip_cityid(Integer ship_cityid) {
        this.ship_cityid = ship_cityid;
    }

    public Integer getShip_regionid() {
        return ship_regionid;
    }

    public void setShip_regionid(Integer ship_regionid) {
        this.ship_regionid = ship_regionid;
    }

    public Integer getSigning_time() {
        return signing_time;
    }

    public void setSigning_time(Integer signing_time) {
        this.signing_time = signing_time;
    }

    public String getThe_sign() {
        return the_sign;
    }

    public void setThe_sign(String the_sign) {
        this.the_sign = the_sign;
    }

    public Long getAllocation_time() {
        return allocation_time;
    }

    public void setAllocation_time(Long allocation_time) {
        this.allocation_time = allocation_time;
    }

    public String getAdmin_remark() {
        return admin_remark;
    }

    public void setAdmin_remark(String admin_remark) {
        this.admin_remark = admin_remark;
    }

    public Integer getAddress_id() {
        return address_id;
    }

    public void setAddress_id(Integer address_id) {
        this.address_id = address_id;
    }

    public double getNeed_pay_money() {
        return need_pay_money;
    }

    public void setNeed_pay_money(double need_pay_money) {
        this.need_pay_money = need_pay_money;
    }

    public String getItems_json() {
        return items_json;
    }

    public void setItems_json(String items_json) {
        this.items_json = items_json;
    }

    public List<OrderItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<OrderItem> itemList) {
        this.itemList = itemList;
    }

    public static Order toOrder(JSONObject jsonObject){
        if(jsonObject == null)
            return null;
        return new Gson().fromJson(jsonObject.toString(), Order.class);
    }
}
