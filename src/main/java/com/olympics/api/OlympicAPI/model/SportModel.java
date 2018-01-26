package com.olympics.api.OlympicAPI.model;

public class SportModel {

	private Integer id;
	private String sport;
	
	public Integer getId() {
		return id == null ? -1 : id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSport() {
		return sport == null ? "" : sport;
	}
	public void setSport(String sport) {
		this.sport = sport;
	}
	
	@Override
	public String toString() {
		return getSport();
	}
}
