package com.mobileshop.mobile.info;

public class StoryInfo extends Object {

//	private float goodsPrice;
	private Double needPayMoney;
	private Double weight;
	private int store_id;
	private String store_name;
    private Double orderPrice;
    private Double shippingPrice;
	public StoryInfo() {

	}

//	public StoryInfo(float goodsPrice, float needPayMoney, float weight,
//			int store_id, String store_name) {
//		this.goodsPrice = goodsPrice;
//		this.needPayMoney = needPayMoney;
//		this.weight = weight;
//		this.store_id = store_id;
//		this.store_name = store_name;
//	}

//	public float getGoodsPrice() {
//		return goodsPrice;
//	}
//
//	public void setGoodsPrice(float goodsPrice) {
//		this.goodsPrice = goodsPrice;
//	}

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

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
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

}
