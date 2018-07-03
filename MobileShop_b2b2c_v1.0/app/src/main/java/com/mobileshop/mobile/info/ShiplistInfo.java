package com.mobileshop.mobile.info;

public class ShiplistInfo {

	private String name;
    private Double shipPrice;
    private int type_id;
    
    public ShiplistInfo(){
    	
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getShipPrice() {
		return shipPrice;
	}

	public void setShipPrice(Double shipPrice) {
		this.shipPrice = shipPrice;
	}

	public int getType_id() {
		return type_id;
	}

	public void setType_id(int type_id) {
		this.type_id = type_id;
	}
    
}
