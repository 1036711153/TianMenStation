package com.example.outstation.entity;

public   class Driver {
	private String  Driver_id;
	private String Driver_name;
	private String tel;
	private String AdultNum;
	private String ID_Card;
	private String photo;
	private String anjian_result;
	private String baoban_result;
	
	
	public Driver(String driver_id, String driver_name, String tel,
			String adultNum, String iD_Card, String photo,
			String anjian_result, String baoban_result) {
		super();
		Driver_id = driver_id;
		Driver_name = driver_name;
		this.tel = tel;
		AdultNum = adultNum;
		ID_Card = iD_Card;
		this.photo = photo;
		this.anjian_result = anjian_result;
		this.baoban_result = baoban_result;
	}
	@Override
	public String toString() {
		return "Driver [Driver_id=" + Driver_id + ", Driver_name="
				+ Driver_name + ", tel=" + tel + ", AdultNum=" + AdultNum
				+ ", ID_Card=" + ID_Card + ", photo=" + photo + "]";
	}
	public String getDriver_id() {
		return Driver_id;
	}
	public void setDriver_id(String driver_id) {
		Driver_id = driver_id;
	}
	public String getDriver_name() {
		return Driver_name;
	}
	public void setDriver_name(String driver_name) {
		Driver_name = driver_name;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getAdultNum() {
		return AdultNum;
	}
	public void setAdultNum(String adultNum) {
		AdultNum = adultNum;
	}
	public String getID_Card() {
		return ID_Card;
	}
	public void setID_Card(String iD_Card) {
		ID_Card = iD_Card;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getAnjian_result() {
		return anjian_result;
	}
	public void setAnjian_result(String anjian_result) {
		this.anjian_result = anjian_result;
	}
	public String getBaoban_result() {
		return baoban_result;
	}
	public void setBaoban_result(String baoban_result) {
		this.baoban_result = baoban_result;
	}
}
