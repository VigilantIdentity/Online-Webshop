package com.ebay.users;

public class User {
	private int id;
	private String fname, lname, email, pass, contact;
	
	public User() {
		
	}
	
	public User(int id,String fname, String lname,String email, String pass,String contact) {
		
		this.id=id;
		this.fname=fname;
		this.lname=lname;
		this.email=email;
		this.pass=pass;
		this.contact=contact;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}
	
	

}
