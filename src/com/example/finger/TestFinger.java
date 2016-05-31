package com.example.finger;

import org.litepal.crud.DataSupport;

public class TestFinger extends DataSupport{
	private int id;
	private String mfinger;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMfinger() {
		return mfinger;
	}
	public void setMfinger(String mfinger) {
		this.mfinger = mfinger;
	}

}
