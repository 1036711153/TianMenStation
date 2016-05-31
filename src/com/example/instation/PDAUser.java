package com.example.instation;

public class PDAUser {
	private String ID;
	private String account;
	private String rightCode;
	private String username;
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getRightCode() {
		return rightCode;
	}
	public void setRightCode(String rightCode) {
		this.rightCode = rightCode;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Override
	public String toString() {
		return "PDAUser [ID=" + ID + ", account=" + account + ", rightCode="
				+ rightCode + ", username=" + username + "]";
	}
	
}
