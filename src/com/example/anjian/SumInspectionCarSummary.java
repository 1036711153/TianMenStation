package com.example.anjian;

import java.util.List;

public class SumInspectionCarSummary {
	private List<InspectionCarSummary>inspectionCarSummaries;

	public List<InspectionCarSummary> getInspectionCarSummaries() {
		return inspectionCarSummaries;
	}

	public void setInspectionCarSummaries(
			List<InspectionCarSummary> inspectionCarSummaries) {
		this.inspectionCarSummaries = inspectionCarSummaries;
	}

	public SumInspectionCarSummary(
			List<InspectionCarSummary> inspectionCarSummaries) {
		super();
		this.inspectionCarSummaries = inspectionCarSummaries;
	}

}
