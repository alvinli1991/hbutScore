package com.hbut.util;

public class VersionInf {
	public String[] getElement() {
		return element;
	}
	public void setElement(String[] element) {
		this.element = element;
	}
	public String getAllow() {
		return allow;
	}
	public void setAllow(String allow) {
		this.allow = allow;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

	String version;
	int year;
	int month;
	int day;
	String allow;
	String[] element; 
	
}
