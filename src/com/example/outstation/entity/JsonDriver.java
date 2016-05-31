package com.example.outstation.entity;

import java.io.Serializable;

import android.R.integer;

public class JsonDriver implements Serializable{
	private String driver_name, driver_id, car_number, driver_report,
	check_car, phone, Driving_license_type, Driving_license;
	private String photo;
	private int load_maxnumber;
	
	public int getLoad_maxnumber() {
		return load_maxnumber;
	}
	public void setLoad_maxnumber(int load_maxnumber) {
		this.load_maxnumber = load_maxnumber;
	}
	public String getDriver_name() {
		return driver_name;
	}
	public void setDriver_name(String driver_name) {
		this.driver_name = driver_name;
	}
	public String getDriver_id() {
		return driver_id;
	}
	public void setDriver_id(String driver_id) {
		this.driver_id = driver_id;
	}
	public String getCar_number() {
		return car_number;
	}

	public JsonDriver(String driver_name, String driver_id, String car_number,
			String driver_report, String check_car, String phone,
			String driving_license_type, String driving_license, String photo,int load_maxnumber) {
		super();
		this.driver_name = driver_name;
		this.driver_id = driver_id;
		this.car_number = car_number;
		this.driver_report = driver_report;
		this.check_car = check_car;
		this.phone = phone;
		Driving_license_type = driving_license_type;
		Driving_license = driving_license;
		this.photo = photo;
		this.load_maxnumber=load_maxnumber;
	}
	@Override
	public String toString() {
		return "JsonDriver [driver_name=" + driver_name + ", driver_id="
				+ driver_id + ", car_number=" + car_number + ", driver_report="
				+ driver_report + ", check_car=" + check_car + ", phone="
				+ phone + ", Driving_license_type=" + Driving_license_type
				+ ", Driving_license=" + Driving_license + ", photo=" + photo
				+ "]";
	}
	public void setCar_number(String car_number) {
		this.car_number = car_number;
	}
	public String getDriver_report() {
		return driver_report;
	}
	public void setDriver_report(String driver_report) {
		this.driver_report = driver_report;
	}
	public String getCheck_car() {
		return check_car;
	}
	public void setCheck_car(String check_car) {
		this.check_car = check_car;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getDriving_license_type() {
		return Driving_license_type;
	}
	public void setDriving_license_type(String driving_license_type) {
		Driving_license_type = driving_license_type;
	}
	public String getDriving_license() {
		return Driving_license;
	}
	public void setDriving_license(String driving_license) {
		Driving_license = driving_license;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}

}

