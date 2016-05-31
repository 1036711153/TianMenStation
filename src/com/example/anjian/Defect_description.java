package com.example.anjian;

public class Defect_description {
	private String defect_description;

	public String getDefect_description() {
		return defect_description;
	}

	public void setDefect_description(String defect_description) {
		this.defect_description = defect_description;
	}

	public Defect_description(String defect_description) {
		super();
		this.defect_description = defect_description;
	}

	@Override
	public String toString() {
		return "Defect_description [defect_description=" + defect_description
				+ "]";
	}
	
}
