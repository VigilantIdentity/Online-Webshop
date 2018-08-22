package com.ebay.products;

public class Product {
	
	private int pid;
	private int user_id;
	private int cat_id;
	private String name, description;
	private float price;
	
	public Product() {
		
	}
	
public Product(int pid, int user_id, int cat_id,String name, String description, float price) {
	
	this.pid=pid;
	this.user_id=user_id;
	this.cat_id=cat_id;
	this.name=name;
	this.description=description;
	this.price=price;
		
	}

public int getPid() {
	return pid;
}

public void setPid(int pid) {
	this.pid = pid;
}

public int getUser_id() {
	return user_id;
}

public void setUser_id(int user_id) {
	this.user_id = user_id;
}

public int getCat_id() {
	return cat_id;
}

public void setCat_id(int cat_id) {
	this.cat_id = cat_id;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getDescription() {
	return description;
}

public void setDescription(String description) {
	this.description = description;
}

public float getPrice() {
	return price;
}

public void setPrice(float price) {
	this.price = price;
}

}
