package com.example.instation;

public class loginUser {
	private String account;
	private String licence;
	private String name;
	private String watchID;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getLicence() {
		return licence;
	}

	public void setLicence(String licence) {
		this.licence = licence;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWatchID() {
		return watchID;
	}

	public void setWatchID(String watchID) {
		this.watchID = watchID;
	}

	public loginUser(String account, String licence, String name, String watchID) {
		super();
		this.account = account;
		this.licence = licence;
		this.name = name;
		this.watchID = watchID;
	}
	
	

}
