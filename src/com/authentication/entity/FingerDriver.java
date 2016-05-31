package com.authentication.entity;

import org.litepal.crud.DataSupport;

public class FingerDriver extends DataSupport{
	private int id;
	
	private String pageId;

	//驾驶员的Id
	private String Driver_ID;
	//驾驶员的姓名
	private String Driver_Name;
	//驾驶员的身份证编号
	private String ID_Card;
	
	
	public String getID_Card() {
		return ID_Card;
	}
	public void setID_Card(String iD_Card) {
		ID_Card = iD_Card;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDriver_ID() {
		return Driver_ID;
	}
	public void setDriver_ID(String driver_ID) {
		Driver_ID = driver_ID;
	}
	public String getDriver_Name() {
		return Driver_Name;
	}
	public void setDriver_Name(String driver_Name) {
		Driver_Name = driver_Name;
	}
	
	public String getPageId() {
		return pageId;
	}
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
}
