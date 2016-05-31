package com.example.instation;

import org.litepal.crud.DataSupport;

public class mAdmin extends DataSupport{
	private String madmin;

	public String getMadmin() {
		return madmin;
	}

	public void setMadmin(String madmin) {
		this.madmin = madmin;
	}

	@Override
	public String toString() {
		return "mAdmin [madmin=" + madmin + "]";
	}
}
