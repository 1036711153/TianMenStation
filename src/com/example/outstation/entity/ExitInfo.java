package com.example.outstation.entity;

import org.litepal.crud.DataSupport;

public class ExitInfo extends DataSupport{
	private int id;
	private String LicencePlate;
	private String Time;
	private String WatchID;
	private String InCarImaURL;
	private String OutCarImaURL;
	private int IsAttached;
	private String ExitWay;
	private int AdultNum;
	private int ChildrenNum;
	private String stationcode;
	
	public String getStationcode() {
		return stationcode;
	}
	public void setStationcode(String stationcode) {
		this.stationcode = stationcode;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLicencePlate() {
		return LicencePlate;
	}
	public void setLicencePlate(String licencePlate) {
		LicencePlate = licencePlate;
	}
	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}
	public String getWatchID() {
		return WatchID;
	}
	public void setWatchID(String watchID) {
		WatchID = watchID;
	}
	public String getInCarImaURL() {
		return InCarImaURL;
	}
	public void setInCarImaURL(String inCarImaURL) {
		InCarImaURL = inCarImaURL;
	}
	public String getOutCarImaURL() {
		return OutCarImaURL;
	}
	public void setOutCarImaURL(String outCarImaURL) {
		OutCarImaURL = outCarImaURL;
	}
	public int getIsAttached() {
		return IsAttached;
	}
	public void setIsAttached(int isAttached) {
		IsAttached = isAttached;
	}
	public String getExitWay() {
		return ExitWay;
	}
	public void setExitWay(String exitWay) {
		ExitWay = exitWay;
	}
	public int getAdultNum() {
		return AdultNum;
	}
	public void setAdultNum(int adultNum) {
		AdultNum = adultNum;
	}
	public int getChildrenNum() {
		return ChildrenNum;
	}
	public void setChildrenNum(int childrenNum) {
		ChildrenNum = childrenNum;
	}
	@Override
	public String toString() {
		return "ExitInfo [id=" + id + ", LicencePlate=" + LicencePlate
				+ ", Time=" + Time + ", WatchID=" + WatchID + ", InCarImaURL="
				+ InCarImaURL + ", OutCarImaURL=" + OutCarImaURL
				+ ", IsAttached=" + IsAttached + ", ExitWay=" + ExitWay
				+ ", AdultNum=" + AdultNum + ", ChildrenNum=" + ChildrenNum
				+ ", stationcode=" + stationcode + "]";
	}

}
