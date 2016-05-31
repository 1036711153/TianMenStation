package com.authentication.entity;

import org.litepal.crud.DataSupport;

public class AutoCarNum extends DataSupport{
	private int id;
	private String carNum;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCarNum() {
		return carNum;
	}
	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

}
