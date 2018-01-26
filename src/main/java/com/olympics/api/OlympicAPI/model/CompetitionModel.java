package com.olympics.api.OlympicAPI.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class CompetitionModel {

	private Integer id;
	private Timestamp iniDate;
	private Timestamp endDate;
	private CountryModel country1;
	private CountryModel country2;
	private StepModel step;
	private LocalModel local;
	private SportModel sport;
	
	public String getSIniDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date(iniDate.getTime()));
	}
	
	public Integer getId() {
		return id == null ? -1 : id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Timestamp getIniDate() {
		return iniDate;
	}
	public void setIniDate(Timestamp iniDate) {
		this.iniDate = iniDate;
	}
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	public CountryModel getCountry1() {
		return country1 == null ? (country1 = new CountryModel()) : country1;
	}
	public void setCountry1(CountryModel country1) {
		this.country1 = country1;
	}
	public CountryModel getCountry2() {
		return country2 == null ? (country2 = new CountryModel()) : country2;
	}
	public void setCountry2(CountryModel country2) {
		this.country2 = country2;
	}
	public StepModel getStep() {
		return step == null ? (step = new StepModel()) : step;
	}
	public void setStep(StepModel step) {
		this.step = step;
	}
	public LocalModel getLocal() {
		return local == null ? (local = new LocalModel()) : local;
	}
	public void setLocal(LocalModel local) {
		this.local = local;
	}
	public SportModel getSport() {
		return sport == null ? (sport = new SportModel()) : sport;
	}
	public void setSport(SportModel sport) {
		this.sport = sport;
	}
}
