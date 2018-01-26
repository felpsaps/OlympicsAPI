package com.olympics.api.OlympicAPI.model;

public class ServerMessageModel {

	private String type;
	private String msg;
	
	public String getType() {
		return type == null ? "" : type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMsg() {
		return msg == null ? "" : msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
