package com.example.configure;

import org.litepal.crud.DataSupport;

import android.R.integer;

public class Config_DataDase extends DataSupport{
	private int id;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String IP;

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}
}
