package com.example.anjian.synchroscope;

import java.util.List;

public class InspectionSubitem {
	private String Name;
	private String Description;
	private String Method;
	private List<Defect_description> defect_descriptions;
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
	public List<Defect_description> getDefect_descriptions() {
		return defect_descriptions;
	}
	public void setDefect_descriptions(List<Defect_description> defect_descriptions) {
		this.defect_descriptions = defect_descriptions;
	}
	
	public InspectionSubitem(String name, String description, String method,
			List<Defect_description> defect_descriptions) {
		super();
		Name = name;
		Description = description;
		Method = method;
		this.defect_descriptions = defect_descriptions;
	}

}
