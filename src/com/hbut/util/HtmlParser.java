package com.hbut.util;

import java.util.ArrayList;
import java.util.regex.*;

public class HtmlParser {

	/*
	 * get personal info from the file downloaded and contains these
	 */
	public static PersonInf parserPsInfo(String personDoc) {
		PersonInf pi = new PersonInf();
		Pattern p = Pattern.compile("<TD Nowrap >[^<]*</TD>");
		String[] result = new String[3];
		Matcher m = p.matcher(personDoc);
		for (int i = 0; i < result.length; i++) {
			if (!m.find())
				return null;
			else
				result[i] = m.group();
		}

		pi.setPwd(result[0].split(">")[1].split("<")[0]);
		// m = Pattern.compile("(\\d)+").matcher(result[0]);
		// if (m.find())
		// pi.setPwd(m.group());
		// else
		// return null;

		pi.setCls(result[1].split(">")[1].split("<")[0]);
		// m = Pattern.compile("(\\d)+(.)+[^(</TD>)]").matcher(result[1]);
		// if (m.find())
		// pi.setCls(m.group());
		// else
		// return null;
		pi.setName(result[2].split(">")[1].split("<")[0]);
		// m =
		// Pattern.compile("[^(<TD Nowrap >)](.)+[^(</TD>)]").matcher(result[2]);
		// if (m.find())
		// pi.setName(m.group());
		// else
		// return null;

		return pi;
	}

	public static ArrayList<PersonSbj> parserPsSbj(String pSbjDoc) {
		ArrayList<PersonSbj> pSbjList = new ArrayList<PersonSbj>();
		PersonSbj pSbj = new PersonSbj();
		Pattern p = Pattern.compile("<TD Nowrap >[^<]*</TD>");
		Matcher m = p.matcher(pSbjDoc);
		int count = 0;
		while (m.find()) {
			switch (count) {
			case 0:
				pSbj.setSbjID(m.group().split(">")[1].split("<")[0]);
				break;
			case 1:
				pSbj.setSbjName(m.group().split(">")[1].split("<")[0]);
				break;
			case 2:
				// String temp = m.group().split(">")[1].split("<")[0];
				// if(temp.matches("[0-9]+")){}
				int grade = Integer
						.parseInt(m.group().split(">")[1].split("<")[0]);
				pSbj.setSbjGrade(grade);

				// else{
				// pSbj.setSbjLevel(temp);
				// }
				break;
			case 3:
				int times = Integer
						.parseInt(m.group().split(">")[1].split("<")[0]);
				pSbj.setSbjTimes(times);
				break;
			case 4:
				String note = null;
				String rawName = m.group().split(">")[1].split("<")[0];
				Matcher nameM = Pattern.compile("[\\d-]").matcher(rawName);
				if (nameM.find())
					note = nameM.replaceAll("");
				note = note.split("_")[0];
				pSbj.setSbjNote(note);
				break;
			}
			if (count < 5)
				count++;

			if (count == 5) {
				count = 0;
				pSbjList.add(pSbj);
				pSbj = new PersonSbj();
			}

		}
		System.out.println();
		return pSbjList;
	}
}
