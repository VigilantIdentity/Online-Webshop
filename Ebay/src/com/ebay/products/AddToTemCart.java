package com.ebay.products;

public class AddToTemCart extends AddToCart {
	
	private int seller_id;
	private int product_id;
	private int buyer_id;
	private String seller_name; 
	private String product_name;
	private String buyer_name;
	private int quantity;
	private float price;
	
	public AddToTemCart() {
		
	}
	
public AddToTemCart(int seller_id,String seller_name,int product_id, String product_name,int buyer_id, String buyer_name,int quantity,float price) {
		
		this.seller_id=seller_id;
		this.product_id=product_id;
		this.buyer_id=buyer_id;
		this.seller_name=seller_name;
		this.product_name=product_name;
		this.buyer_name=buyer_name;
		this.quantity=quantity;
		this.price=price;
	}

public int getSeller_id() {
	return seller_id;
}

public void setSeller_id(int seller_id) {
	this.seller_id = seller_id;
}

public int getProduct_id() {
	return product_id;
}

public void setProduct_id(int product_id) {
	this.product_id = product_id;
}

public int getBuyer_id() {
	return buyer_id;
}

public void setBuyer_id(int buyer_id) {
	this.buyer_id = buyer_id;
}

public String getSeller_name() {
	return seller_name;
}

public void setSeller_name(String seller_name) {
	this.seller_name = seller_name;
}

public String getProduct_name() {
	return product_name;
}

public void setProduct_name(String product_name) {
	this.product_name = product_name;
}

public String getBuyer_name() {
	return buyer_name;
}

public void setBuyer_name(String buyer_name) {
	this.buyer_name = buyer_name;
}

public int getQuantity() {
	return quantity;
}

public void setQuantity(int quantity) {
	this.quantity = quantity;
}

public float getPrice() {
	return price;
}

public void setPrice(float price) {
	this.price = price;
}


}
