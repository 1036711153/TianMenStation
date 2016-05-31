package com.example.Utils;

import java.util.List;

import org.litepal.crud.DataSupport;

import com.authentication.entity.AutoCarNum;

public class DataBaseFindData {
	
	public List<AutoCarNum> getAutoCarNum() {
		List<AutoCarNum> mAutoCarNums=DataSupport.findAll(AutoCarNum.class);
		return mAutoCarNums;
	}
	
	

}
