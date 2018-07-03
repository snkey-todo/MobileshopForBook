package com.mobileshop.mobile.model;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dawei on 4/29/15.
 */
public class CartItem {
    private Integer id;
    private Integer product_id;
    private Integer goods_id;
    private String name;

    private Double mktprice;
    private Double price;

    private Double coupPrice; // 优惠后的价格
    private Double subtotal; // 小计

    private int num;
    private Integer limitnum; //限购数量(对于赠品)
    private String image_default;
    private Integer point;
    private Integer itemtype; //物品类型(0商品，1捆绑商品，2赠品)
    private String sn; // 捆绑促销的货号
    private String addon; //配件字串
    private String specs;
    private int catid; //是否货到付款
    private Map others; //扩展项,可通过 ICartItemFilter 进行过滤修改


    private String store_name;	//店铺名称

    public int getGoods_transfee_charge() {
        return goods_transfee_charge;
    }

    public void setGoods_transfee_charge(int goods_transfee_charge) {
        this.goods_transfee_charge = goods_transfee_charge;
    }

    private int goods_transfee_charge; //是否由卖家承担运费（即免运费）
    private String unit;
    private double weight;

    private boolean checked;

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    public Integer getStore_id() {
        return store_id;
    }

    public void setStore_id(Integer store_id) {
        this.store_id = store_id;
    }

    private Integer store_id;	//店铺Id

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }


    public Map getOthers() {
        if (others == null) others = new HashMap();
        return others;
    }

    public void setOthers(Map others) {
        this.others = others;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer productId) {
        product_id = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMktprice() {
        return mktprice;
    }

    public void setMktprice(Double mktprice) {
        this.mktprice = mktprice;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getCoupPrice() {
        return coupPrice;
    }

    public void setCoupPrice(Double coupPrice) {
        this.coupPrice = coupPrice;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getImage_default() {
        return image_default;
    }

    public void setImage_default(String imageDefault) {
        image_default = imageDefault;
    }

    public Integer getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(Integer goodsId) {
        goods_id = goodsId;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }


    public Integer getLimitnum() {
        return limitnum;
    }

    public void setLimitnum(Integer limitnum) {
        this.limitnum = limitnum;
    }

    public Integer getItemtype() {
        return itemtype;
    }

    public void setItemtype(Integer itemtype) {
        this.itemtype = itemtype;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getAddon() {
        return addon;
    }

    public void setAddon(String addon) {
        this.addon = addon;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public int getCatid() {
        return catid;
    }

    public void setCatid(int catid) {
        this.catid = catid;
    }


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public static CartItem toCartItem(JSONObject jsonObject){
        if(jsonObject == null)
            return null;
        try{
            CartItem cartItem = new CartItem();
            cartItem.setAddon(jsonObject.getString("addon"));
            cartItem.setCatid(jsonObject.getInt("catid"));
            cartItem.setCoupPrice(jsonObject.getDouble("coupPrice"));
            cartItem.setGoods_id(jsonObject.getInt("goods_id"));
            cartItem.setId(jsonObject.getInt("id"));
            cartItem.setImage_default(jsonObject.getString("image_default"));
            cartItem.setItemtype(jsonObject.getInt("itemtype"));
            cartItem.setLimitnum(jsonObject.getInt("limitnum"));
            cartItem.setMktprice(jsonObject.getDouble("mktprice"));
            cartItem.setName(jsonObject.getString("name"));
            cartItem.setNum(jsonObject.getInt("num"));
            cartItem.setPoint(jsonObject.getInt("point"));
            cartItem.setPrice(jsonObject.getDouble("price"));
            cartItem.setProduct_id(jsonObject.getInt("product_id"));
            cartItem.setSn(jsonObject.getString("sn"));
            cartItem.setSpecs(jsonObject.getString("specs"));
            cartItem.setSubtotal(jsonObject.getDouble("subtotal"));
            cartItem.setUnit(jsonObject.getString("unit"));
            cartItem.setWeight(jsonObject.getDouble("weight"));
            cartItem.setStore_name(jsonObject.getString("store_name"));
            return cartItem;
        }catch(Exception ex){

        }
        return null;
    }
}
