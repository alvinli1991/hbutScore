package com.hbut.util;

import java.util.ArrayList;

public class PersonCombine {
	public ArrayList<PersonSbj> getpSbjList() {
		return pSbjList;
	}
	public void setpSbjList(ArrayList<PersonSbj> pSbjList) {
		this.pSbjList = pSbjList;
	}
	public PersonInf getPi() {
		return pi;
	}
	public void setPi(PersonInf pi) {
		this.pi = pi;
	}
	private ArrayList<PersonSbj> pSbjList = null;
	private PersonInf pi = null;
	
	
}
