package com.example.Utils;

import java.io.Serializable;


public class ExitCheckInfoJson implements Serializable{
	//两个驾驶员的ID
	private String firDriverId;
	private String secDriverId;
	//两个驾驶员的名字
	private String firName;
	private String secName;
	//两个驾驶员的指纹编号
	private String firFingerCode1;
	private String firFingerCode2;
	private String firFingerCode3;
	private String firFingerCode4;
	
	private String secFingerCode1;
	private String secFingerCode2;
	private String secFingerCode3;
	private String secFingerCode4;
	//两个驾驶员的电话
	private String firTel;
	private String secTel;
	//车辆的ID
	private String vehicleId;
	//车辆的性质
	private String vehicleType;
	//两个驾驶员的驾驶证号
	private String firAdultNum;
	private String secAdultNum;
	//两个驾驶员的身份证号
	private String firIdCard;
	private String secIdCard;
	//两个驾驶员的头像
	private String firPhoto;
	private String secPhoto;
	//准载人数
	private String accurateLoadNum;
	//安检状态
	private String inspectionStatus;
	//报班状态
	private String classReportStatus;
	//车牌号
	private String licencePlate;
	public String getFirDriverId() {
		return firDriverId;
	}
	public void setFirDriverId(String firDriverId) {
		this.firDriverId = firDriverId;
	}
	public String getSecDriverId() {
		return secDriverId;
	}
	public void setSecDriverId(String secDriverId) {
		this.secDriverId = secDriverId;
	}
	public String getFirName() {
		return firName;
	}
	public void setFirName(String firName) {
		this.firName = firName;
	}
	public String getSecName() {
		return secName;
	}
	public void setSecName(String secName) {
		this.secName = secName;
	}
	public String getFirTel() {
		return firTel;
	}
	public void setFirTel(String firTel) {
		this.firTel = firTel;
	}
	public String getSecTel() {
		return secTel;
	}
	public void setSecTel(String secTel) {
		this.secTel = secTel;
	}
	public String getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public String getFirAdultNum() {
		return firAdultNum;
	}
	public void setFirAdultNum(String firAdultNum) {
		this.firAdultNum = firAdultNum;
	}
	public String getSecAdultNum() {
		return secAdultNum;
	}
	public void setSecAdultNum(String secAdultNum) {
		this.secAdultNum = secAdultNum;
	}
	public String getFirIdCard() {
		return firIdCard;
	}
	public void setFirIdCard(String firIdCard) {
		this.firIdCard = firIdCard;
	}
	public String getSecIdCard() {
		return secIdCard;
	}
	public void setSecIdCard(String secIdCard) {
		this.secIdCard = secIdCard;
	}
	public String getFirPhoto() {
		return firPhoto;
	}
	public void setFirPhoto(String firPhoto) {
		this.firPhoto = firPhoto;
	}
	public String getSecPhoto() {
		return secPhoto;
	}
	public void setSecPhoto(String secPhoto) {
		this.secPhoto = secPhoto;
	}
	public String getAccurateLoadNum() {
		return accurateLoadNum;
	}
	public void setAccurateLoadNum(String accurateLoadNum) {
		this.accurateLoadNum = accurateLoadNum;
	}
	public String getInspectionStatus() {
		return inspectionStatus;
	}
	public void setInspectionStatus(String inspectionStatus) {
		this.inspectionStatus = inspectionStatus;
	}
	public String getClassReportStatus() {
		return classReportStatus;
	}
	public void setClassReportStatus(String classReportStatus) {
		this.classReportStatus = classReportStatus;
	}
	public String getLicencePlate() {
		return licencePlate;
	}
	public void setLicencePlate(String licencePlate) {
		this.licencePlate = licencePlate;
	}
	public String getFirFingerCode1() {
		return firFingerCode1;
	}
	public void setFirFingerCode1(String firFingerCode1) {
		this.firFingerCode1 = firFingerCode1;
	}
	public String getFirFingerCode2() {
		return firFingerCode2;
	}
	public void setFirFingerCode2(String firFingerCode2) {
		this.firFingerCode2 = firFingerCode2;
	}
	public String getFirFingerCode3() {
		return firFingerCode3;
	}
	public void setFirFingerCode3(String firFingerCode3) {
		this.firFingerCode3 = firFingerCode3;
	}
	public String getFirFingerCode4() {
		return firFingerCode4;
	}
	public void setFirFingerCode4(String firFingerCode4) {
		this.firFingerCode4 = firFingerCode4;
	}
	public String getSecFingerCode1() {
		return secFingerCode1;
	}
	public void setSecFingerCode1(String secFingerCode1) {
		this.secFingerCode1 = secFingerCode1;
	}
	public String getSecFingerCode2() {
		return secFingerCode2;
	}
	public void setSecFingerCode2(String secFingerCode2) {
		this.secFingerCode2 = secFingerCode2;
	}
	public String getSecFingerCode3() {
		return secFingerCode3;
	}
	public void setSecFingerCode3(String secFingerCode3) {
		this.secFingerCode3 = secFingerCode3;
	}
	public String getSecFingerCode4() {
		return secFingerCode4;
	}
	public void setSecFingerCode4(String secFingerCode4) {
		this.secFingerCode4 = secFingerCode4;
	}
	@Override
	public String toString() {
		return "ExitCheckInfoJson [firDriverId=" + firDriverId
				+ ", secDriverId=" + secDriverId + ", firName=" + firName
				+ ", secName=" + secName + ", firFingerCode1=" + firFingerCode1
				+ ", firFingerCode2=" + firFingerCode2 + ", firFingerCode3="
				+ firFingerCode3 + ", firFingerCode4=" + firFingerCode4
				+ ", secFingerCode1=" + secFingerCode1 + ", secFingerCode2="
				+ secFingerCode2 + ", secFingerCode3=" + secFingerCode3
				+ ", secFingerCode4=" + secFingerCode4 + ", firTel=" + firTel
				+ ", secTel=" + secTel + ", vehicleId=" + vehicleId
				+ ", vehicleType=" + vehicleType + ", firAdultNum="
				+ firAdultNum + ", secAdultNum=" + secAdultNum + ", firIdCard="
				+ firIdCard + ", secIdCard=" + secIdCard + ", firPhoto="
				+ firPhoto + ", secPhoto=" + secPhoto + ", accurateLoadNum="
				+ accurateLoadNum + ", inspectionStatus=" + inspectionStatus
				+ ", classReportStatus=" + classReportStatus
				+ ", licencePlate=" + licencePlate + "]";
	}
}
