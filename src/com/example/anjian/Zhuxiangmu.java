package com.example.anjian;

public class Zhuxiangmu {
	private String Cname;
	private String Description;
	
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

	
	public Zhuxiangmu(String cname, String description) {
		super();
		Cname = cname;
		Description = description;
	}

	@Override
	public String toString() {
		return "Zhuxiangmu [Cname=" + Cname + ", Description=" + Description
				+ "]";
	}
}
