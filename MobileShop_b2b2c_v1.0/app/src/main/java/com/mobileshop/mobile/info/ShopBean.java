package com.mobileshop.mobile.info;


public class ShopBean {

	public int shopcarid;
	public String stationname;
	public String sha1;
	public String img_sha1;
	public Double price;
	public String goodsname;
	public String vcardname;
	public String overduetime;//过期时间
	public int num;//数量
	public String seller_sha1;
	private boolean isChoosed;		//商品是否在购物车中被选中
	private int promotion_ids;
	
	
	
//	bundle.putString("goodsname", mVIPInfo.getName());
//	bundle.putInt("flog", flog);
//	bundle.putDouble("price", mVIPInfo.getPrice());
//	bundle.putInt("count", mVIPInfo.getCount());
//	bundle.putString("address", mVIPInfo.getAddress());
//	bundle.putDouble("discount", mVIPInfo.getDiscount());
//	bundle.putString("phone", mVIPInfo.getPhone());
//	bundle.putString("comp_name", mVIPInfo.getCompanyName());
//	bundle.putString("src", mVIPInfo.getSrc());
//	bundle.putString("img_sha1", mVIPInfo.getImg_sha1());
//	bundle.putString("sha1", mVIPInfo.getSha1());
//	bundle.putInt("pay_type", mVIPInfo.getPay_type());
//	bundle.putString("seller_sha1", mVIPInfo.getSeller_sha1());
//	bundle.putDouble("lon", mVIPInfo.getGeo_x());
//	bundle.putDouble("lat", mVIPInfo.getGeo_y());
	
	public boolean isChoosed() {
		return isChoosed;
	}

	public void setChoosed(boolean isChoosed) {
		this.isChoosed = isChoosed;
	}

	public ShopBean(){
		
	}
	
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getOverduetime() {
		return overduetime;
	}

	public void setOverduetime(String overduetime) {
		this.overduetime = overduetime;
	}

	public String getVcardname() {
		return vcardname;
	}
	public void setVcardname(String vcardname) {
		this.vcardname = vcardname;
	}
	public int getShopCaridId() {
		return shopcarid;
	}


	public void setShopCarid(int shopcarid) {
		this.shopcarid = shopcarid;
	}


	public String getStationname() {
		return stationname;
	}


	public void setStationname(String stationname) {
		this.stationname = stationname;
	}

	public String getSha1() {
		return sha1;
	}


	public void setSha1(String sha1) {
		this.sha1 = sha1;
	}


	public String getImg_sha1() {
		return img_sha1;
	}


	public void setImg_sha1(String img_sha1) {
		this.img_sha1 = img_sha1;
	}


	public Double getPrice() {
		return price;
	}


	public void setPrice(Double price) {
		this.price = price;
	}


	public String getGoodsname() {
		return goodsname;
	}


	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}

	public String getSeller_sha1() {
		return seller_sha1;
	}

	public void setSeller_sha1(String seller_sha1) {
		this.seller_sha1 = seller_sha1;
	}

	public int getPromotion_ids() {
		return promotion_ids;
	}

	public void setPromotion_ids(int promotion_ids) {
		this.promotion_ids = promotion_ids;
	}

}
