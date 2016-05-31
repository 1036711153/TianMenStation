package com.example.anjian.synchroscope;

import java.util.ArrayList;
import java.util.List;

public class InspectionItem {
	private String Cname;
	private String Description;
	private List<InspectionSubitem> inspectionSubitems;
	public String getCname() {
		return Cname;
	}
	public void setCname(String cname) {
		Cname = cname;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public List<InspectionSubitem> getInspectionSubitems() {
		return inspectionSubitems;
	}
	public void setInspectionSubitems(List<InspectionSubitem> inspectionSubitems) {
		this.inspectionSubitems = inspectionSubitems;
	}
	public InspectionItem(String cname,
			List<InspectionSubitem> inspectionSubitems) {
		super();
		Cname = cname;
		this.inspectionSubitems = inspectionSubitems;
	}
	
}
