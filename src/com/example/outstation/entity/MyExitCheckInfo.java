package com.example.outstation.entity;

import java.io.Serializable;

import org.litepal.crud.DataSupport;


public class MyExitCheckInfo  implements Serializable {
	//两个驾驶员的ID
	private String FirDriver_ID;
	private String SecDriver_ID;
	//两个驾驶员的名字
	private String FirName;
	private String SecName;
	//两个驾驶员的指纹编号
	private String FirFingerprintCode;
	private String SecFingerprintCode;
	//两个驾驶员的电话
	private String FirTel;
	private String SecTel;
	//车辆的ID
	private String Vehicle_ID;
	//车辆的性质
	private String VehicleType;
	//两个驾驶员的驾驶证号
	private String FirAdultNum;
	private String SecAdultNum;
	//两个驾驶员的身份证号
	private String FirID_Card;
	private String SecID_Card;
	//两个驾驶员的头像
	private String Fir_Photo;
	private String Sec_Photo;
	//准载人数
	private String AccurateLoadNum;
	//安检状态
	private String InspectionStatus;
	//报班状态
	private String ClassReportStatus;
	//车牌号
	private String LicencePlate;
	public String getFirDriver_ID() {
		return FirDriver_ID;
	}
	public void setFirDriver_ID(String firDriver_ID) {
		FirDriver_ID = firDriver_ID;
	}
	public String getSecDriver_ID() {
		return SecDriver_ID;
	}
	public void setSecDriver_ID(String secDriver_ID) {
		SecDriver_ID = secDriver_ID;
	}
	public String getFirName() {
		return FirName;
	}
	public void setFirName(String firName) {
		FirName = firName;
	}
	public String getSecName() {
		return SecName;
	}
	public void setSecName(String secName) {
		SecName = secName;
	}
	public String getFirFingerprintCode() {
		return FirFingerprintCode;
	}
	public void setFirFingerprintCode(String firFingerprintCode) {
		FirFingerprintCode = firFingerprintCode;
	}
	public String getSecFingerprintCode() {
		return SecFingerprintCode;
	}
	public void setSecFingerprintCode(String secFingerprintCode) {
		SecFingerprintCode = secFingerprintCode;
	}
	public String getFirTel() {
		return FirTel;
	}
	public void setFirTel(String firTel) {
		FirTel = firTel;
	}
	public String getSecTel() {
		return SecTel;
	}
	public void setSecTel(String secTel) {
		SecTel = secTel;
	}
	public String getVehicle_ID() {
		return Vehicle_ID;
	}
	public void setVehicle_ID(String vehicle_ID) {
		Vehicle_ID = vehicle_ID;
	}
	public String getVehicleType() {
		return VehicleType;
	}
	public void setVehicleType(String vehicleType) {
		VehicleType = vehicleType;
	}
	public String getFirAdultNum() {
		return FirAdultNum;
	}
	public void setFirAdultNum(String firAdultNum) {
		FirAdultNum = firAdultNum;
	}
	public String getSecAdultNum() {
		return SecAdultNum;
	}
	public void setSecAdultNum(String secAdultNum) {
		SecAdultNum = secAdultNum;
	}
	public String getFirID_Card() {
		return FirID_Card;
	}
	public void setFirID_Card(String firID_Card) {
		FirID_Card = firID_Card;
	}
	public String getSecID_Card() {
		return SecID_Card;
	}
	public void setSecID_Card(String secID_Card) {
		SecID_Card = secID_Card;
	}
	public String getFir_Photo() {
		return Fir_Photo;
	}
	public void setFir_Photo(String fir_Photo) {
		Fir_Photo = fir_Photo;
	}
	public String getSec_Photo() {
		return Sec_Photo;
	}
	public void setSec_Photo(String sec_Photo) {
		Sec_Photo = sec_Photo;
	}
	public String getAccurateLoadNum() {
		return AccurateLoadNum;
	}
	public void setAccurateLoadNum(String accurateLoadNum) {
		AccurateLoadNum = accurateLoadNum;
	}
	public String getInspectionStatus() {
		return InspectionStatus;
	}
	public void setInspectionStatus(String inspectionStatus) {
		InspectionStatus = inspectionStatus;
	}
	public String getClassReportStatus() {
		return ClassReportStatus;
	}
	public void setClassReportStatus(String classReportStatus) {
		ClassReportStatus = classReportStatus;
	}
	public String getLicencePlate() {
		return LicencePlate;
	}
	public void setLicencePlate(String licencePlate) {
		LicencePlate = licencePlate;
	}
	@Override
	public String toString() {
		return "ExitCheckInfo [FirDriver_ID=" + FirDriver_ID
				+ ", SecDriver_ID=" + SecDriver_ID + ", FirName=" + FirName
				+ ", SecName=" + SecName + ", FirFingerprintCode="
				+ FirFingerprintCode + ", SecFingerprintCode="
				+ SecFingerprintCode + ", FirTel=" + FirTel + ", SecTel="
				+ SecTel + ", Vehicle_ID=" + Vehicle_ID + ", VehicleType="
				+ VehicleType + ", FirAdultNum=" + FirAdultNum
				+ ", SecAdultNum=" + SecAdultNum + ", FirID_Card=" + FirID_Card
				+ ", SecID_Card=" + SecID_Card + ", Fir_Photo=" + Fir_Photo
				+ ", Sec_Photo=" + Sec_Photo + ", AccurateLoadNum="
				+ AccurateLoadNum + ", InspectionStatus=" + InspectionStatus
				+ ", ClassReportStatus=" + ClassReportStatus
				+ ", LicencePlate=" + LicencePlate + "]";
	}
	public MyExitCheckInfo(String firDriver_ID, String secDriver_ID,
			String firName, String secName, String firFingerprintCode,
			String secFingerprintCode, String firTel, String secTel,
			String vehicle_ID, String vehicleType, String firAdultNum,
			String secAdultNum, String firID_Card, String secID_Card,
			String fir_Photo, String sec_Photo, String accurateLoadNum,
			String inspectionStatus, String classReportStatus,
			String licencePlate) {
		super();
		FirDriver_ID = firDriver_ID;
		SecDriver_ID = secDriver_ID;
		FirName = firName;
		SecName = secName;
		FirFingerprintCode = firFingerprintCode;
		SecFingerprintCode = secFingerprintCode;
		FirTel = firTel;
		SecTel = secTel;
		Vehicle_ID = vehicle_ID;
		VehicleType = vehicleType;
		FirAdultNum = firAdultNum;
		SecAdultNum = secAdultNum;
		FirID_Card = firID_Card;
		SecID_Card = secID_Card;
		Fir_Photo = fir_Photo;
		Sec_Photo = sec_Photo;
		AccurateLoadNum = accurateLoadNum;
		InspectionStatus = inspectionStatus;
		ClassReportStatus = classReportStatus;
		LicencePlate = licencePlate;
	}
}
