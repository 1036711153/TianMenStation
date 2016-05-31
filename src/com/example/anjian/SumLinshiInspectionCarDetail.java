package com.example.anjian;

import java.io.Serializable;
import java.util.List;

public class SumLinshiInspectionCarDetail implements Serializable{
	private List<LinshiInspectionCarDetail> linshiInspectionCarDetails;
	public List<LinshiInspectionCarDetail> getLinshiInspectionCarDetails() {
		return linshiInspectionCarDetails;
	}

	public void setLinshiInspectionCarDetails(
			List<LinshiInspectionCarDetail> linshiInspectionCarDetails) {
		this.linshiInspectionCarDetails = linshiInspectionCarDetails;
	}

	public SumLinshiInspectionCarDetail(List<LinshiInspectionCarDetail> linshiInspectionCarDetails) {
		super();
		this.linshiInspectionCarDetails = linshiInspectionCarDetails;
	}

	@Override
	public String toString() {
		return "SumLinshiInspectionCarDetail [linshiInspectionCarDetails="
				+ linshiInspectionCarDetails + "]";
	}
	
	
	
	
	

}
