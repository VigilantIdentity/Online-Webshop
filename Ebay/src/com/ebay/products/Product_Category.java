package com.ebay.products;

public class Product_Category {
	
	private int cat_id;
	private String name;

	public Product_Category() {
		
	}
	
	public Product_Category(int cat_id,String name) {
		
		this.cat_id=cat_id;
		this.name=name;
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
	
}
