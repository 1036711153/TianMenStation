package com.example.anjian.synchroscope;

import java.util.List;

public class Anjian {
	private List<InspectionItem> inspectionItems;

	public List<InspectionItem> getInspectionItems() {
		return inspectionItems;
	}

	public void setInspectionItems(List<InspectionItem> inspectionItems) {
		this.inspectionItems = inspectionItems;
	}

	public Anjian(List<InspectionItem> inspectionItems) {
		super();
		this.inspectionItems = inspectionItems;
	}
}
