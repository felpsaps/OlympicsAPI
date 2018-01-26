package com.olympics.api.OlympicAPI.model;

public class CountryModel {

	private Integer id;
	private String country;
	
	public Integer getId() {
		return id == null ? -1 : id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCountry() {
		return country == null ? "" : country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
}
