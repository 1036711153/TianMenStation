package com.example.instation;

import org.litepal.crud.DataSupport;

public class UnconventionInstation extends DataSupport{
	private int id;
	private String LicencePlate;
	private String Time;
	private String Watch_ID;
	private String EntranceImg;
	private String Vehicle_Property;
	private String Driver_Name;
	private String Tel;
	private String num;
	private String remark;
	private String stationcode;
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getStationcode() {
		return stationcode;
	}
	public void setStationcode(String stationcode) {
		this.stationcode = stationcode;
	}
	
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
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
	public String getWatch_ID() {
		return Watch_ID;
	}
	public void setWatch_ID(String watch_ID) {
		Watch_ID = watch_ID;
	}
	public String getEntranceImg() {
		return EntranceImg;
	}
	public void setEntranceImg(String entranceImg) {
		EntranceImg = entranceImg;
	}
	public String getVehicle_Property() {
		return Vehicle_Property;
	}
	public void setVehicle_Property(String vehicle_Property) {
		Vehicle_Property = vehicle_Property;
	}
	public String getDriver_Name() {
		return Driver_Name;
	}
	public void setDriver_Name(String driver_Name) {
		Driver_Name = driver_Name;
	}
	public String getTel() {
		return Tel;
	}
	public void setTel(String tel) {
		Tel = tel;
	}
	@Override
	public String toString() {
		return "UnconventionInstation [id=" + id + ", LicencePlate="
				+ LicencePlate + ", Time=" + Time + ", Watch_ID=" + Watch_ID
				+ ", EntranceImg=" + EntranceImg + ", Vehicle_Property="
				+ Vehicle_Property + ", Driver_Name=" + Driver_Name + ", Tel="
				+ Tel + ", num=" + num + ", remark=" + remark
				+ ", stationcode=" + stationcode + "]";
	}
}
