package com.example.outstation.entity;

public class OnOutOfSationCar {
	private String LicencePlate;
	private String status;
	public String getLicencePlate() {
		return LicencePlate;
	}
	public void setLicencePlate(String licencePlate) {
		LicencePlate = licencePlate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public OnOutOfSationCar(String licencePlate, String status) {
		super();
		LicencePlate = licencePlate;
		this.status = status;
	}
	@Override
	public String toString() {
		return  LicencePlate + ","+ status;
	}
	

}
