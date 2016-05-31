package com.example.anjian;

import java.io.Serializable;
import java.util.List;

public class SumDefect_description implements Serializable {
	private List<Defect_description>defect_descriptions;

	public List<Defect_description> getDefect_descriptions() {
		return defect_descriptions;
	}

	public void setDefect_descriptions(List<Defect_description> defect_descriptions) {
		this.defect_descriptions = defect_descriptions;
	}

	public SumDefect_description(List<Defect_description> defect_descriptions) {
		super();
		this.defect_descriptions = defect_descriptions;
	}

	@Override
	public String toString() {
		return "SumDefect_description [defect_descriptions="
				+ defect_descriptions + "]";
	}
	
	
}
