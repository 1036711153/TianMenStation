package com.example.anjian;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.litepal.crud.DataSupport;

public class InspectionItem extends DataSupport implements Serializable{
	private int id;
	private String Cname;
	private String Description;
	private List<InspectionSubitem> inspectionSubitems=new ArrayList<InspectionSubitem>();
	
	public List<InspectionSubitem> getInspectionSubitems() {
		return inspectionSubitems;
	}
	public void setInspectionSubitems(List<InspectionSubitem> inspectionSubitems) {
		this.inspectionSubitems = inspectionSubitems;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
	@Override
	public String toString() {
		return "InspectionItem [id=" + id+ ", Cname=" + Cname + ", Description=" + Description
				+ ", inspectionSubitems="
				+ inspectionSubitems + "]";
	}
}
