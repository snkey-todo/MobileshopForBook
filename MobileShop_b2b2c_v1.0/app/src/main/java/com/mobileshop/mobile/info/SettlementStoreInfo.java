package com.mobileshop.mobile.info;

import java.util.ArrayList;

public class SettlementStoreInfo {

	private Double goodsPrice;
	private Double orderPrice;
	private Double shippingPrice;
	private Double needPayMoney;
	private int shiptypeid;
	private int store_id;
	private String store_name;
	private String shipname;
	private ArrayList<ShiplistInfo> shiplist;

	public SettlementStoreInfo() {

	}

	public String getShipname() {
		return shipname;
	}

	public void setShipname(String shipname) {
		this.shipname = shipname;
	}

	public int getShiptypeid() {
		return shiptypeid;
	}

	public void setShiptypeid(int shiptypeid) {
		this.shiptypeid = shiptypeid;
	}

	public Double getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(Double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public Double getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(Double orderPrice) {
		this.orderPrice = orderPrice;
	}

	public Double getShippingPrice() {
		return shippingPrice;
	}

	public void setShippingPrice(Double shippingPrice) {
		this.shippingPrice = shippingPrice;
	}

	public Double getNeedPayMoney() {
		return needPayMoney;
	}

	public void setNeedPayMoney(Double needPayMoney) {
		this.needPayMoney = needPayMoney;
	}

	public int getStore_id() {
		return store_id;
	}

	public void setStore_id(int store_id) {
		this.store_id = store_id;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	public ArrayList<ShiplistInfo> getShiplist() {
		return shiplist;
	}

	public void setShiplist(ArrayList<ShiplistInfo> shiplist) {
		this.shiplist = shiplist;
	}

}
