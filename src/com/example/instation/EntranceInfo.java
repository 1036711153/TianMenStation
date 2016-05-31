package com.example.instation;

import org.litepal.crud.DataSupport;

import android.R.integer;

public class EntranceInfo extends DataSupport{
	
	private int id;
	private String licnence;
	private String watchID;
	private int adult_num;
	private int child_num;
	private String upload_photo1_path;
	private String upload_photo2_path;
	private String time;
	private String stationcode;
	
	public String getStationcode() {
		return stationcode;
	}
	
	public void setStationcode(String stationcode) {
		this.stationcode = stationcode;
	}
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLicnence() {
		return licnence;
	}
	public void setLicnence(String licnence) {
		this.licnence = licnence;
	}
	public String getWatchID() {
		return watchID;
	}
	public void setWatchID(String watchID) {
		this.watchID = watchID;
	}
	public int getAdult_num() {
		return adult_num;
	}
	public void setAdult_num(int adult_num) {
		this.adult_num = adult_num;
	}
	public int getChild_num() {
		return child_num;
	}
	public void setChild_num(int child_num) {
		this.child_num = child_num;
	}
	public String getUpload_photo1_path() {
		return upload_photo1_path;
	}
	public void setUpload_photo1_path(String upload_photo1_path) {
		this.upload_photo1_path = upload_photo1_path;
	}
	public String getUpload_photo2_path() {
		return upload_photo2_path;
	}
	public void setUpload_photo2_path(String upload_photo2_path) {
		this.upload_photo2_path = upload_photo2_path;
	}
	@Override
	public String toString() {
		return "EntranceInfo [id=" + id + ", licnence=" + licnence
				+ ", watchID=" + watchID + ", adult_num=" + adult_num
				+ ", child_num=" + child_num + ", upload_photo1_path="
				+ upload_photo1_path + ", upload_photo2_path="
				+ upload_photo2_path + ", time=" + time + ", stationcode="
				+ stationcode + "]";
	}
}
