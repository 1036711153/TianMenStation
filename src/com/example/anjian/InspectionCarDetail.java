package com.example.anjian;

import org.litepal.crud.DataSupport;

public class InspectionCarDetail extends DataSupport{
	private int id;
	private String InspectionSubitem_name;
	private int Conclusion;
	private String ImageURL;
	private String Zhuxiangmu_name;
	private String Defect_Description;
	private InspectionCarSummary inspectionCarSummary;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getInspectionSubitem_name() {
		return InspectionSubitem_name;
	}
	public void setInspectionSubitem_name(String inspectionSubitem_name) {
		InspectionSubitem_name = inspectionSubitem_name;
	}
	public int getConclusion() {
		return Conclusion;
	}
	public void setConclusion(int conclusion) {
		Conclusion = conclusion;
	}
	public String getImageURL() {
		return ImageURL;
	}
	public void setImageURL(String imageURL) {
		ImageURL = imageURL;
	}
	public String getZhuxiangmu_name() {
		return Zhuxiangmu_name;
	}
	public void setZhuxiangmu_name(String zhuxiangmu_name) {
		Zhuxiangmu_name = zhuxiangmu_name;
	}
	public String getDefect_Description() {
		return Defect_Description;
	}
	public void setDefect_Description(String defect_Description) {
		Defect_Description = defect_Description;
	}
	public InspectionCarSummary getInspectionCarSummary() {
		return inspectionCarSummary;
	}
	public void setInspectionCarSummary(InspectionCarSummary inspectionCarSummary) {
		this.inspectionCarSummary = inspectionCarSummary;
	}
	@Override
	public String toString() {
		return "InspectionCarDetail [id=" + id + ", InspectionSubitem_name="
				+ InspectionSubitem_name + ", Conclusion=" + Conclusion
				+ ", ImageURL=" + ImageURL + ", Zhuxiangmu_name="
				+ Zhuxiangmu_name + ", Defect_Description="
				+ Defect_Description + ", inspectionCarSummary="
				+ inspectionCarSummary + "]";
	}
	

}
