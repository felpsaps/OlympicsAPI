package com.olympics.api.OlympicAPI.model;

public class StepModel {

	private Integer id;
	private String step;
	
	public Integer getId() {
		return id == null ? -1 : id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStep() {
		return step == null ? "" : step;
	}
	public void setStep(String step) {
		this.step = step;
	}
}
