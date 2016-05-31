package com.example.anjian;

import java.util.ArrayList;
import java.util.List;

import org.litepal.crud.DataSupport;
import org.w3c.dom.Comment;

public class LinshiInspectionCarSummary  {

	private String Vehicle_ID;
	private String Inspector_ID;
	private String DateTime;
	private String OutsideImgURL;
	private int Conclusion;
	private String InsideImgURL;
	private String CheckImg;
	private List<InspectionCarDetail> inspectionCarDetails = new ArrayList<InspectionCarDetail>();
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
		return  "Vehicle_ID=" + Vehicle_ID
				+ ", Inspector_ID=" + Inspector_ID + ", DateTime=" + DateTime
				+ ", OutsideImgURL=" + OutsideImgURL + ", Conclusion="
				+ Conclusion + ", InsideImgURL=" + InsideImgURL + ", CheckImg="
				+ CheckImg + ",inspectionCarDetails=" + inspectionCarDetails;
	}
}
