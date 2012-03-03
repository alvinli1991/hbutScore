package com.hbut.util;

import java.util.regex.*;

public class PersonInf {



	public int getSbjCount() {
		return sbjCount;
	}

	public void setSbjCount(int sbjNum) {
		this.sbjCount = sbjNum;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getCls() {
		return cls;
	}

	public void setCls(String cls) {
		this.cls = cls;
	}

	
	private String name;
	private String pwd;
	private String cls;
	private String ID;
	private int sbjCount;
	
	public static boolean checkID(String id) {
		Pattern p = Pattern.compile("^[a-zA-z0-9]+$");
		Matcher m = p.matcher(id);
		if (m.find())
			return true;
		else
			return false;
	}

	public static String getCidByID(String id) {
		Pattern p = Pattern.compile("\\d{7}");
		Matcher m = p.matcher(id);
		if (m.find())
			return m.group();
		else
			return null;
	}
}
