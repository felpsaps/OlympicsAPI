package com.olympics.api.OlympicAPI.model;

public class LocalModel {

	private Integer id;
	private String local;
	
	public Integer getId() {
		return id == null ? -1 : id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLocal() {
		return local == null ? "" : local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
}
