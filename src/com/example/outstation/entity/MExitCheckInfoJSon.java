package com.example.outstation.entity;

import java.io.Serializable;


public class MExitCheckInfoJSon implements Serializable{
	
	private String firstdriver_id;
	private String secdriver_id;
	
	private String licenceplate;
	
	private String firstname;
	private String secname;
	
	private String firstfingerprintcode;
	private String secfingerprintcode;
	
	private String firsttel;
	private String sectel;
	
	private String vehicle_id;
	private String vehicletype;
	
	
	private String  firstdriverlicid;
	private String  secdriverlicid;
	
	
	private Integer accurateloadnum;
	private String inspectionstatus;
	private String classreportstatu;
	
	private String firstidcard;
	private String secidcard; 
	
	private String firstimg;
	private String secimg;
	
	public String getFirstdriver_id() {
		return firstdriver_id;
	}
	public void setFirstdriver_id(String firstdriver_id) {
		this.firstdriver_id = firstdriver_id;
	}
	public String getSecdriver_id() {
		return secdriver_id;
	}
	public void setSecdriver_id(String secdriver_id) {
		this.secdriver_id = secdriver_id;
	}
	public String getLicenceplate() {
		return licenceplate;
	}
	public void setLicenceplate(String licenceplate) {
		this.licenceplate = licenceplate;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getSecname() {
		return secname;
	}
	public void setSecname(String secname) {
		this.secname = secname;
	}
	
	public String getFirstfingerprintcode() {
		return firstfingerprintcode;
	}
	public void setFirstfingerprintcode(String firstfingerprintcode) {
		this.firstfingerprintcode = firstfingerprintcode;
	}
	public String getSecfingerprintcode() {
		return secfingerprintcode;
	}
	public void setSecfingerprintcode(String secfingerprintcode) {
		this.secfingerprintcode = secfingerprintcode;
	}
	public String getFirsttel() {
		return firsttel;
	}
	public void setFirsttel(String firsttel) {
		this.firsttel = firsttel;
	}
	public String getSectel() {
		return sectel;
	}
	public void setSectel(String sectel) {
		this.sectel = sectel;
	}
	public String getVehicle_id() {
		return vehicle_id;
	}
	public void setVehicle_id(String vehicle_id) {
		this.vehicle_id = vehicle_id;
	}
	public String getVehicletype() {
		return vehicletype;
	}
	public void setVehicletype(String vehicletype) {
		this.vehicletype = vehicletype;
	}
	public String getFirstdriverlicid() {
		return firstdriverlicid;
	}
	public void setFirstdriverlicid(String firstdriverlicid) {
		this.firstdriverlicid = firstdriverlicid;
	}
	public String getSecdriverlicid() {
		return secdriverlicid;
	}
	public void setSecdriverlicid(String secdriverlicid) {
		this.secdriverlicid = secdriverlicid;
	}
	public Integer getAccurateloadnum() {
		return accurateloadnum;
	}
	public void setAccurateloadnum(Integer accurateloadnum) {
		this.accurateloadnum = accurateloadnum;
	}
	public String getInspectionstatus() {
		return inspectionstatus;
	}
	public void setInspectionstatus(String inspectionstatus) {
		this.inspectionstatus = inspectionstatus;
	}
	public String getClassreportstatu() {
		return classreportstatu;
	}
	public void setClassreportstatu(String classreportstatu) {
		this.classreportstatu = classreportstatu;
	}
	
	public MExitCheckInfoJSon() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MExitCheckInfoJSon(String firstdriver_id, String secdriver_id,
			String licenceplate, String firstname, String secname,
			String firstfingerprintcode, String secfingerprintcode,
			String firsttel, String sectel, String vehicle_id,
			String vehicletype, String firstdriverlicid, String secdriverlicid,
			Integer accurateloadnum, String inspectionstatus,
			String classreportstatu) {
		super();
		this.firstdriver_id = firstdriver_id;
		this.secdriver_id = secdriver_id;
		this.licenceplate = licenceplate;
		this.firstname = firstname;
		this.secname = secname;
		this.firstfingerprintcode = firstfingerprintcode;
		this.secfingerprintcode = secfingerprintcode;
		this.firsttel = firsttel;
		this.sectel = sectel;
		this.vehicle_id = vehicle_id;
		this.vehicletype = vehicletype;
		this.firstdriverlicid = firstdriverlicid;
		this.secdriverlicid = secdriverlicid;
		this.accurateloadnum = accurateloadnum;
		this.inspectionstatus = inspectionstatus;
		this.classreportstatu = classreportstatu;
	}
	@Override
	public String toString() {
		return "ExitCkeckInfo [firstdriver_id=" + firstdriver_id
				+ ", secdriver_id=" + secdriver_id + ", licenceplate="
				+ licenceplate + ", firstname=" + firstname + ", secname="
				+ secname + ", fingerprintcode=" + firstfingerprintcode
				+ ", secfingerprintcode=" + secfingerprintcode + ", firsttel="
				+ firsttel + ", sectel=" + sectel + ", vehicle_id="
				+ vehicle_id + ", vehicletype=" + vehicletype
				+ ", firstdriverlicid=" + firstdriverlicid
				+ ", secdriverlicid=" + secdriverlicid + ", accurateloadnum="
				+ accurateloadnum + ", inspectionstatus=" + inspectionstatus
				+ ", classreportstatu=" + classreportstatu + "]";
	}
	public String getFirstidcard() {
		return firstidcard;
	}
	public void setFirstidcard(String firstidcard) {
		this.firstidcard = firstidcard;
	}
	public String getSecidcard() {
		return secidcard;
	}
	public void setSecidcard(String secidcard) {
		this.secidcard = secidcard;
	}
	public String getFirstimg() {
		return firstimg;
	}
	public void setFirstimg(String firstimg) {
		this.firstimg = firstimg;
	}
	public String getSecimg() {
		return secimg;
	}
	public void setSecimg(String secimg) {
		this.secimg = secimg;
	}
	
	
	
}
