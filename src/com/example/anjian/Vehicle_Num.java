package com.example.anjian;

import org.litepal.crud.DataSupport;

public class Vehicle_Num extends DataSupport{
	private String car_num;

	public String getCar_num() {
		return car_num;
	}

	public void setCar_num(String car_num) {
		this.car_num = car_num;
	}

	@Override
	public String toString() {
		return "Vehicle_Num [car_num=" + car_num + "]";
	}
}
