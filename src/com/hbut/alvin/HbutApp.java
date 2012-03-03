package com.hbut.alvin;

import java.util.List;
import java.util.Map;

import com.hbut.util.ClsStuSbj;
import com.hbut.util.PersonInf;
import com.hbut.util.PersonSbj;

import android.app.Application;

public class HbutApp extends Application {


	public boolean isClsDownloadEnd() {
		return isClsDownloadEnd;
	}

	public void setClsDownloadEnd(boolean isClsDownloadEnd) {
		this.isClsDownloadEnd = isClsDownloadEnd;
	}

	public Map<String, List<ClsStuSbj>> getClsSbj() {
		return clsSbj;
	}

	public void setClsSbj(Map<String, List<ClsStuSbj>> clsSbj) {
		this.clsSbj = clsSbj;
	}

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
	private Map<String,List<ClsStuSbj>> clsSbj;
	boolean isClsDownloadEnd = false;
}