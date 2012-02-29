package com.hbut.alvin;

import java.util.List;

import com.hbut.util.PersonInf;
import com.hbut.util.PersonSbj;

import android.app.Application;

public class HbutApp extends Application {

	public PersonInf getPsi() {
		return psi;
	}

	public void setPsi(PersonInf psi) {
		this.psi = psi;
	}

	public List<PersonSbj> getpSbjList() {
		return pSbjList;
	}

	public void setpSbjList(List<PersonSbj> pSbjList) {
		this.pSbjList = pSbjList;
	}

	private List<PersonSbj> pSbjList;
	private PersonInf psi;
}
