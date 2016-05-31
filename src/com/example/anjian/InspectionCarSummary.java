package com.example.anjian;

import java.util.ArrayList;
import java.util.List;

import org.litepal.crud.DataSupport;
import org.w3c.dom.Comment;

public class InspectionCarSummary extends DataSupport {

	private int id;
	private String Vehicle_ID;
	private String Inspector_ID;
	private String DateTime;
	private String OutsideImgURL;
	private int Conclusion;
	private String InsideImgURL;
	private String CheckImg;
	private String Checkcontent;
	private String image1;
	private String image2;
	private String image3;
	private String image4;
	private String image5;
	private String image6;
	private String image7;
	private String image8;
	private String image9;
	private String stationcode;
	
	public String getStationcode() {
		return stationcode;
	}
	public void setStationcode(String stationcode) {
		this.stationcode = stationcode;
	}
	public String getImage1() {
		return image1;
	}
	public void setImage1(String image1) {
		this.image1 = image1;
	}
	public String getImage2() {
		return image2;
	}
	public void setImage2(String image2) {
		this.image2 = image2;
	}
	public String getImage3() {
		return image3;
	}
	public void setImage3(String image3) {
		this.image3 = image3;
	}
	public String getImage4() {
		return image4;
	}
	public void setImage4(String image4) {
		this.image4 = image4;
	}
	public String getImage5() {
		return image5;
	}
	public void setImage5(String image5) {
		this.image5 = image5;
	}
	public String getImage6() {
		return image6;
	}
	public void setImage6(String image6) {
		this.image6 = image6;
	}
	public String getImage7() {
		return image7;
	}
	public void setImage7(String image7) {
		this.image7 = image7;
	}
	public String getImage8() {
		return image8;
	}
	public void setImage8(String image8) {
		this.image8 = image8;
	}
	public String getImage9() {
		return image9;
	}
	public void setImage9(String image9) {
		this.image9 = image9;
	}
	
	
	public String getCheckcontent() {
		return Checkcontent;
	}
	public void setCheckcontent(String checkcontent) {
		Checkcontent = checkcontent;
	}
	private List<InspectionCarDetail> inspectionCarDetails = new ArrayList<InspectionCarDetail>();
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getVehicle_ID() {
		return Vehicle_ID;
	}
	public void setVehicle_ID(String vehicle_ID) {
		Vehicle_ID = vehicle_ID;
	}
	public String getInspector_ID() {
		return Inspector_ID;
	}
	public void setInspector_ID(String inspector_ID) {
		Inspector_ID = inspector_ID;
	}
	public String getDateTime() {
		return DateTime;
	}
	public void setDateTime(String dateTime) {
		DateTime = dateTime;
	}
	public String getOutsideImgURL() {
		return OutsideImgURL;
	}
	public void setOutsideImgURL(String outsideImgURL) {
		OutsideImgURL = outsideImgURL;
	}
	public int getConclusion() {
		return Conclusion;
	}
	public void setConclusion(int conclusion) {
		Conclusion = conclusion;
	}
	public String getInsideImgURL() {
		return InsideImgURL;
	}
	public void setInsideImgURL(String insideImgURL) {
		InsideImgURL = insideImgURL;
	}
	public String getCheckImg() {
		return CheckImg;
	}
	public void setCheckImg(String checkImg) {
		CheckImg = checkImg;
	}
	public List<InspectionCarDetail> getInspectionCarDetails() {
		return inspectionCarDetails;
	}
	public void setInspectionCarDetails(
			List<InspectionCarDetail> inspectionCarDetails) {
		this.inspectionCarDetails = inspectionCarDetails;
	}
	@Override
	public String toString() {
		return "InspectionCarSummary [id=" + id + ", Vehicle_ID=" + Vehicle_ID
				+ ", Inspector_ID=" + Inspector_ID + ", DateTime=" + DateTime
				+ ", OutsideImgURL=" + OutsideImgURL + ", Conclusion="
				+ Conclusion + ", InsideImgURL=" + InsideImgURL + ", CheckImg="
				+ CheckImg + ", Checkcontent=" + Checkcontent + ", image1="
				+ image1 + ", image2=" + image2 + ", image3=" + image3
				+ ", image4=" + image4 + ", image5=" + image5 + ", image6="
				+ image6 + ", image7=" + image7 + ", image8=" + image8
				+ ", image9=" + image9 + ", stationcode=" + stationcode
				+ ", inspectionCarDetails=" + inspectionCarDetails + "]";
	}
}
