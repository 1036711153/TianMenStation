package com.example.anjian;

import java.io.Serializable;
import java.util.List;

import org.litepal.crud.DataSupport;

public class InspectionSubitem extends DataSupport implements Serializable{
   private int id;
   private String Name;
   private String Description;
   private String Method;
   private InspectionItem inspectionItem;
   private String defect_description;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
@Override
public String toString() {
	return "InspectionSubitem [id=" + id + ", Name=" + Name + ", Description="
			+ Description + ", Method=" + Method + ", inspectionItem="
			+ inspectionItem + "]";
}
public String getName() {
	return Name;
}
public void setName(String name) {
	Name = name;
}
public String getDescription() {
	return Description;
}
public void setDescription(String description) {
	Description = description;
}
public String getMethod() {
	return Method;
}
public void setMethod(String method) {
	Method = method;
}
public InspectionItem getInspectionItem() {
	return inspectionItem;
}
public void setInspectionItem(InspectionItem inspectionItem) {
	this.inspectionItem = inspectionItem;
}
public String getDefect_description() {
	return defect_description;
}
public void setDefect_description(String defect_description) {
	this.defect_description = defect_description;
}

}
